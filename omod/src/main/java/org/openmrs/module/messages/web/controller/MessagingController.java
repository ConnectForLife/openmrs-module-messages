package org.openmrs.module.messages.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.HttpURLConnection;
import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.ErrorMessage;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.model.ServiceResultListDTO;
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

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Exposes the endpoints related to managing of scheduled services
 */
@Api(
    value = "Managing of scheduled messaging services",
    tags = {"REST API for managing scheduled messaging services"}
)
@Controller
@RequestMapping(value = "/messages")
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
     *@return DTO object containing detailed information about messages settings in patient templates
     */
    @ApiOperation(
        value = "Fetch detailed settings of configured patient templates",
        notes = "Fetch detailed settings of configured patient templates",
        response = MessageDetailsDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Default patient template details fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Default patient template details not fetched"
            )
        }
    )
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public MessageDetailsDTO getMessageDetails(
        @ApiParam(name = "personId", value = "personId")
        @RequestParam(value = "personId") Integer personId,
        @ApiParam(name = "isPatient", value = "isPatient", defaultValue = "true")
        @RequestParam(value = "isPatient", defaultValue = "true") boolean isPatient) {

        final PatientTemplateCriteria criteria;
        if (isPatient) {
            criteria = PatientTemplateCriteria.forPatientId(personId);
        } else {
            criteria = PatientTemplateCriteria.forActorId(personId);
        }

        List<PatientTemplate> patientTemplates = patientTemplateService.findAllByCriteria(criteria);

        return messageDetailsMapper.toDto(patientTemplates).withPersonId(criteria.getPersonId());
    }

    /**
     * Fetches executions list for a service from given date range
     *
     * @param personId       id of person
     * @param startDateMilli the start of the date range
     * @param endDateMilli   the end of the date range
     * @param isPatient      identifies whether person is a patient
     * @return list of service execution result
     */
    @ApiOperation(
        value = "Fetch execution list for a service for a date range",
        notes = "Fetch execution list for a service for a date range",
        response = ServiceResultListDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Execution details fetched for a date range"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Execution details not fetched for the date range"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<ServiceResultListDTO> getMessages(
        @ApiParam(name = "personId", value = "personId", required = true)
        @RequestParam(value = "personId") Integer personId,
        @ApiParam(name = "startDate", value = "startDate", required = true)
        @RequestParam(value = "startDate") long startDateMilli,
        @ApiParam(name = "endDate", value = "endDate", required = true)
        @RequestParam(value = "endDate") long endDateMilli,
        @ApiParam(name = "isPatient", value = "isPatient", defaultValue = "true")
        @RequestParam(value = "isPatient", defaultValue = "true") boolean isPatient) {
        final ZonedDateTime startDate = DateUtil.ofEpochMilliInUserTimeZone(startDateMilli);
        final ZonedDateTime endDate = DateUtil.ofEpochMilliInUserTimeZone(endDateMilli);

        final List<ServiceResultList> resultLists;

        if (isPatient) {
            resultLists = messagingService.retrieveAllServiceExecutions(personId, startDate, endDate);
        } else {
            resultLists = messagingService.retrieveAllServiceExecutionsForActor(personId, startDate, endDate);
        }

        return resultLists.stream().map(ServiceResultListDTO::new).collect(Collectors.toList());
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
