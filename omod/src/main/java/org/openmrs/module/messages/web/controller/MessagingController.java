package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.GridSettings;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.PatientTemplateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessagingController {

    @Autowired
    @Qualifier("messages.PatientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.MessageDetailsMapper")
    private MessageDetailsMapper messageDetailsMapper;

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public MessageDetailsDTO getMessageDetails(@RequestBody GridSettings gridSettings) {
        PatientTemplateCriteria criteria = gridSettings.getCriteria();
        PagingInfo paging = gridSettings.getPagingInfo();
        List<PatientTemplate> templates = patientTemplateService.findAllByCriteria(
                criteria, paging);
        return messageDetailsMapper.toDto(templates, criteria.getPatientId());
    }
}
