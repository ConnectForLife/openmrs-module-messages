package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.messages.api.dao.BaseOpenmrsDataDao;
import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

import java.sql.Date;
import java.time.Instant;

public class MessagingGroupDaoImpl extends BaseOpenmrsDataDao<ScheduledServiceGroup> implements MessagingGroupDao {

    private static final String PATIENT_FIELD = "patient.id";
    private static final String ACTOR_FIELD = "actor.id";
    private static final String MSG_SEND_TIME_FIELD = "msgSendTime";
    private static final String CHANNEL_TYPE_FIELD = "channelType";

    public MessagingGroupDaoImpl() {
        super(ScheduledServiceGroup.class);
    }

    @Override
    public long countRowsByPatientIdActorIdAndMsgSendTime(int patientId, int actorId, Instant msgSendTime,
                                                          String channelType) {
        Criteria criteria = createCriteria();

        criteria.add(Restrictions.eq(PATIENT_FIELD, patientId));
        criteria.add(Restrictions.eq(ACTOR_FIELD, actorId));
        criteria.add(Restrictions.eq(MSG_SEND_TIME_FIELD, Date.from(msgSendTime)));
        criteria.add(Restrictions.eq(CHANNEL_TYPE_FIELD, channelType));

        return countRows(criteria);
    }

    @Override
    public ScheduledServiceGroup saveGroup(ScheduledServiceGroup group) {
        return (ScheduledServiceGroup) saveOrUpdateWithClearingSessionCache(group);
    }

    @Override
    public void flushAndClearSessionCache() {
        flushAndClearCache();
    }

}
