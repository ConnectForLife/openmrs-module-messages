package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.PagingInfo;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.web.model.MessagingParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessagingController extends BaseRestController {

    private static final String ERR_EXECUTION = "system.error.execution";

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.MessageDetailsMapper")
    private MessageDetailsMapper messageDetailsMapper;

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public PageDTO<MessageDetailsDTO> getMessageDetails(MessagingParams messagingParams) {
        PatientTemplateCriteria criteria = messagingParams.getCriteria();
        PagingInfo paging = messagingParams.getPagingInfo();
        List<PatientTemplate> templates = patientTemplateService.findAllByCriteria(
                criteria, paging);

        // the response is wrapped in a page because the collection inside is paginated
        List<MessageDetailsDTO> details = new ArrayList<>();
        details.add(messageDetailsMapper.toDto(templates).withPatientId(criteria.getPatientId()));
        return new PageDTO<>(details, paging);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceResultList> getMessages(
            @RequestParam(value = "patientId") Integer patientId,
            @RequestParam(value = "startDate") long startDate,
            @RequestParam(value = "endDate") long endDate
    ) throws ExecutionException {
        Date date1 = new Date(startDate);
        Date date2 = new Date(endDate);
        return messagingService.retrieveAllServiceExecutions(patientId, date1, date2);
    }

    @ExceptionHandler(ExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDTO handleIllegalArgumentException(ExecutionException exception) {
        getLogger().error(exception.getMessage(), exception);
        return new ErrorResponseDTO(new ErrorMessage(ERR_EXECUTION, exception.getMessage()));
    }
}
