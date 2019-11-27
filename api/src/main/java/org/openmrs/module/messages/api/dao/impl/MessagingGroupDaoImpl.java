package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.api.db.hibernate.HibernateOpenmrsDataDAO;
import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

public class MessagingGroupDaoImpl extends HibernateOpenmrsDataDAO<ScheduledServiceGroup>
        implements MessagingGroupDao {
    
    public MessagingGroupDaoImpl() {
        super(ScheduledServiceGroup.class);
    }
}
