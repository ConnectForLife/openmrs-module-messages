package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

/**
 * Provides methods related to messages delivery management
 * <p>
 * The service automatically schedules proper message delivery tasks during system startup.
 * </p>
 */
public interface MessagesDeliveryService extends OpenmrsService {
    /**
     * Schedules a new event delivery
     *
     * @param executionContext scheduled service context which contains all necessary data to schedule an event
     */
    void scheduleDelivery(ScheduledExecutionContext executionContext);
}
