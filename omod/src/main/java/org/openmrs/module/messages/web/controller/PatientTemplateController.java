package org.openmrs.module.messages.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleStateException;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.web.model.PageableParams;
import org.openmrs.module.messages.web.model.PatientTemplatesWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.openmrs.module.messages.api.model.ErrorMessageEnum.ERR_SYSTEM;
import static org.openmrs.module.messages.api.model.TemplateFieldType.SERVICE_TYPE;

@Controller
@RequestMapping("/messages/patient-templates")
public class PatientTemplateController extends BaseRestController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    @Qualifier("patientService")
    private PatientService patientService;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.patientTemplateMapper")
    private PatientTemplateMapper mapper;

    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PageDTO<PatientTemplateDTO> getPatientTemplates(@PathVariable("id") Integer id,
                                                           PageableParams pageableParams) {
        Patient patient = new Patient();
        patient.setId(id);
        PagingInfo pagingInfo = pageableParams.getPagingInfo();

        List<PatientTemplate> templates = patientTemplateService.findAllByCriteria(
            new PatientTemplateCriteria(patient),
            pagingInfo);
        return new PageDTO<>(mapper.toDtos(templates), pagingInfo);
    }

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientTemplatesWrapper updatePatientTemplates(@PathVariable("id") Integer id,
                                                          @RequestBody PatientTemplatesWrapper wrapper)
            throws ValidationException, APIException, StaleStateException {
        validateConsistency(id, wrapper.getPatientTemplates());
        validatePatientTemplates(wrapper.getPatientTemplates());
        List<PatientTemplate> saved = patientTemplateService.batchSave(
            mapper.fromDtos(wrapper.getPatientTemplates()), id);
        return new PatientTemplatesWrapper(mapper.toDtos(saved));
    }

    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDTO handleAPIException(APIException ex) {
        logger.error(ex.getMessage(), ex);
        return new ErrorResponseDTO(new ErrorMessage(ERR_SYSTEM.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(StaleStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponseDTO handleStaleStateException(StaleStateException ex) {
        logger.error(ex.getMessage(), ex);
        return new ErrorResponseDTO(new ErrorMessage(ERR_SYSTEM.getCode(), ex.getMessage()));
    }

    private void validateConsistency(Integer id, List<PatientTemplateDTO> templates)
            throws ValidationException {
        Patient patient = patientService.getPatient(id);
        if (patient == null || patient.getId() == null) {
            throw new ValidationException(
                String.format("Invalid patient id: %s", id));
        }

        for (PatientTemplateDTO template : templates) {
            if (template.getPatientId() == null || !template.getPatientId().equals(id)) {
                throw new ValidationException(
                    String.format("Patient templates contains invalid patient id! %s != %s",
                        id, template.getPatientId()));
            }
        }
    }

    private void validatePatientTemplates(List<PatientTemplateDTO> templates)
            throws ValidationException {
        for (PatientTemplateDTO patientTemplate : templates) {
            for (TemplateFieldValueDTO tfv : patientTemplate.getTemplateFieldValues()) {
                Template template = templateService.getById(patientTemplate.getTemplateId());
                validateTemplateFieldValue(tfv, template.getTemplateFields());
            }
        }
    }

    private void validateTemplateFieldValue(TemplateFieldValueDTO tfv,
                                            List<TemplateField> templateFields) {
        for (TemplateField tf : templateFields) {
            if (tf.getId().equals(tfv.getTemplateFieldId()) && SERVICE_TYPE.equals(tf.getTemplateFieldType())) {
                validateServiceType(tfv);
            }
        }
    }

    private void validateServiceType(TemplateFieldValueDTO tfv) {
        boolean isValid;
        try {
            ChannelType.fromName(tfv.getValue());
            isValid = true;

        } catch (Exception ex) {
            logger.error(ex);
            isValid = false;
        }
        if (!isValid) {
            throw new ValidationException(String.format("Invalid service type: %s",
                tfv.getValue()));
        }
    }

}
