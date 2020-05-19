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
import org.openmrs.api.context.Daemon;
import org.openmrs.event.Event;
import org.openmrs.event.SubscribableEventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

/**
 * Abstract class for subscribable event listening.
 */
public abstract class PeopleActionListener implements SubscribableEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(PeopleActionListener.class);

    private PersonService personService;

    private DaemonToken daemonToken;

    private Event event = new Event();

    /**
     * Defines the list of Actions which will be performed {@link #performAction(Message)} by this listener
     * @return a list of supported Actions by this listener
     */
    public abstract List<String> subscribeToActions();

    /**
     * Perform action on specific event.
     * @param message - processed message with provided properties
     */
    public abstract void performAction(Message message);

    public void init() {
        LOGGER.debug("The PeopleActionListener was initialized and registered as a subscriber");
        event.setSubscription(this);
    }

    public void close() {
        LOGGER.debug("The PeopleActionListener was unsubscribe");
        event.unsetSubscription(this);
    }

    /**
     * Defines the list of classes which will be handled {@link #performAction(Message)} by this listener
     * @return a list of classes that this can handle
     */
    @Override
    public List<Class<? extends OpenmrsObject>> subscribeToObjects() {
        return Arrays.asList(Person.class, Patient.class);
    }

    /**
     * Handle messages received by this listener
     * @param message - processed message with provided properties
     */
    @Override
    public void onMessage(Message message) {
        LOGGER.trace("Handle onMessage");
        Daemon.runInDaemonThread(new Runnable() {
            @Override
            public void run() {
                performAction(message);
            }
        }, daemonToken);
    }

    public void setDaemonToken(DaemonToken daemonToken) {
        this.daemonToken = daemonToken;
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

    private String getMessagePropertyValue(Message message, String propertyName) {
        LOGGER.debug(String.format("Handle getMessagePropertyValue for '%s' property", propertyName));
        try {
            validateMessage(message);
            return ((MapMessage) message).getString(propertyName);
        } catch (JMSException e) {
            throw new MessagesRuntimeException("Exception while get uuid of created object from JMS message. " + e, e);
        }
    }

    private Person getPerson(String personUuid) {
        LOGGER.debug(String.format("Handle getPerson for '%s' uuid", personUuid));
        Person person = personService.getPersonByUuid(personUuid);
        if (person == null) {
            throw new MessagesRuntimeException(String.format("Unable to retrieve person by uuid: %s", personUuid));
        }
        Context.refreshEntity(person); //person caching issue fix
        return person;
    }

    private void validateMessage(Message message) {
        if (!(message instanceof MapMessage)) {
            throw new MessagesRuntimeException("Event message had to be MapMessage");
        }
    }
}
