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
import org.apache.commons.collections.CollectionUtils;
import org.openmrs.Person;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.util.PersonStatusHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Exposes endpoints which can be used for managing the person status resources
 */
@Api(
    value = "Manage Person Status",
    tags = {"REST API for managing person statuses"}
)
@Controller
@RequestMapping(value = "/messages/person-statuses")
public class PersonStatusController extends BaseRestController {

    @Autowired
    @Qualifier("messages.personStatusHelper")
    private PersonStatusHelper personStatusHelper;

    /**
     * Fetches the person status
     *
     * @param personIdOrUuid DB id or UUID of person
     * @return DTO object containing data about person status
     */
    @ApiOperation(
        value = "Fetch patient status",
        notes = "Fetch patient status",
        response = PersonStatusDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Person status fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Person status not fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "/{personIdOrUuid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PersonStatusDTO getPersonStatus(
        @ApiParam(name = "personIdOrUuid", value = "personIdOrUuid", required = true)
        @PathVariable("personIdOrUuid") String personIdOrUuid) {
        PersonStatusDTO result = personStatusHelper.getStatus(personIdOrUuid);
        if (result == null) {
            throw new EntityNotFoundException(String.format("Could not fetch person status for personIdOrUuid: %s",
                    HtmlUtils.htmlEscape(personIdOrUuid)));
        }
        return result;
    }

    /**
     * Updates the person status
     *
     * @param personIdOrUuid  DB id or UUID of person
     * @param personStatusDTO DTO object containing data about person status
     * @return updated DTO object containing data about person status
     */
    @ApiOperation(
        value = "Update Person status",
        notes = "Update person status",
        response = PersonStatusDTO.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Person status details updated"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Person status details could not be updated"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_NOT_FOUND,
                message = "Person not found"
            )
        }
    )
    @RequestMapping(value = "/{personIdOrUuid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PersonStatusDTO updatePersonStatus(
        @ApiParam(name = "personIdOrUuid", value = "personIdOrUuid", required = true)
        @PathVariable("personIdOrUuid") String personIdOrUuid,
        @ApiParam(name = "personStatusDTO", value = "personStatusDTO", required = true)
        @RequestBody PersonStatusDTO personStatusDTO) {
        Person person = personStatusHelper.getPersonFromDashboardPersonId(personIdOrUuid);
        personStatusDTO.setPersonId(person.getPersonId());
        personStatusHelper.saveStatus(personStatusDTO);
        return personStatusHelper.getStatus(personIdOrUuid);
    }

    /**
     * Fetches the possible reasons for patient deactivation
     *
     * @return list of possible reasons
     */
    @ApiOperation(
        value = "Fetch patient deactivation reasons",
        notes = "Fetch patient deactivation reasons",
        response = String.class
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                code = HttpURLConnection.HTTP_OK,
                message = "Patient deactivation reasons fetched"
            ),
            @ApiResponse(
                code = HttpURLConnection.HTTP_INTERNAL_ERROR,
                message = "Patient deactivation reasons not fetched"
            )
        }
    )
    @RequestMapping(value = "/reasons", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<String> getPossibleReasons() {
        List<String> result = personStatusHelper.getPossibleReasons();
        if (CollectionUtils.isEmpty(result)) {
            throw new EntityNotFoundException("Could not fetch person status possible reasons. Check configuration.");
        }
        return result;
    }
}
