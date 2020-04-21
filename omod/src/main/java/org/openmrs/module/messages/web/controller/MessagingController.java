package org.openmrs.module.messages.web.controller;

import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
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

import java.util.Date;
import java.util.List;

/**
 * Exposes the endpoints related to managing of scheduled services
 */
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

    /**
     * Fetches detailed settings of configured patient templates
     *
     * @param messagingParams object containing basic information needed to fetch patient templates e.g. patient, actor
     * @return DTO object containing detailed information about messages settings in patient templates
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public MessageDetailsDTO getMessageDetails(MessagingParams messagingParams) {
        PatientTemplateCriteria criteria = messagingParams.getCriteria();
        List<PatientTemplate> patientTemplates = patientTemplateService.findAllByCriteria(
                criteria);

        return messageDetailsMapper.toDto(patientTemplates).withPersonId(criteria.getPersonId());
    }

    /**
     * Fetches executions list for a service from given date range
     *
     * @param personId id of person
     * @param startDate the start of the date range
     * @param endDate the end of the date range
     * @param isPatient identifies whether person is a patient
     * @return list of service execution result
     * @throws ExecutionException
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceResultList> getMessages(
            @RequestParam(value = "personId") Integer personId,
            @RequestParam(value = "startDate") long startDate,
            @RequestParam(value = "endDate") long endDate,
            @RequestParam(value = "isPatient", defaultValue = "true") boolean isPatient
    ) throws ExecutionException {
        Date date1 = new Date(startDate);
        Date date2 = new Date(endDate);
        if (isPatient) {
            return messagingService.retrieveAllServiceExecutions(personId, date1, date2);
        } else {
            return messagingService.retrieveAllServiceExecutionsForActor(personId, date1, date2);
        }
    }

    /**
     * Execution exception handler for internal server error
     *
     * @param exception thrown exception
     * @return an error response
     */
    @ExceptionHandler(ExecutionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorResponseDTO handleIllegalArgumentException(ExecutionException exception) {
        getLogger().error(exception.getMessage(), exception);
        return new ErrorResponseDTO(new ErrorMessage(ERR_EXECUTION, exception.getMessage()));
    }
}
