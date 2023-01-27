/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy;

import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Matchers;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.ScheduledExecutionContextUtil;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceGroupBuilder;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition.EXECUTION_CONTEXT;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DELIVERED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.FAILED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.PENDING_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.PENDING_SCHEDULED_SERVICE_IN_ANOTHER_CHANNEL;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

public abstract class BaseReschedulingStrategyITTest extends ContextSensitiveTest {

  public static final String CHANNEL_TYPE_1_NAME = Constant.CHANNEL_TYPE_CALL;
  protected static final Long NEVER_REPEAT = 0L;
  protected static final int MAX_ATTEMPTS = 3;
  @Autowired
  @Qualifier("schedulerService")
  protected SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env
  @Autowired
  @Qualifier("messages.messagingService")
  protected MessagingService messagingService;
  @Autowired
  @Qualifier("messages.messagingGroupService")
  protected MessagingGroupService messagingGroupService;
  protected ScheduledService deliveredScheduledService;
  protected ScheduledService pendingScheduledService;
  protected ScheduledService pendingScheduledServiceInAnotherChannel;
  protected ScheduledService failedScheduledService;
  @Captor
  private ArgumentCaptor<TaskDefinition> taskCaptor;
  @Autowired
  private SessionFactory sessionFactory;

  @Before
  public void setUp() throws Exception {
    executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml");
    executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
    executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");

    deliveredScheduledService = messagingService.getById(DELIVERED_SCHEDULED_SERVICE);
    pendingScheduledService = messagingService.getById(PENDING_SCHEDULED_SERVICE);
    pendingScheduledServiceInAnotherChannel = messagingService.getById(PENDING_SCHEDULED_SERVICE_IN_ANOTHER_CHANNEL);
    failedScheduledService = messagingService.getById(FAILED_SCHEDULED_SERVICE);

    reset(schedulerService);
  }

  @Test(expected = MessagesRuntimeException.class)
  public void shouldThrowExceptionIfAttemptNumbersForNotDeliveredServicesAreNotEqual() throws Exception {
    addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS - 1);
    addDeliveryAttempts(pendingScheduledService, MAX_ATTEMPTS - 2);
    addDeliveryAttempts(deliveredScheduledService, MAX_ATTEMPTS - 1);

    getStrategy().execute(failedScheduledService.getGroup());
  }

  @Test
  public void shouldRescheduleFailedDelivery() throws Exception {
    addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS - 1);
    addDeliveryAttempts(pendingScheduledService, MAX_ATTEMPTS - 1);

    getStrategy().execute(failedScheduledService.getGroup());

    TaskDefinition task = getCreatedTask();
    assertNotNull(task);
    assertEquals(NEVER_REPEAT, task.getRepeatInterval());

    ScheduledExecutionContext executionContext = getExecutionContext(task);
    assertEquals(failedScheduledService.getGroup().getActor().getId().intValue(), executionContext.getActorId());
    assertThat(executionContext.getServiceIdsToExecute(), hasItem(failedScheduledService.getId()));
  }

  @Test
  public void shouldNotRescheduleNotFailedDelivery() throws Exception {
    failedScheduledService.setStatus(ServiceStatus.DELIVERED);
    failedScheduledService = messagingService.saveOrUpdate(failedScheduledService);
    pendingScheduledService.setStatus(ServiceStatus.DELIVERED);
    pendingScheduledService = messagingService.saveOrUpdate(pendingScheduledService);

    verify(schedulerService, times(0)).scheduleTask(Matchers.<TaskDefinition>any());
  }

  @Test
  public void shouldNotRescheduleAfterExceedingMaxNumberOfAttempts() throws Exception {
    addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS);
    addDeliveryAttempts(pendingScheduledService, MAX_ATTEMPTS);

    getStrategy().execute(failedScheduledService.getGroup());

    verify(schedulerService, times(0)).scheduleTask(Matchers.<TaskDefinition>any());
  }

  @Test
  public void shouldNotRescheduleWhenAllServicesAreDelivered() throws Exception {
    ScheduledServiceGroup scheduledServiceGroup = messagingGroupService.saveOrUpdate(new ScheduledServiceGroupBuilder()
        .withPatientId(DEFAULT_PATIENT_ID)
        .withActorId(DEFAULT_PATIENT_ID)
        .withChannelType(CHANNEL_TYPE_1_NAME)
        .withScheduledServices(wrap(new ScheduledServiceBuilder().withStatus(ServiceStatus.DELIVERED).build()))
        .build());

    getStrategy().execute(scheduledServiceGroup);
    verifyIfTaskIsNotCreated();
  }

  @Test
  public void shouldNotRescheduleWhenThereAreNoServicesToReschedule() throws Exception {
    ScheduledServiceGroup scheduledServiceGroup = messagingGroupService.saveOrUpdate(new ScheduledServiceGroupBuilder()
        .withPatientId(DEFAULT_PATIENT_ID)
        .withActorId(DEFAULT_PATIENT_ID)
        .withChannelType(CHANNEL_TYPE_1_NAME)
        .build());

    getStrategy().execute(scheduledServiceGroup);
    verifyIfTaskIsNotCreated();
  }

  protected abstract ReschedulingStrategy getStrategy();

  protected TaskDefinition getCreatedTask() throws SchedulerException {
    verify(schedulerService, atLeastOnce()).saveTaskDefinition(taskCaptor.capture());
    return taskCaptor.getValue();
  }

  protected void verifyIfTaskIsNotCreated() throws SchedulerException {
    verify(schedulerService, never()).scheduleTask(Matchers.<TaskDefinition>any());
  }

  protected ScheduledExecutionContext getExecutionContext(TaskDefinition task) {
    return ScheduledExecutionContextUtil.fromJson(task.getProperty(EXECUTION_CONTEXT));
  }

  protected void addDeliveryAttempts(ScheduledService scheduledService, int numberOfAttempts) {
    DeliveryAttemptBuilder deliveryAttemptBuilder = new DeliveryAttemptBuilder();
    List<DeliveryAttempt> deliveryAttempts = scheduledService.getDeliveryAttempts();
    for (int i = 0; i < numberOfAttempts; ++i) {
      final DeliveryAttempt deliveryAttempt =
          deliveryAttemptBuilder.withScheduledService(scheduledService).buildAsNew();
      sessionFactory.getCurrentSession().saveOrUpdate(deliveryAttempt);
    }
  }

  private <T> List<T> wrap(T toWrap) {
    ArrayList<T> list = new ArrayList<T>();
    list.add(toWrap);
    return list;
  }
}
