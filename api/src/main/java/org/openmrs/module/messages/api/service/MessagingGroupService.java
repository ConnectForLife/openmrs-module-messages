/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
