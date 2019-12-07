package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

public class MessagingGroupDaoImpl extends BaseOpenmrsDataDao<ScheduledServiceGroup>
        implements MessagingGroupDao {
    
    public MessagingGroupDaoImpl() {
        super(ScheduledServiceGroup.class);
    }
}
