/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener.subscribable;

import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.event.Event;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import javax.jms.Message;

/**
 * The specific listener which creates the initial attributes after people (Person / Patient) creation.
 */
public class InitialPersonAttributeListener extends PeopleActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialPersonAttributeListener.class);

    private ConfigService configService;

    /**
     * Defines the list of Actions which will be performed {@link #performAction(Message)} by this listener
     * @return a list of supported Actions by this listener
     */
    @Override
    public List<String> subscribeToActions() {
        return Collections.singletonList(Event.Action.CREATED.name());
    }

    /**
     * Perform action on specific event.
     * @param message - processed message with provided properties
     */
    @Override
    public void performAction(Message message) {
        Person person = extractPerson(message);
        LOGGER.debug("Creating attribute for {} person", person);
        person.addAttribute(createStatusAttribute());
        getPersonService().savePerson(person);
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    private PersonAttribute createStatusAttribute() {
        PersonAttributeType attributeType = getStatusType();
        PersonStatus status = getInitialValue();
        return new PersonAttribute(attributeType, status.name());
    }

    private PersonAttributeType getStatusType() {
        return getPersonService().getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_UUID);
    }

    private PersonStatus getInitialValue() {
        PersonStatus status = PersonStatus.ACTIVATED;
        if (configService.isConsentControlEnabled()) {
            status = PersonStatus.NO_CONSENT;
        }
        return status;
    }
}
