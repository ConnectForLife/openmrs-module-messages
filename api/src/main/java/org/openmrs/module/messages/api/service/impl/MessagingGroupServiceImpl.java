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

import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Implements methods related to creating, reading, updating and deleting scheduled service group entities
 */
public class MessagingGroupServiceImpl extends BaseOpenmrsDataService<ScheduledServiceGroup>
        implements MessagingGroupService {

    @Override
    @Transactional(readOnly = true)
    public boolean isGroupExists(int patientId, int actorId, Instant msgSendTime, String channelType) {
        return ((MessagingGroupDao) getDao()).countRowsByPatientIdActorIdAndMsgSendTime(patientId, actorId, msgSendTime,
                channelType) > 0;
    }

    @Override
    @Transactional
    public ScheduledServiceGroup saveGroup(ScheduledServiceGroup group) {
        return ((MessagingGroupDao) getDao()).saveGroup(group);
    }

    @Override
    @Transactional
    public void flushAndClearSessionCache() {
        ((MessagingGroupDao) getDao()).flushAndClearSessionCache();
    }
}
