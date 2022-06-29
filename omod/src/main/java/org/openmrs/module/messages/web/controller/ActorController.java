/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.net.HttpURLConnection;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.dto.ActorTypeDTO;
import org.openmrs.module.messages.api.dto.ContactTimeDTO;
import org.openmrs.module.messages.api.dto.DefaultContactTimeDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.ActorMapper;
import org.openmrs.module.messages.api.mappers.ActorTypeMapper;
import org.openmrs.module.messages.api.service.ActorService;
import org.openmrs.module.messages.api.util.BestContactTimeHelper;
import org.openmrs.module.messages.web.model.DefaultContactTimeWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * Exposes the endpoints related to managing data for actor
 */
@Api(
    value = "Actor Details",
    tags = {"REST API for Actor Details"}
)
@Controller
@RequestMapping(value = "/messages/actor")
public class ActorController extends BaseRestController {

    @Autowired
    @Qualifier("messages.actorService")
    private ActorService actorService;

    @Autowired
    @Qualifier("messages.actorMapper")
    private ActorMapper actorMapper;

    @Autowired
    @Qualifier("messages.actorTypeMapper")
    private ActorTypeMapper actorTypeMapper;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    /**
     * Fetches all actors for person
     *
     * @param personId id of person
     * @param isPatient identifies whether person is a patient
     * @return list of DTO objects containing all actors related to the person
     */
    @ApiOperation(
        value = "Fetch All Actors For a Person",
        notes = "Fetch All Actors For a Person",
        response = ActorDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "All actors for the person fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Actors for the person not fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "/{personId}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorDTO> getAllForPerson(
        @ApiParam(name = "personId", value = "personId", required = true)
            @PathVariable String personId,
            @ApiParam(name = "isPatient", value = "isPatient", defaultValue = "true")
            @RequestParam(value = "isPatient", defaultValue = "true") boolean isPatient) {
        validateId(personId);
        Person person = personService.getPerson(Integer.valueOf(personId));
        if (person == null) {
            throw new ValidationException(String.format("Person with %s id doesn't exist.", personId));
        }
        Context.refreshEntity(person); //person caching issue fix
        return actorMapper.toDtos(actorService.getAllActorsForPersonId(person.getId(), isPatient));
    }

    /**
     * Fetches best contact time for person
     *
     * @param personId id of person
     * @return best contact time as a text
     */
    @ApiOperation(
        value = "Fetch contact time For a Person",
        notes = "Fetch contact time For a Person",
        response = String.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Contact time for the person fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Contact time for the person not fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "/{personId}/contact-time", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public String getBestContactTime(
        @ApiParam(name = "personId", value = "personId", required = true)
        @PathVariable String personId) {
        validateId(personId);
        return actorService.getContactTime(Integer.valueOf(personId));
    }

    /**
     * Fetches best contact times for people
     *
     * @param personIds list of people ids
     * @return list of DTO objects containing best contact times data
     */
    @ApiOperation(
        value = "Fetch contact times For all Persons",
        notes = "Fetch contact times Actors For all Persons",
        response = ContactTimeDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Contact times for persons fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Contact times for persons not fetched"
            )
        }
    )
    @RequestMapping(value = "/contact-times", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ContactTimeDTO> getBestContactTimes(
        @ApiParam(name = "personIds[]", value = "personIds", required = true)
        @RequestParam(value = "personIds[]") List<Integer> personIds) {
        return actorService.getContactTimes(personIds);
    }

    /**
     * Fetches all actor types
     *
     * @return list of DTO objects containing available actor types
     */
    @ApiOperation(
        value = "Fetch All Actors Types",
        notes = "Fetch All Actors Types",
        response = ActorTypeDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "All actor types fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Actor types not fetched"
            )
        }
    )
    @RequestMapping(value = "/types", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<ActorTypeDTO> getAllActorTypes() {
        return actorTypeMapper.toDtos(actorService.getAllActorTypes());
    }

    /**
     * Fetches default best contact times
     *
     * @return list of DTO objects containing default best contact times data
     */
    @ApiOperation(
        value = "Fetch default best contact times",
        notes = "Fetch default best contact times",
        response = DefaultContactTimeDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Default best times fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Default best times not fetched"
            )
        }
    )
    @RequestMapping(value = "/contact-times/default", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<DefaultContactTimeDTO> getBestContactTimes() {
        return BestContactTimeHelper.getDefaultContactTimes();
    }

    /**
     * Saves default best contact times
     *
     * @param contactTimes list of default best contact times
     */
    @ApiOperation(
        value = "Saves default best contact times",
        notes = "Saves default best contact times"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Default best times saved"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Default best times not saved"
            )
        }
    )
    @RequestMapping(value = "/contact-times/default", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void setBestContactTimes(
        @ApiParam(name = "contactTimes", value = "contactTimes", required = true)
        @RequestBody DefaultContactTimeWrapper contactTimes) {
        BestContactTimeHelper.setDefaultContactTimes(contactTimes.getRecords());
    }

    /**
     * Saves best contact time
     *
     * @param contactTimeDTO DTO object containing best contact time data
     */
    @ApiOperation(
        value = "Save best contact time",
        notes = "Save best contact time"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_CREATED,
                message = "Best contact time saved for a person"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Best contact time not saved for a person"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_BAD_REQUEST,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "/contact-time", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBestContactTime(
        @ApiParam(name = "contactTimeDTO", value = "contactTimeDTO", required = true)
        @RequestBody ContactTimeDTO contactTimeDTO) {
        actorService.saveContactTime(contactTimeDTO);
    }

    /**
     * Saves best contact times
     *
     * @param contactTimeDTOs list of DTO objects containing best contact times data
     */
    @ApiOperation(
        value = "Best contact times for all persons saved",
        notes = "Best contact times for all persons saved"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_CREATED,
                message = "Best contact times for persons saved"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Best contact times for persons not saved"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_BAD_REQUEST,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "/contact-times", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void setBestContactTimes(
        @ApiParam(name = "contactTimeDTOs", value = "contactTimeDTOs", required = true)
        @RequestBody List<ContactTimeDTO> contactTimeDTOs) {
        List<ContactTimeDTO> contactTimes = convertContactTimeDTOs(contactTimeDTOs);
        actorService.saveContactTimes(contactTimes);
    }

    private void validateId(String id) {
        if (StringUtils.isBlank(id) || !StringUtils.isNumeric(id)) {
            throw new ValidationException("Missing id parameter.");
        }
    }

    private List<ContactTimeDTO> convertContactTimeDTOs(List<ContactTimeDTO> contactTimeDTOs) {
        List<ContactTimeDTO> result = contactTimeDTOs;
        // OpenMRS by default convert requested list into List<LinkedHashMap>
        if (!contactTimeDTOs.isEmpty() && !(contactTimeDTOs.get(0) instanceof ContactTimeDTO)) {
            ObjectMapper mapper = new ObjectMapper();
            result = mapper.convertValue(contactTimeDTOs, new TypeReference<List<ContactTimeDTO>>() {
            });
        }
        return result;
    }
}
