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

        for (String key : params.keySet()) {
            eventMessage.put(key, (Serializable) params.get(key));
        }

        return eventMessage;
    }
}
