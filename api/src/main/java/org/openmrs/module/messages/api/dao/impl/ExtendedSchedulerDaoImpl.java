package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.HibernateOpenmrsMetadataDAO;
import org.openmrs.module.messages.api.dao.ExtendedSchedulerDao;
import org.openmrs.scheduler.TaskDefinition;

import java.util.List;

/**
 * The default implementation of {@link ExtendedSchedulerDao}.
 */
public class ExtendedSchedulerDaoImpl extends HibernateOpenmrsMetadataDAO<TaskDefinition> implements ExtendedSchedulerDao {
    private static final String LAST_EXECUTION_TIME_PROP = "lastExecutionTime";
    private static final String TASK_CLASS_PROP = "taskClass";

    public ExtendedSchedulerDaoImpl() {
        super(TaskDefinition.class);
    }

    @Override
    public List<TaskDefinition> getNotExecutedTasksByClassName(String className) {
        final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(this.mappedClass);
        criteria.add(Restrictions.isNull(LAST_EXECUTION_TIME_PROP));
        criteria.add(Restrictions.eq(TASK_CLASS_PROP, className));
        return criteria.list();
    }
}
