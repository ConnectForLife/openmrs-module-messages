/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao;

import org.openmrs.scheduler.TaskDefinition;

import java.time.Instant;
import java.util.List;

/**
 * The ExtendedSchedulerDao class.
 * <p>
 * This DAO provides an extension to {@link org.openmrs.scheduler.db.SchedulerDAO} functions, including more advanced way
 * to read {@link org.openmrs.scheduler.TaskDefinition}s.
 * </p>
 */
public interface ExtendedSchedulerDao {
  /**
   * Gets all Tasks which ware not executed (the {@code lastExecutionTime} is null) and the task's class name is equal
   * to the {@code className}.
   *
   * @param className the task class to get tasks for, not null
   * @return the list of tasks, never null
   */
  List<TaskDefinition> getNotExecutedTasksByClassName(String className);

  /**
   * Gets all Tasks which names starts with {@code taskNamePrefix} and start time is after {@code afterStartTime}.
   *
   * @param taskNamePrefix the task name prefix, not null
   * @param afterStartTime the date time limit, not null
   * @return the list of tasks, never null
   */
  List<TaskDefinition> getTasksByPrefixAndAfterStartTime(String taskNamePrefix, Instant afterStartTime);
}
