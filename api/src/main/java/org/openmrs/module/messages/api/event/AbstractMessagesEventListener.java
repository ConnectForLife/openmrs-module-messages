/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.event.EventListener;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.util.Properties;

public abstract class AbstractMessagesEventListener implements EventListener, DaemonTokenAware {

    private DaemonToken daemonToken;

    @Override
    public void onMessage(Message message) {
        try {
            Properties properties = getProperties(message);
            Daemon.runInDaemonThread(new Runnable() {
                @Override
                public void run() {
                    handleEvent(properties);
                }
            }, daemonToken);
        } catch (JMSException ex) {
            throw new MessagesRuntimeException("Error during handling Messages event", ex);
        }
    }

    @Override
    public void setDaemonToken(DaemonToken daemonToken) {
        this.daemonToken = daemonToken;
    }

    public abstract String getSubject();

    protected abstract void handleEvent(Properties properties);

    protected <T> T getComponent(String beanName, Class<T> type) {
        return Context.getRegisteredComponent(beanName, type);
    }

    private Properties getProperties(Message message) throws JMSException {
        Map<String, Object> properties = new HashMap<>();

        // OpenMRS event module uses underneath MapMessage to construct Message. For some reason retrieving properties
        // from Message interface doesn't work and we have to map object to MapMessage.
        MapMessage mapMessage = (MapMessage) message;
        Enumeration<String> propertiesKey = (Enumeration<String>) mapMessage.getMapNames();

        while (propertiesKey.hasMoreElements()) {
            String key = propertiesKey.nextElement();
            properties.put(key, mapMessage.getObject(key));
        }
        return new Properties(properties);
    }

}
