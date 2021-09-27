package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.event.MessagesEvent;

/**
 * Provides methods related to events management
 */
public interface MessagesEventService extends OpenmrsService {
    /**
     * Sends event message
     *
     * @param event object of MessageEvent containing data to send e.g. to Callflow
     */
    void sendEventMessage(MessagesEvent event);
}
