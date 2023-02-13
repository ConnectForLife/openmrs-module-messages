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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.dao.ExtendedSchedulerDao;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Implements methods related to the messages delivery management
 */
@Transactional
public class MessagesDeliveryServiceImpl extends BaseOpenmrsService implements MessagesDeliveryService {

  private static final Log LOGGER = LogFactory.getLog(MessagesDeliveryServiceImpl.class);

  private MessagesSchedulerService schedulerService;
  private ExtendedSchedulerDao extendedSchedulerDao;

  @Override
  public void scheduleDelivery(ScheduledExecutionContext executionContext) {
    final JobDefinition definition = new ServiceGroupDeliveryJobDefinition(executionContext);
    schedulerService.createNewTask(definition, executionContext.getExecutionDate(), JobRepeatInterval.NEVER);
  }

  @Override
  @Transactional(readOnly = true)
  public List<TaskDefinition> getTasksByPrefixAndAfterStartTime(String taskNamePrefix, Instant afterStartTime) {
    return extendedSchedulerDao.getTasksByPrefixAndAfterStartTime(taskNamePrefix, afterStartTime);
  }

  /**
   * Warning: During normal OpenMRS startup this method is called twice! Therefore we use 'synchronized' to ensure
   * only one thread enters this method at the time.
   */
  @Override
  public synchronized void onStartup() {
    LOGGER.info("Scheduling Message Delivery tasks created during previous runtime...");

    final List<TaskDefinition> tasksToSchedule =
        extendedSchedulerDao.getNotExecutedTasksByClassName(ServiceGroupDeliveryJobDefinition.class.getName());
    schedulerService.scheduleAll(tasksToSchedule);

    LOGGER.info("Scheduling Message Delivery tasks created during previous runtime complete.");
  }

  public void setSchedulerService(MessagesSchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  public void setExtendedSchedulerDao(ExtendedSchedulerDao extendedSchedulerDao) {
    this.extendedSchedulerDao = extendedSchedulerDao;
  }
}
