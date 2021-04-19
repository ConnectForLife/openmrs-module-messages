package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Implements methods related to creating, reading, updating and deleting scheduled service group entities
 */
public class MessagingGroupServiceImpl extends BaseOpenmrsDataService<ScheduledServiceGroup>
        implements MessagingGroupService {

    @Override
    @Transactional(readOnly = true)
    public boolean isGroupExists(int patientId, int actorId, Date msgSendTime) {
        return ((MessagingGroupDao) getDao()).countRowsByPatientIdActorIdAndMsgSendTime(patientId, actorId, msgSendTime) > 0;
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
