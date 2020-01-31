package org.openmrs.module.messages.web.controller;

import java.util.List;
import org.openmrs.Patient;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.api.dto.DefaultPatientTemplateStateDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.DefaultPatientTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/messages/defaults")
public class DefaultPatientTemplateController extends BaseRestController {

    @Autowired
    @Qualifier("patientService")
    private PatientService patientService;

    @Autowired
    @Qualifier("messages.defaultPatientTemplateService")
    private DefaultPatientTemplateService defaultPatientTemplateService;

    @Autowired
    @Qualifier("messages.patientTemplateMapper")
    private PatientTemplateMapper mapper;

    @RequestMapping(value = "{patientId}/check", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DefaultPatientTemplateStateDTO checkDefaultValuesUsed(@PathVariable("patientId") Integer id) {
        List<PatientTemplate> lacking =
            defaultPatientTemplateService.findLackingPatientTemplates(getPatientById(id));

        MessageDetailsDTO details = defaultPatientTemplateService
            .getDetailsForRealAndDefault(getPatientById(id), lacking);

        return new DefaultPatientTemplateStateDTO(mapper.toDtos(lacking), details);
    }

    @RequestMapping(value = "{patientId}/generate-and-save", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<PatientTemplateDTO> generateDefaultPatientTemplates(@PathVariable("patientId") Integer id) {
        return mapper.toDtos(
            defaultPatientTemplateService.generateDefaultPatientTemplates(getPatientById(id))
        );
    }

    private Patient getPatientById(Integer id) {
        if (id == null) {
            throw new ValidationException("Provided id cannot be null");
        }

        Patient patient = patientService.getPatient(id);
        if (patient == null) {
            throw new ValidationException(String.format("Patient with id %s doesn't exist", id));
        }
        return patient;
    }
}
