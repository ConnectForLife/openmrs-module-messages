/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.controller;

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

import java.util.List;

/**
 * Exposes endpoints which can be used for managing the person status resources
 */
@Controller
@RequestMapping("/messages/person-statuses")
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
    @RequestMapping(value = "/{personIdOrUuid}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PersonStatusDTO getPersonStatus(@PathVariable("personIdOrUuid") String personIdOrUuid) {
        PersonStatusDTO result = personStatusHelper.getStatus(personIdOrUuid);
        if (result == null) {
            throw new EntityNotFoundException(String.format(
                    "Could not fetch person status for personIdOrUuid: %s", personIdOrUuid));
        }
        return result;
    }

    /**
     * Updates the person status
     *
     * @param personIdOrUuid DB id or UUID of person
     * @param personStatusDTO DTO object containing data about person status
     * @return updated DTO object containing data about person status
     */
    @RequestMapping(value = "/{personIdOrUuid}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public PersonStatusDTO updatePersonStatus(@PathVariable("personIdOrUuid") String personIdOrUuid,
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
