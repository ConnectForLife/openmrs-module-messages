package org.openmrs.module.messages.web.controller;

import org.openmrs.Patient;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.web.model.PageableParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Controller
@RequestMapping("/patient-templates")
public class PatientTemplateController {

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
}
