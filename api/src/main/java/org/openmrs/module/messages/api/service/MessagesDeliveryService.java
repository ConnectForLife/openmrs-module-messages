package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

/**
 * Provides methods related to messages delivery management
 */
public interface MessagesDeliveryService extends OpenmrsService {
    /**
     * Schedules a new event delivery
     *
     * @param executionContext scheduled service context which contains all necessary data to schedule an event
     */
    void scheduleDelivery(ScheduledExecutionContext executionContext);
}
