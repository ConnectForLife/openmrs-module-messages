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
import org.openmrs.api.context.Context;
import org.openmrs.api.context.Daemon;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.constants.ConfigConstants;
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
import org.openmrs.util.PrivilegeConstants;

import java.time.Instant;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.WeakHashMap;

import static java.util.Collections.synchronizedMap;

/** Implements methods related to job scheduling */
public class MessagesSchedulerServiceImpl extends BaseOpenmrsDataService<ScheduledService>
    implements MessagesSchedulerService {

  private static final Log LOGGER = LogFactory.getLog(MessagesSchedulerServiceImpl.class);

  private static final boolean IS_DAEMON_THREAD_USED = true;

  private final Timer timer = new Timer(IS_DAEMON_THREAD_USED);
  private final Map<Integer, TimerSchedulerTask> scheduledTasks =
      synchronizedMap(new WeakHashMap<>());

  private SchedulerService schedulerService;

  private DaemonToken daemonToken;

  @Override
  public void rescheduleOrCreateNewTask(JobDefinition jobDefinition, Long intervalInSecond) {
    final TaskDefinition previousTask = schedulerService.getTaskByName(jobDefinition.getTaskName());
    final TaskDefinition rescheduledTask;

    if (previousTask != null) {
      shutdownTaskInOpenMRSScheduler(previousTask);
      shutdownTask(previousTask);
      rescheduledTask = prepareTaskFromPrevious(jobDefinition, previousTask, intervalInSecond);
      schedulerService.deleteTask(previousTask.getId());
    } else {
      rescheduledTask = prepareNewTask(jobDefinition, intervalInSecond);
    }

    if (shouldBeExecuted(rescheduledTask, jobDefinition)) {
      rescheduledTask.setLastExecutionTime(DateUtil.toDate(DateUtil.now()));
      executeJob(jobDefinition);
    }

    scheduleTask(rescheduledTask);
  }

  @Override
  public void createNewTask(
      JobDefinition jobDefinition, Instant startTime, JobRepeatInterval repeatInterval) {
    final TaskDefinition newTask =
        prepareTask(jobDefinition, null, Date.from(startTime), repeatInterval.getSeconds());
    scheduleTask(newTask);
  }

  @Override
  public void createNewTask(
      JobDefinition jobDefinition, Date startTime, JobRepeatInterval repeatInterval) {
    this.createNewTask(jobDefinition, startTime.toInstant(), repeatInterval);
  }

  @Override
  public void scheduleAll(Iterable<TaskDefinition> taskDefinitions) {
    taskDefinitions.forEach(this::scheduleTask);
  }

  @Override
  public void shutdownTask(TaskDefinition taskDefinition) {
    final TimerSchedulerTask taskToShutdown = scheduledTasks.remove(taskDefinition.getId());
    if (taskToShutdown != null) {
      taskToShutdown.shutdown();
      stopTaskDefinition(taskDefinition);
    }
  }

  @Override
  public void onShutdown() {
    LOGGER.info("Shutting down MessagesSchedulerService...");

    try {
      // Workaround for OpenMRS running shutdown without any user
      Context.addProxyPrivilege(PrivilegeConstants.MANAGE_SCHEDULER);

      shutdownAllTasks();
      cancelTimer();
    } finally {
      Context.removeProxyPrivilege(PrivilegeConstants.MANAGE_SCHEDULER);
    }

    LOGGER.info("MessagesSchedulerService shutdown.");
  }

  public void setSchedulerService(SchedulerService schedulerService) {
    this.schedulerService = schedulerService;
  }

  public void setDaemonToken(DaemonToken daemonToken) {
    this.daemonToken = daemonToken;
  }

  private TaskDefinition prepareNewTask(JobDefinition jobDefinition, long repeatInterval) {
    return prepareTask(jobDefinition, null, getMessageDeliveryJobStartDate(), repeatInterval);
  }

  private TaskDefinition prepareTaskFromPrevious(
      JobDefinition jobDefinition, TaskDefinition previousTask, long repeatInterval) {
    return prepareTask(
        jobDefinition,
        previousTask.getLastExecutionTime(),
        getMessageDeliveryJobStartDate(),
        repeatInterval);
  }

  private TaskDefinition prepareTask(
      JobDefinition jobDefinition, Date lastExecutionTime, Date startTime, long repeatInterval) {
    final TaskDefinition task = new TaskDefinition();
    task.setName(jobDefinition.getTaskName());
    task.setLastExecutionTime(lastExecutionTime);
    task.setRepeatInterval(repeatInterval);
    task.setTaskClass(jobDefinition.getTaskClass().getName());
    task.setStartTime(startTime);
    task.setStartOnStartup(false);
    task.setProperties(jobDefinition.getProperties());
    return task;
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

  private boolean shouldBeExecuted(TaskDefinition task, JobDefinition jobDefinition) {
    if (isPrimaryTaskCreation(task)) {
      return jobDefinition.shouldStartAtFirstCreation();
    } else {
      return task.getLastExecutionTime()
          .toInstant()
          .isBefore(DateUtil.now().minusSeconds(task.getRepeatInterval()).toInstant());
    }
  }

  private boolean isPrimaryTaskCreation(TaskDefinition task) {
    return task.getLastExecutionTime() == null;
  }

  /* Method copied and adjusted from OpenMRS SchedulerService
  This method uses one Timer instance for storing all tasks (instead of default OpenMRS implementation -
  one Timer per one task)
  Changed due to performance issues */
  private void customScheduleTask(TaskDefinition taskDefinition) throws SchedulerException {
    if (taskDefinition == null) {
      return;
    }

    try {
      // Shutdown any previous execution of the task
      shutdownTask(taskDefinition);

      // Create new task from task definition
      final Task clientTask = TaskFactory.getInstance().createInstance(taskDefinition);
      // if we were unable to get a class, just quit
      if (clientTask == null) {
        return;
      }

      final TimerSchedulerTask schedulerTask = new TimerSchedulerTask(clientTask);
      taskDefinition.setTaskInstance(clientTask);
      // Once this method is called, the timer is set to start at the given start time.
      // NOTE:  We need to adjust the repeat interval as the JDK Timer expects time in milliseconds
      // and
      // we record by seconds.
      long repeatInterval = 0;

      if (taskDefinition.getRepeatInterval() != null) {
        repeatInterval =
            taskDefinition.getRepeatInterval() * SchedulerConstants.SCHEDULER_MILLIS_PER_SECOND;
      }

      // Need to calculate the "next execution time" because the scheduled time is most likely in
      // the past
      // and the JDK timer will run the task X number of times from the start time until now to
      // catch up.
      final Date nextTime = SchedulerUtil.getNextExecution(taskDefinition);

      if (repeatInterval > 0 && nextTime != null) {
        // Start task at fixed rate at given future date and repeat as directed
        timer.scheduleAtFixedRate(schedulerTask, nextTime, repeatInterval);
      } else if (repeatInterval > 0) {
        // Schedule the task to run at a fixed rate
        timer.scheduleAtFixedRate(
            schedulerTask, SchedulerConstants.SCHEDULER_DEFAULT_DELAY, repeatInterval);
      } else {
        // Schedule the task to be non-repeating
        timer.schedule(schedulerTask, nextTime);
      }

      // Update task that has been started
      // Update the timer status in the database
      startTaskDefinition(taskDefinition);
      scheduledTasks.put(taskDefinition.getId(), schedulerTask);
    } catch (Exception e) {
      throw new SchedulerException("Failed to schedule task", e);
    }
  }

  private void shutdownTaskInOpenMRSScheduler(TaskDefinition taskDefinition) {
    try {
      schedulerService.shutdownTask(taskDefinition);
    } catch (SchedulerException e) {
      LOGGER.error(
          "Failed to shutdown following task in OpenMRS scheduler: " + taskDefinition.getName(), e);
    }
  }

  private void startTaskDefinition(TaskDefinition taskDefinition) {
    taskDefinition.setStarted(Boolean.TRUE);
    schedulerService.saveTaskDefinition(taskDefinition);
  }

  private void stopTaskDefinition(TaskDefinition taskDefinition) {
    taskDefinition.setStarted(Boolean.FALSE);
    schedulerService.saveTaskDefinition(taskDefinition);
  }

  private void shutdownAllTasks() {
    final Iterator<Map.Entry<Integer, TimerSchedulerTask>> scheduledTaskEntriesIterator =
        scheduledTasks.entrySet().iterator();

    while (scheduledTaskEntriesIterator.hasNext()) {
      final Map.Entry<Integer, TimerSchedulerTask> scheduledTaskEntry =
          scheduledTaskEntriesIterator.next();

      try {
        scheduledTaskEntry.getValue().shutdown();
        stopTaskDefinition(schedulerService.getTask(scheduledTaskEntry.getKey()));
      } catch (Exception e) {
        LOGGER.error(
            "Failed to shutdown task for TaskDefinition ID: " + scheduledTaskEntry.getKey(), e);
      } finally {
        scheduledTaskEntriesIterator.remove();
      }
    }
  }

  private void cancelTimer() {
    timer.cancel();
  }

  private Date getMessageDeliveryJobStartDate() {
    String messageJobStartTime =
        Context.getAdministrationService()
            .getGlobalProperty(
                ConfigConstants.MESSAGE_DELIVERY_JOB_START_TIME_GP_KEY,
                ConfigConstants.MESSAGE_DELIVERY_JOB_START_TIME_DEFAULT_VALUE);
    return DateUtil.getDateWithTimeOfDay(
        DateUtil.toDate(DateUtil.now()),
        messageJobStartTime,
        DateUtil.convertZoneIdToTimeZone(DateUtil.getDefaultSystemTimeZone()));
  }
}
