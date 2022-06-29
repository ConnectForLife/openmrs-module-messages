/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.scheduler.TaskDefinition;

import java.time.Instant;
import java.util.Date;

/**
 * Provides methods related to job scheduling.
 * <p>
 * <b>Attention:</b> All tasks created by this service are scheduled in an internal singleton {@link java.util.Timer} and
 * all of these tasks are unscheduled automatically at system shutdown.
 * </p>
 */
public interface MessagesSchedulerService extends OpenmrsService {

    /**
     * Reschedules a task related to {@code jobDefinition} in custom CfL Timer. If task already exists, then properties
     * including start date and interval carry over to the newly scheduled task.
     * <p>
     * This method always creates a new instance of TaskDefinition entity.
     * </p>
     *
     * @param jobDefinition    object containing the important data about job
     * @param intervalInSecond interval between job executions (represented in seconds)
     */
    void rescheduleOrCreateNewTask(JobDefinition jobDefinition, Long intervalInSecond);

    /**
     * Creates a new scheduled task
     *
     * @param jobDefinition  object containing the important data about job
     * @param startTime      date when the scheduled task will start from
     * @param repeatInterval interval between job executions
     */
    void createNewTask(JobDefinition jobDefinition, Instant startTime, JobRepeatInterval repeatInterval);

    /**
     * Creates a new scheduled task
     *
     * @param jobDefinition  object containing the important data about job
     * @param startTime      date when the scheduled task will start from
     * @param repeatInterval interval between job executions
     * @deprecated Use {@link #createNewTask(JobDefinition, Instant, JobRepeatInterval)} with Java Time API instead of Date
     */
    @Deprecated
    void createNewTask(JobDefinition jobDefinition, Date startTime, JobRepeatInterval repeatInterval);

    /**
     * Schedule all {@code taskDefinitions} in the internal singleton Timer. If any of the {@code taskDefinitions} is
     * already scheduled, the existing Timer task will be canceled and a new one will be added.
     *
     * @param taskDefinitions the Task Definitions to schedule, not null
     */
    void scheduleAll(Iterable<TaskDefinition> taskDefinitions);

    /**
     * Shutdown any related Timer task managed by this service for {@code taskDefinition}. If there is no related Timer
     * task, the method does nothing. <b>This method does not shutdown tasks managed by regular OpenMRS scheduler.</b>
     *
     * @param taskDefinition the Task definition to shutdown the Timer task for, not null
     */
    void shutdownTask(TaskDefinition taskDefinition);

    /**
     * Setting up a token for daemon execution
     *
     * @param daemonToken daemon token
     */
    void setDaemonToken(DaemonToken daemonToken);
}
