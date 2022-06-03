/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.service.MessagesEventService;
import org.openmrs.module.messages.api.service.ServiceResultsHandlerService;
import org.openmrs.module.messages.api.util.PersonUtil;

import static org.openmrs.module.messages.api.constants.ConfigConstants.PERSON_PHONE_ATTR;

/**
 * Implements basic methods related to the handling of service results
 */
public abstract class AbstractServiceResultsHandlerService implements ServiceResultsHandlerService {

    private PersonService personService;

    private MessagesEventService messagesEventService;

    public void setMessagesEventService(MessagesEventService messagesEventService) {
        this.messagesEventService = messagesEventService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    protected void sendEventMessage(MessagesEvent messagesEvent) {
        messagesEventService.sendEventMessage(messagesEvent);
    }

    protected String getPersonPhone(Integer personId) {
        Person person = personService.getPerson(personId);
        if (person == null) {
            throw new MessagesRuntimeException(String.format("Person with id %s does not exist", personId));
        }
        Context.refreshEntity(person); //person caching issue fix
        PersonAttribute attribute = person.getAttribute(PERSON_PHONE_ATTR);
        if (attribute == null || StringUtils.isBlank(attribute.getValue())) {
            throw new MessagesRuntimeException(String.format("Phone number not specified for " +
                "person with name: %s and id: %d", PersonUtil.getPersonFullName(person), person.getId()));
        }
        return attribute.getValue();
    }
}
