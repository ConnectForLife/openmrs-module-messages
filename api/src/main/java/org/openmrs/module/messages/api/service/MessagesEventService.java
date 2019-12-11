package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.event.MessagesEvent;

public interface MessagesEventService extends OpenmrsService {

    void sendEventMessage(MessagesEvent event);
}
