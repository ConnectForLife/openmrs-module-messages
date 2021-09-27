package org.openmrs.module.messages.api.dao.impl;

import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.MessagingParameterDao;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;

public class MessagingParameterDaoImpl extends BaseOpenmrsDataDao<ScheduledServiceParameter>
        implements MessagingParameterDao {

    public MessagingParameterDaoImpl() {
        super(ScheduledServiceParameter.class);
    }
}
