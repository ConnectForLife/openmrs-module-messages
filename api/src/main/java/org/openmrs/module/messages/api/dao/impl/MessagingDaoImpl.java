package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.api.db.hibernate.HibernateOpenmrsDataDAO;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.model.ScheduledService;

public class MessagingDaoImpl extends HibernateOpenmrsDataDAO<ScheduledService> implements MessagingDao {
    
    public MessagingDaoImpl() {
        super(ScheduledService.class);
    }
}
