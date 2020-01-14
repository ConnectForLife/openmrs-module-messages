/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.service.ConfigService;

/**
 * Provides the useful set of method which can be used during work with person status attribute
 */
public class PersonStatusHelper {

    private static final Log LOGGER = LogFactory.getLog(PersonStatusHelper.class);

    private PersonService personService;

    private ConfigService configService;

    /**
     * Provides the {@link PersonStatusDTO} which contains all required values, related to person status.
     * @param personId - value which can be used to identify the person (DB id or UUID)
     * @return - the actual value of person status, if missing then the {@link PersonStatus#MISSING_VALUE} will be return
     */
    public PersonStatusDTO getStatus(String personId) {
        LOGGER.debug(String.format("Get status attribute for person %s", personId));
        Person person = getPersonFromDashboardPersonId(personId);
        if (person != null) {
            PersonStatus status = PersonAttributeUtil.getPersonStatus(person);
            String style = buildMessageStyle(status);
            return new PersonStatusDTO()
                    .setPersonId(personId)
                    .setTitle(status.getTitleKey())
                    .setValue(status.name())
                    .setStyle(style);
        }
        LOGGER.debug(String.format("Couldn't find person for id: %s", personId));
        return null;
    }

    /**
     * Saves the value of the person status attribute
     * @param statusDTO - the value which should be insert to DB, if the status value is equals to
     * {@link PersonStatus#DEACTIVATE} then the voided reason will be set to the previous value
     */
    public void saveStatus(PersonStatusDTO statusDTO) {
        LOGGER.debug(String.format("Trying to save a new status value %s for person %s", statusDTO.getValue(),
                statusDTO.getPersonId()));
        Person person = getPersonFromDashboardPersonId(statusDTO.getPersonId());
        voidTheRecentValue(statusDTO, person);
        saveNewValue(statusDTO, person);
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    private String buildMessageStyle(PersonStatus personStatus) {
        PersonStatusConfigDTO configuration = configService.getPersonStatusConfiguration(personStatus);
        StringBuilder result = new StringBuilder();
        if (configuration != null) {
            result.append("style=\"");
            appendTheBackgroundColor(configuration.getBackgroundColor(), result);
            appendTextColor(configuration.getTextColor(), result);
            result.append("\"");
        }
        return result.toString();
    }

    private Person getPersonFromDashboardPersonId(String personId) {
        if (NumberUtils.isDigits(personId)) {
            return personService.getPerson(Integer.parseInt(personId));
        }
        return personService.getPersonByUuid(personId);
    }

    private void appendTheBackgroundColor(String backgroundColor, StringBuilder result) {
        if (StringUtils.isNotBlank(backgroundColor)) {
            result.append("background-color: ").append(backgroundColor).append("; ");
            result.append("border-color: ").append(backgroundColor).append("; ");
        }
    }

    private void appendTextColor(String textColor, StringBuilder result) {
        if (StringUtils.isNotBlank(textColor)) {
            result.append("color: ").append(textColor).append("; ");
        }
    }

    private void voidTheRecentValue(PersonStatusDTO statusDTO, Person person) {
        PersonAttribute oldStatus = PersonAttributeUtil.getPersonStatusAttribute(person);
        if (oldStatus != null && !statusDTO.getValue().equalsIgnoreCase(PersonStatus.DEACTIVATE.name())) {
            oldStatus.setVoidReason(statusDTO.getReason());
        }
    }

    private void saveNewValue(PersonStatusDTO statusDTO, Person person) {
        PersonAttributeType attributeTypeStatus =
                personService.getPersonAttributeTypeByUuid(ConfigConstants.PATIENT_STATUS_ATTRIBUTE_TYPE_UUID);
        PersonAttribute newStatus = new PersonAttribute(attributeTypeStatus, statusDTO.getValue());
        person.addAttribute(newStatus);
        personService.savePerson(person);
    }
}