package org.openmrs.module.messages.api.dao;

import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

import java.time.Instant;

/**
 * Data access object for the scheduled service group entities
 */
public interface MessagingGroupDao extends BaseOpenmrsPageableDao<ScheduledServiceGroup> {

    /**
     * Counts rows with provided patient id, actor id and msg send time
     *
     * @param patientId   id of patient
     * @param actorId     id of actor
     * @param msgSendTime messages send time
     * @param channelType the channel type
     * @return number of rows
     */
    long countRowsByPatientIdActorIdAndMsgSendTime(int patientId, int actorId, Instant msgSendTime, String channelType);

    ScheduledServiceGroup saveGroup(ScheduledServiceGroup group);

    void flushAndClearSessionCache();
}
