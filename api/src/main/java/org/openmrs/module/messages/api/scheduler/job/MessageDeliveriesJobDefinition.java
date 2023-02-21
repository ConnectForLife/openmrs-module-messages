/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.scheduler.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResultGroupHelper;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.StopwatchUtil;
import org.openmrs.scheduler.SchedulerUtil;

import java.text.MessageFormat;
import java.time.ZonedDateTime;
import java.util.List;

import static org.openmrs.module.messages.api.constants.MessagesConstants.PERFORMANCE_LOGGER;

public class MessageDeliveriesJobDefinition extends JobDefinition {

  private static final Log LOGGER = LogFactory.getLog(MessageDeliveriesJobDefinition.class);
  private static final String TASK_NAME = "Message Deliveries Job Task";

  @Override
  public void execute() {
    LOGGER.info(getTaskName() + " started");
    final StopwatchUtil executeStopwatch = new StopwatchUtil();
    final ZonedDateTime nextExecution =
        ZonedDateTime.ofInstant(SchedulerUtil.getNextExecution(getTaskDefinition()).toInstant(),
            DateUtil.getDefaultSystemTimeZone());

    final List<ServiceResultList> results = getMessagingService().retrieveAllServiceExecutions(
        nextExecution.minusSeconds(getTaskDefinition().getRepeatInterval()), nextExecution);
    logNumberOfResults(results);

    PERFORMANCE_LOGGER.info(MessageFormat.format("getMessagingService.retrieveAllServiceExecutions took {0}ms",
        executeStopwatch.restart().toMillis()));

    List<GroupedServiceResultList> groupedResults =
        ServiceResultGroupHelper.groupByChannelTypePatientActorExecutionDate(results, true);
    LOGGER.debug(String.format("Converted to %d groups to execute", groupedResults.size()));

    for (GroupedServiceResultList groupedResult : groupedResults) {
      try {
        scheduleTaskForActivePerson(groupedResult);
      } catch (Exception e) {
        LOGGER.error(
            String.format("The error occurred during scheduling group for: %s. %s", groupedResult.getKey().toString(),
                e.getMessage()));
        LOGGER.debug(e.getMessage(), e);
      } finally {
        PERFORMANCE_LOGGER.info(
            MessageFormat.format("MessageDeliveriesJobDefinition.scheduleTaskForActivePerson for patient ID:{0} took {1}ms",
                groupedResult.getKey().getPatientId(), executeStopwatch.lap().toMillis()));
      }
    }

    PERFORMANCE_LOGGER.info(MessageFormat.format("All MessageDeliveriesJobDefinition.scheduleTaskForActivePerson took {0}ms",
        executeStopwatch.stop().toMillis()));
  }

  @Override
  public String getTaskName() {
    return TASK_NAME;
  }

  @Override
  public boolean shouldStartAtFirstCreation() {
    return true;
  }

  @Override
  public Class getTaskClass() {
    return MessageDeliveriesJobDefinition.class;
  }

  private void scheduleTaskForActivePerson(GroupedServiceResultList groupedResult) {
    final Person person = getPersonService().getPerson(groupedResult.getKey().getActorId());
    final Person patient = getPersonService().getPerson(groupedResult.getKey().getPatientId());

    if (shouldGroupBeCreated(patient, person, groupedResult)) {
      final ScheduledServiceGroup group = convertAndSave(groupedResult);

      getDeliveryService().scheduleDelivery(
          new ScheduledExecutionContext(group.getScheduledServices(), group.getChannelType(),
              groupedResult.getKey().getDate().toInstant(), group.getActor(), group.getPatient().getPatientId(),
              groupedResult.getKey().getActorType(), group.getId()));
    }
  }

  private boolean shouldGroupBeCreated(Person patient, Person person, GroupedServiceResultList groupedResult) {
    return isGroupNotExist(patient.getId(), person.getId(), groupedResult.getKey().getDate(),
        groupedResult.getKey().getChannelType()) && isActive(person) && isActive(patient);
  }

  private ScheduledServiceGroup convertAndSave(GroupedServiceResultList groupedResult) {
    ScheduledServiceGroup group = getGroupMapper().fromDto(groupedResult);
    return getGroupService().saveGroup(group);
  }

  private boolean isActive(Person person) {
    boolean isActive = PersonStatus.isActive(person);
    if (!isActive && LOGGER.isDebugEnabled()) {
      LOGGER.debug(
          String.format("Status of a person with id=%d is not active, so no service execution events will be scheduled",
              person.getId()));
    }
    return isActive;
  }

  private boolean isGroupNotExist(int patientId, int actorId, ZonedDateTime executionDate, String channelType) {
    boolean exist = getGroupService().isGroupExists(patientId, actorId, executionDate.toInstant(), channelType);
    if (exist) {
      LOGGER.warn(
          String.format("Messaging group for patient=%d, actor=%d and executionDate=%s has been already created", patientId,
              actorId, executionDate));
    }
    return !exist;
  }

  private MessagingService getMessagingService() {
    return Context.getRegisteredComponent(MessagesConstants.MESSAGING_SERVICE, MessagingService.class);
  }

  private MessagesDeliveryService getDeliveryService() {
    return Context.getRegisteredComponent(MessagesConstants.DELIVERY_SERVICE, MessagesDeliveryService.class);
  }

  private ScheduledGroupMapper getGroupMapper() {
    return Context.getRegisteredComponent(MessagesConstants.SCHEDULED_GROUP_MAPPER, ScheduledGroupMapper.class);
  }

  private MessagingGroupService getGroupService() {
    return Context.getRegisteredComponent(MessagesConstants.MESSAGING_GROUP_SERVICE, MessagingGroupService.class);
  }

  private PersonService getPersonService() {
    return Context.getRegisteredComponent(MessagesConstants.PERSON_SERVICE, PersonService.class);
  }

  private void logNumberOfResults(List<ServiceResultList> results) {
    if (LOGGER.isDebugEnabled()) {
      int numberOfServiceResults = 0;
      for (ServiceResultList srl : results) {
        if (srl.getResults() != null) {
          numberOfServiceResults += srl.getResults().size();
        }
      }
      LOGGER.debug(String.format("Collected %d service results from SQL", numberOfServiceResults));
    }
  }
}
