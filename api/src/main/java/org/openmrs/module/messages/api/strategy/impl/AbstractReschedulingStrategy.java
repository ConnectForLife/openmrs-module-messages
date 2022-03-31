/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class AbstractReschedulingStrategy implements ReschedulingStrategy {

  private final Log logger = LogFactory.getLog(getClass());
  private static final String SHORT_DATE_FORMAT = "yyyyMMddHHmmz";

  private ConfigService configService;
  private MessagesDeliveryService deliveryService;

  @Override
  public void execute(ScheduledServiceGroup group) {
    List<ScheduledService> servicesToExecute = extractServiceListToExecute(group);
    if (servicesToExecute.isEmpty()) {
      logger.debug(
          String.format(
              "The group %s have been fully delivered, so the rescheduling logic will not be run",
              group.getId()));
      return;
    }

    ScheduledService service = validateAndGetFirstServiceToRetry(servicesToExecute, group);
    if (!shouldReschedule(service)) {
      return;
    }

    SchedulerService schedulerService = Context.getSchedulerService();
    TaskDefinition task = schedulerService.getTaskByName(getTaskNameByGroup(group));
    if (service.getStatus() == ServiceStatus.DELIVERED && task != null) {
      task.setStarted(false);
      schedulerService.deleteTask(task.getId());
      return;
    }

    deliveryService.scheduleDelivery(
        new ScheduledExecutionContext(
            servicesToExecute,
            group.getChannelType(),
            getRescheduleDate().toInstant(),
            service.getPatientTemplate().getActor(),
            service.getPatientTemplate().getPatient().getPatientId(),
            service.getPatientTemplate().getActorTypeAsString(),
            service.getGroup().getId()));
  }

  public void setConfigService(ConfigService configService) {
    this.configService = configService;
  }

  public void setDeliveryService(MessagesDeliveryService deliveryService) {
    this.deliveryService = deliveryService;
  }

  protected boolean shouldReschedule(ScheduledService service) {
    boolean shouldReschedule = true;
    if (service.getStatus() != ServiceStatus.FAILED) {
      logger.debug(
          String.format(
              "ScheduledService %d will be not rescheduled due to no failed status: %s",
              service.getId(), service.getStatus()));
      shouldReschedule = false;
    } else if (service.getNumberOfAttempts() >= getMaxNumberOfAttempts()) {
      logger.info(
          String.format(
              "ScheduledService %d will be not rescheduled due to exceeding the max number of attempts: %d/%d",
              service.getId(), service.getNumberOfAttempts(), getMaxNumberOfAttempts()));
      shouldReschedule = false;
    }
    return shouldReschedule;
  }

  protected ScheduledService validateAndGetFirstServiceToRetry(
      List<ScheduledService> servicesToExecute, ScheduledServiceGroup group) {
    // There are additional validation, which make sure that data is DB is valid.
    if (servicesToExecute.isEmpty()) {
      throw new MessagesRuntimeException(
          String.format(
              "Rescheduling will not be conducted for the group %s, because of lack of services to retry",
              group.getId()));
    }
    int numberOfAttempts = servicesToExecute.get(0).getNumberOfAttempts();
    for (ScheduledService ss : servicesToExecute) {
      if (ss.getNumberOfAttempts() != numberOfAttempts) {
        throw new MessagesRuntimeException(
            String.format(
                "Group rescheduling assumes that all entries to retry"
                    + " have the same number of attempts, but they not in the group %s",
                group.getId()));
      }
    }
    return servicesToExecute.get(0);
  }

  protected Log getLogger() {
    return logger;
  }

  protected abstract List<ScheduledService> extractServiceListToExecute(
      ScheduledServiceGroup group);

  private ZonedDateTime getRescheduleDate() {
    return DateUtil.now().plusSeconds(getTimeIntervalToNextReschedule());
  }

  private int getMaxNumberOfAttempts() {
    return configService.getMaxNumberOfAttempts();
  }

  private int getTimeIntervalToNextReschedule() {
    return configService.getTimeIntervalToNextReschedule();
  }

  private String getTaskNameByGroup(ScheduledServiceGroup group) {
    return String.format(
            "a:%s:%dp:%dd:%s",
            group.getChannelType(),
            group.getActor().getId(),
            group.getPatient().getPatientId(),
            DateUtil.formatDateTime(
                    getRescheduleDate().toInstant().atZone(DateUtil.getDefaultSystemTimeZone()),
                    SHORT_DATE_FORMAT));
  }
}
