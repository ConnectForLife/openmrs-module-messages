/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.openmrs.api.db.hibernate.HibernateOpenmrsMetadataDAO;
import org.openmrs.module.messages.api.dao.ExtendedSchedulerDao;
import org.openmrs.scheduler.TaskDefinition;

import java.sql.Date;
import java.time.Instant;
import java.util.List;

/**
 * The default implementation of {@link ExtendedSchedulerDao}.
 */
public class ExtendedSchedulerDaoImpl extends HibernateOpenmrsMetadataDAO<TaskDefinition> implements ExtendedSchedulerDao {
  private static final String NAME_PROP = "name";
  private static final String START_TIME_PROP = "startTime";
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

  @Override
  public List<TaskDefinition> getTasksByPrefixAndAfterStartTime(String taskNamePrefix, Instant afterStartTime) {
    final Criteria criteria = this.sessionFactory.getCurrentSession().createCriteria(this.mappedClass);
    criteria.add(Restrictions.ilike(NAME_PROP, taskNamePrefix, MatchMode.START));
    criteria.add(Restrictions.gt(START_TIME_PROP, Date.from(afterStartTime)));
    return criteria.list();
  }

}
