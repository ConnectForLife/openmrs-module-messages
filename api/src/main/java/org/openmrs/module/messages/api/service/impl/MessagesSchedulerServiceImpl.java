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
import org.openmrs.scheduler.SchedulerConstants;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.SchedulerUtil;
import org.openmrs.scheduler.Task;
import org.openmrs.scheduler.TaskDefinition;
import org.openmrs.scheduler.TaskFactory;
import org.openmrs.scheduler.timer.TimerSchedulerTask;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;

/**
 * Implements methods related to job scheduling
 */
public class MessagesSchedulerServiceImpl extends BaseOpenmrsDataService<ScheduledService>
        implements MessagesSchedulerService {

    private static final Log LOGGER = LogFactory.getLog(MessagesSchedulerServiceImpl.class);

    private static final boolean IS_DAEMON_THREAD_USED = true;

    private final Timer timer = new Timer(IS_DAEMON_THREAD_USED);

    private SchedulerService schedulerService;

    private DaemonToken daemonToken;

    @Override
    public void rescheduleOrCreateNewTask(JobDefinition jobDefinition, Long intervalInSecond) {
        TaskDefinition previousTask = schedulerService.getTaskByName(jobDefinition.getTaskName());
        TaskDefinition newTask = prepareTask(jobDefinition, previousTask, intervalInSecond);

        if (shouldBeExecuted(newTask, jobDefinition)) {
            newTask.setLastExecutionTime(DateUtil.toDate(DateUtil.now()));
            executeJob(jobDefinition);
        }
        if (previousTask == null) {
            scheduleTask(newTask);
        } else {
            try {
                schedulerService.shutdownTask(previousTask);
                schedulerService.deleteTask(previousTask.getId());
                scheduleTask(newTask);
            } catch (SchedulerException ex) {
                LOGGER.error(ex);
            }
        }
    }

    @Override
    public void createNewTask(JobDefinition jobDefinition, Instant startTime, JobRepeatInterval repeatInterval) {
        TaskDefinition newTask = prepareTask(jobDefinition, null, Date.from(startTime), repeatInterval.getSeconds());
        scheduleTask(newTask);
    }

    @Override
    public void createNewTask(JobDefinition jobDefinition, Date startTime, JobRepeatInterval repeatInterval) {
        this.createNewTask(jobDefinition, startTime.toInstant(), repeatInterval);
    }

    private void executeJob(JobDefinition jobDefinition) {
        Daemon.runInDaemonThread(jobDefinition::execute, daemonToken);
    }

    private void scheduleTask(TaskDefinition task) {
        try {
            schedulerService.saveTaskDefinition(task);
            customScheduleTask(task);
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
            return task
                    .getLastExecutionTime()
                    .toInstant()
                    .isBefore(DateUtil.now().minusSeconds(task.getRepeatInterval()).toInstant());
        }
    }

    private TaskDefinition prepareTask(JobDefinition jobDefinition, TaskDefinition previousTask, long repeatInterval) {
        if (previousTask == null) {
            return prepareTask(jobDefinition, null, DateUtil.toDate(DateUtil.now()), repeatInterval);
        } else {
            return prepareTask(jobDefinition, previousTask.getLastExecutionTime(), previousTask.getStartTime(),
                    repeatInterval);
        }
    }

    private TaskDefinition prepareTask(JobDefinition jobDefinition, Date lastExecutionTime, Date startTime,
                                       long repeatInterval) {
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

    /* Method copied and adjusted from OpenMRS SchedulerService
    This method uses one Timer instance for storing all tasks (instead of default OpenMRS implementation -
    one Timer per one task)
    Changed due to performance issues */
    private Task customScheduleTask(TaskDefinition taskDefinition) throws SchedulerException {
        Task clientTask = null;
        if (taskDefinition != null) {
            TimerSchedulerTask schedulerTask;
            try {
                // Create new task from task definition
                clientTask = TaskFactory.getInstance().createInstance(taskDefinition);
                // if we were unable to get a class, just quit
                if (clientTask != null) {
                    schedulerTask = new TimerSchedulerTask(clientTask);
                    taskDefinition.setTaskInstance(clientTask);
                    // Once this method is called, the timer is set to start at the given start time.
                    // NOTE:  We need to adjust the repeat interval as the JDK Timer expects time in milliseconds and
                    // we record by seconds.
                    long repeatInterval = 0;
                    if (taskDefinition.getRepeatInterval() != null) {
                        repeatInterval = taskDefinition.getRepeatInterval() * SchedulerConstants.SCHEDULER_MILLIS_PER_SECOND;
                    }
                    if (taskDefinition.getStartTime() != null) {
                        // Need to calculate the "next execution time" because the scheduled time is most likely in the past
                        // and the JDK timer will run the task X number of times from the start time until now to catch up.
                        Date nextTime = SchedulerUtil.getNextExecution(taskDefinition);
                        // Start task at fixed rate at given future date and repeat as directed
                        if (repeatInterval > 0) {
                            // Schedule the task to run at a fixed rate
                            getTimer().scheduleAtFixedRate(schedulerTask, nextTime, repeatInterval);
                        } else {
                            // Schedule the task to be non-repeating
                            getTimer().schedule(schedulerTask, nextTime);
                        }
                    } else if (repeatInterval > 0) {
                        // Start task on repeating schedule, delay for SCHEDULER_DEFAULT_DELAY seconds
                        getTimer().scheduleAtFixedRate(schedulerTask, SchedulerConstants.SCHEDULER_DEFAULT_DELAY,
                                repeatInterval);
                    } else {
                        // schedule for single execution, starting now
                        getTimer().schedule(schedulerTask, new Date());
                    }
                    // Update task that has been started
                    // Update the timer status in the database
                    taskDefinition.setStarted(true);
                    schedulerService.saveTaskDefinition(taskDefinition);
                }
            } catch (Exception e) {
                throw new SchedulerException("Failed to schedule task", e);
            }
        }
        return clientTask;
    }

    private Timer getTimer() {
        return timer;
    }
}
