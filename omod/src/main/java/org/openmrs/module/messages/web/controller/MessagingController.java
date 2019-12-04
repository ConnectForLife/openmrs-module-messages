package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.web.model.MessagingParams;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessagingController extends BaseRestController {

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.MessageDetailsMapper")
    private MessageDetailsMapper messageDetailsMapper;

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public MessageDetailsDTO getMessageDetails(MessagingParams messagingParams) {
        PatientTemplateCriteria criteria = messagingParams.getCriteria();
        PagingInfo paging = messagingParams.getPagingInfo();
        List<PatientTemplate> templates = patientTemplateService.findAllByCriteria(
                criteria, paging);
        return messageDetailsMapper.toDto(templates, criteria.getPatientId());
    }
}
