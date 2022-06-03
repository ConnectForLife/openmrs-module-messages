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

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.event.Event;
import org.openmrs.event.EventMessage;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.service.MessagesEventService;

import java.io.Serializable;
import java.util.Map;

/**
 * Implements methods related to events management
 */
public class MessagesEventServiceImpl extends BaseOpenmrsService implements MessagesEventService {

    @Override
    public void sendEventMessage(MessagesEvent event) {
        Event.fireEvent(event.getSubject(), convertParamsToEventMessage(event.getParameters()));
    }

    private EventMessage convertParamsToEventMessage(Map<String, Object> params) {
        EventMessage eventMessage = new EventMessage();

        for (Map.Entry<String, Object> param : params.entrySet()) {
            eventMessage.put(param.getKey(), (Serializable) param.getValue());
        }

        return eventMessage;
    }
}
