/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.event.Event;

import java.util.List;
import org.openmrs.module.messages.api.event.AbstractMessagesEventListener;

public final class MessagesEventListenerFactory {
    private static final Log LOGGER = LogFactory.getLog(MessagesEventListenerFactory.class);

    public static void registerEventListeners() {
        List<AbstractMessagesEventListener> eventComponents =
                Context.getRegisteredComponents(AbstractMessagesEventListener.class);
        for (AbstractMessagesEventListener eventListener : eventComponents) {
            subscribeListener(eventListener);
        }
    }

    public static void unRegisterEventListeners() {
        List<AbstractMessagesEventListener> eventComponents =
                Context.getRegisteredComponents(AbstractMessagesEventListener.class);
        for (AbstractMessagesEventListener eventListener : eventComponents) {
            unSubscribeListener(eventListener);
        }
    }

    private static void subscribeListener(AbstractMessagesEventListener callFlowEventListener) {
        LOGGER.debug(String.format("The Message Module has subscribed %s listener on the %s subject.",
                callFlowEventListener.getClass().toString(), callFlowEventListener.getSubject()));
        Event.subscribe(callFlowEventListener.getSubject(), callFlowEventListener);
    }

    private static void unSubscribeListener(AbstractMessagesEventListener callFlowEventListener) {
        LOGGER.debug(String.format("The Messages Module has unsubscribed %s listener on the %s subject.",
                callFlowEventListener.getClass().toString(), callFlowEventListener.getSubject()));
        Event.unsubscribe(callFlowEventListener.getSubject(), callFlowEventListener);
    }

    private MessagesEventListenerFactory() {
    }
}
