/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener.subscribable;

import org.openmrs.OpenmrsObject;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.Event;
import org.openmrs.event.SubscribableEventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import java.util.List;

public abstract class BaseActionListener implements SubscribableEventListener, DaemonTokenAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseActionListener.class);

    private DaemonToken daemonToken;

    private Event event = new Event();

    /**
     * Defines the list of Actions which will be performed {@link #performAction(Message)} by this listener
     * @return a list of supported Actions by this listener
     */
    public abstract List<String> subscribeToActions();

    /**
     * Performs action on specific event.
     * @param message - processed message with provided properties
     */
    public abstract void performAction(Message message);

    /**
     * Defines the list of classes which will be handled {@link #performAction(Message)} by this listener
     * @return a list of classes that this can handle
     */
    public abstract List<Class<? extends OpenmrsObject>> subscribeToObjects();

    /**
     * Handles messages received by this listener
     * @param message - processed message with provided properties
     */
    @Override
    public void onMessage(Message message) {
        LOGGER.trace("Handle onMessage");
        Daemon.runInDaemonThread(() -> performAction(message), daemonToken);
    }

    public void init() {
        LOGGER.debug("The PeopleActionListener was initialized and registered as a subscriber");
        event.setSubscription(this);
    }

    public void close() {
        LOGGER.debug("The PeopleActionListener was unsubscribe");
        event.unsetSubscription(this);
    }

    public void setDaemonToken(DaemonToken daemonToken) {
        this.daemonToken = daemonToken;
    }

    protected String getMessagePropertyValue(Message message, String propertyName) {
        LOGGER.debug("Handle getMessagePropertyValue for {} property", propertyName);
        try {
            validateMessage(message);
            return ((MapMessage) message).getString(propertyName);
        } catch (JMSException e) {
            throw new MessagesRuntimeException("Exception while get uuid of created object from JMS message. " + e, e);
        }
    }

    private void validateMessage(Message message) {
        if (!(message instanceof MapMessage)) {
            throw new MessagesRuntimeException("Event message has to be MapMessage");
        }
    }
}
