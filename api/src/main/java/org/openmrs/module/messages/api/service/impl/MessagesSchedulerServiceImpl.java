/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;

import java.util.Date;

/**
 * Implements methods related to job scheduling
 */
public class MessagesSchedulerServiceImpl extends BaseOpenmrsDataService<ScheduledService>
        implements MessagesSchedulerService {

    private static final Log LOGGER = LogFactory.getLog(MessagesSchedulerServiceImpl.class);

    private SchedulerService schedulerService;

    private DaemonToken daemonToken;

    @Override
    public void rescheduleOrCreateNewTask(JobDefinition jobDefinition, Long intervalInSecond) {
        TaskDefinition previousTask = schedulerService.getTaskByName(jobDefinition.getTaskName());
        TaskDefinition newTask = prepareTask(jobDefinition, previousTask, intervalInSecond);

        if (shouldBeExecuted(newTask, jobDefinition)) {
            newTask.setLastExecutionTime(DateUtil.now());
            executeJob(jobDefinition);
        }
        if (previousTask == null) {
            scheduleTask(newTask);
        } else {
            shutdownTask(newTask.getName());
            scheduleTask(newTask);
        }
    }

    @Override
    public void createNewTask(JobDefinition jobDefinition, Date startTime,
                              JobRepeatInterval repeatInterval) {
        TaskDefinition previousTask = schedulerService.getTaskByName(jobDefinition.getTaskName());
        if (previousTask != null) {
            throw new MessagesRuntimeException(String.format("A task with name %s has been " +
                "already scheduled", jobDefinition.getTaskName()));
        }

        TaskDefinition newTask = prepareTask(jobDefinition, null, startTime, repeatInterval.getSeconds());
        scheduleTask(newTask);
    }

    private void executeJob(JobDefinition jobDefinition) {
        Daemon.runInDaemonThread(new Runnable() {
            @Override
            public void run() {
                jobDefinition.execute();
            }
        }, daemonToken);
    }

    private void scheduleTask(TaskDefinition task) {
        try {
            schedulerService.saveTaskDefinition(task);
            schedulerService.scheduleTask(task);
        } catch (SchedulerException ex) {
            throw new MessagesRuntimeException(ex);
        }
    }

    private boolean isPrimaryTaskCreation(TaskDefinition task) {
        return task.getLastExecutionTime() == null;
    }

    private boolean shouldBeExecuted(TaskDefinition task, JobDefinition jobDefinition) {
        if (isPrimaryTaskCreation(task)) {
            return jobDefinition.shouldStartAtFirstCreation();
        } else {
            return task.getLastExecutionTime().before(DateUtil.getDateSecondsAgo(task.getRepeatInterval()));
        }
    }

    private TaskDefinition prepareTask(JobDefinition jobDefinition, TaskDefinition previousTask, long repeatInterval) {
        if (previousTask == null) {
            return prepareTask(jobDefinition, null, DateUtil.now(), repeatInterval);
        } else {
            return prepareTask(jobDefinition, previousTask.getLastExecutionTime(),
                    previousTask.getStartTime(), repeatInterval);
        }
    }

    private TaskDefinition prepareTask(JobDefinition jobDefinition, Date lastExecutionTime,
                                       Date startTime, long repeatInterval) {
        TaskDefinition task = new TaskDefinition();
        task.setName(jobDefinition.getTaskName());
        task.setLastExecutionTime(lastExecutionTime);
        task.setRepeatInterval(repeatInterval);
        task.setTaskClass(jobDefinition.getTaskClass().getName());
        task.setStartTime(startTime);
        task.setStartOnStartup(false);
        task.setProperties(jobDefinition.getProperties());
        return task;
    }

    public void setSchedulerService(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public void setDaemonToken(DaemonToken daemonToken) {
        this.daemonToken = daemonToken;
    }

    private void shutdownTask(String taskName) {
        try {
            TaskDefinition taskDefinition = schedulerService.getTaskByName(taskName);
            if (taskDefinition != null) {
                schedulerService.shutdownTask(taskDefinition);
                schedulerService.deleteTask(taskDefinition.getId());
            }
        } catch (SchedulerException ex) {
            LOGGER.error(ex);
        }
    }
}
