/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
