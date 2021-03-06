package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

import java.time.Instant;

/**
 * Provides methods for creating, reading, updating and deleting scheduled service group entities
 */
public interface MessagingGroupService extends OpenmrsDataService<ScheduledServiceGroup> {

    /**
     * Returns true if a scheduled service group with provided fields exists in system.
     *
     * @param patientId   id of patient
     * @param actorId     id of actor
     * @param msgSendTime message send time
     * @param channelType the channel type (service type)
     * @return true if a group exists.
     */
    boolean isGroupExists(int patientId, int actorId, Instant msgSendTime, String channelType);

    ScheduledServiceGroup saveGroup(ScheduledServiceGroup group);

    void flushAndClearSessionCache();
}
