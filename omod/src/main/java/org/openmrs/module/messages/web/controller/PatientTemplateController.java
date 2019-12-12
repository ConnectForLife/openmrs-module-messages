package org.openmrs.module.messages.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
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

@Controller
@RequestMapping("/messages/patient-templates")
public class PatientTemplateController extends BaseRestController {

    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private PatientService patientService;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.patientTemplateMapper")
    private PatientTemplateMapper mapper;

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
        return new PageDTO<PatientTemplateDTO>(mapper.toDtos(templates), pagingInfo);
    }

    @RequestMapping(value = "/patient/{id}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PatientTemplatesWrapper updatePatientTemplates(@PathVariable("id") Integer id,
                                                           @RequestBody PatientTemplatesWrapper wrapper)
                                                           throws ValidationException, APIException {
        validateConsistency(id, wrapper.getPatientTemplates());
        List<PatientTemplate> saved = patientTemplateService.saveOrUpdate(
                mapper.fromDtos(wrapper.getPatientTemplates()));
        return new PatientTemplatesWrapper(mapper.toDtos(saved));
    }

    @ExceptionHandler(APIException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDTO handleAPIException(ValidationException ex) {
        logger.error(ex.getMessage(), ex);
        return ex.getErrorResponse();
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
}
