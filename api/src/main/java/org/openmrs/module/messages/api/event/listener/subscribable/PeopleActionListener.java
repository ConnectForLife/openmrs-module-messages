/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener.subscribable;

import org.openmrs.OpenmrsObject;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import java.util.Arrays;
import java.util.List;

/**
 * Abstract class for subscribable event listening.
 */
public abstract class PeopleActionListener extends BaseActionListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleActionListener.class);

    private PersonService personService;

    /**
     * @see BaseActionListener#subscribeToObjects()
     */
    @Override
    public List<Class<? extends OpenmrsObject>> subscribeToObjects() {
        return Arrays.asList(Person.class, Patient.class);
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    /**
     * Extracts the person from the received messages.
     * Fetch the {@link Person} object from DB based on UUID from message properties.
     * @param message message with properties.
     * @return retrieved patient
     */
    protected Person extractPerson(Message message) {
        LOGGER.trace("Handle extractPerson");
        String personUuid = getMessagePropertyValue(message, MessagesConstants.UUID_KEY);
        return getPerson(personUuid);
    }

    private Person getPerson(String personUuid) {
        LOGGER.debug("Handle getPerson for {} uuid", personUuid);
        Person person = personService.getPersonByUuid(personUuid);
        if (person == null) {
            throw new MessagesRuntimeException(String.format("Unable to retrieve person by uuid: %s", personUuid));
        }
        Context.refreshEntity(person); //person caching issue fix
        return person;
    }
}
