/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.ValidationException;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.service.ConfigService;

import java.util.List;

/**
 * Provides the useful set of method which can be used during work with person status attribute
 */
public class PersonStatusHelper {

    private static final Log LOGGER = LogFactory.getLog(PersonStatusHelper.class);

    private PersonService personService;

    private ConfigService configService;

    /**
     * Provides the {@link PersonStatusDTO} which contains all required values, related to person status.
     * @param personIdOrUuid - value which can be used to identify the person (DB id or UUID)
     * @return - the actual value of person status, if missing then the {@link PersonStatus#MISSING_VALUE} will be return
     */
    public PersonStatusDTO getStatus(String personIdOrUuid) {
        LOGGER.debug(String.format("Get status attribute for person %s", personIdOrUuid));
        Person person = getPersonFromDashboardPersonId(personIdOrUuid);
        if (person != null) {
            PersonStatus status = PersonAttributeUtil.getPersonStatus(person);
            PersonAttribute lastReason = PersonAttributeUtil.getPersonStatusReasonAttribute(person);
            String style = buildMessageStyle(status);
            return new PersonStatusDTO()
                    .setPersonId(person.getPersonId())
                    .setPersonUuid(person.getUuid())
                    .setTitle(status.getTitleKey())
                    .setValue(status.name())
                    .setStyle(style)
                    .setReason(lastReason != null ? lastReason.getValue() : null);
        }
        LOGGER.debug(String.format("Couldn't find person for id/UUID: %s", personIdOrUuid));
        return null;
    }

    /**
     * Saves the value of the person status attribute
     * @param statusDTO - the value which should be insert to DB, if the status value is equals to
     * {@link PersonStatus#DEACTIVATED} then the voided reason will be set to the previous value
     */
    public void saveStatus(PersonStatusDTO statusDTO) {
        LOGGER.debug(String.format("Trying to save a new status value %s for person %s", statusDTO.getValue(),
                statusDTO.getPersonId()));
        if (!isValidStatusValue(statusDTO.getValue())) {
            throw new ValidationException(String.format("Not valid value of status: %s", statusDTO.getValue()));
        }
        if (PersonStatus.DEACTIVATED.name().equals(statusDTO.getValue()) && !isValidReason(statusDTO.getReason())) {
            throw new ValidationException(String.format("Not valid value of reason: %s", statusDTO.getReason()));
        }
        Person person = getPersonFromDashboardPersonId(statusDTO.getPersonId().toString());
        if (person != null) {
            changeStatusReason(statusDTO, person);
            saveNewValue(statusDTO, person);
        }
    }

    /**
     * Returns the list of possible reasons for changing the person status
     * @return - possible reasons
     */
    public List<String> getPossibleReasons() {
        return configService.getPersonStatusPossibleChangeReasons();
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    private String buildMessageStyle(PersonStatus personStatus) {
        PersonStatusConfigDTO configuration = configService.getPersonStatusConfiguration(personStatus);
        return configuration.getStyle();
    }

    public Person getPersonFromDashboardPersonId(String personId) {
        Person person;
        if (NumberUtils.isDigits(personId)) {
            person = personService.getPerson(Integer.parseInt(personId));

        } else {
            person = personService.getPersonByUuid(personId);
        }
        if (person != null) {
            Context.refreshEntity(person); //person caching issue fix
        }
        return person;
    }

    private void changeStatusReason(PersonStatusDTO statusDTO, Person person) {
        PersonAttributeType attributeTypeStatusReason =
                personService.getPersonAttributeTypeByUuid(ConfigConstants.PERSON_STATUS_REASON_ATTRIBUTE_TYPE_UUID);
        if (attributeTypeStatusReason != null) {
            Context.refreshEntity(attributeTypeStatusReason); //person caching issue fix
        }
        PersonAttribute newStatus = new PersonAttribute(attributeTypeStatusReason, statusDTO.getReason());
        person.addAttribute(newStatus);
        personService.savePerson(person);
        Context.flushSession(); //person caching issue fix
    }

    private void saveNewValue(PersonStatusDTO statusDTO, Person person) {
        PersonAttributeType attributeTypeStatus =
                personService.getPersonAttributeTypeByUuid(ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_UUID);
        PersonAttribute newStatus = new PersonAttribute(attributeTypeStatus, statusDTO.getValue());
        person.addAttribute(newStatus);
        personService.savePerson(person);
        Context.flushSession(); //person caching issue fix
    }

    private boolean isValidStatusValue(String value) {
        boolean valid = false;
        try {
            if (!PersonStatus.valueOf(value).equals(PersonStatus.MISSING_VALUE)) {
                valid = true;
            }
        } catch (IllegalArgumentException ex) {
            valid = false;
        }
        return valid;
    }

    private boolean isValidReason(String reason) {
        return getPossibleReasons().contains(reason);
    }
}
