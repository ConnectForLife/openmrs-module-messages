/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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
import org.openmrs.module.messages.api.util.JsonUtil;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceGroupBuilder;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
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

    protected static final Long NEVER_REPEAT = 0L;
    protected static final int MAX_ATTEMPTS = 3;
    public static final String CHANNEL_TYPE_1_NAME = Constant.CHANNEL_TYPE_CALL;

    @Autowired
    @Qualifier("schedulerService")
    protected SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env

    @Autowired
    @Qualifier("messages.messagingService")
    protected MessagingService messagingService;

    @Autowired
    @Qualifier("messages.messagingGroupService")
    protected MessagingGroupService messagingGroupService;

    @Captor
    private ArgumentCaptor<TaskDefinition> taskCaptor;

    private final Gson gson = JsonUtil.getGson();

    protected ScheduledService deliveredScheduledService;
    protected ScheduledService pendingScheduledService;
    protected ScheduledService pendingScheduledServiceInAnotherChannel;
    protected ScheduledService failedScheduledService;

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

        getStrategy().execute(failedScheduledService.getGroup(), failedScheduledService.getChannelType());
    }

    @Test
    public void shouldRescheduleFailedDelivery() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS - 1);
        addDeliveryAttempts(pendingScheduledService, MAX_ATTEMPTS - 1);

        getStrategy().execute(failedScheduledService.getGroup(), failedScheduledService.getChannelType());

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

        verify(schedulerService, times(0)).scheduleTask(any());
    }

    @Test
    public void shouldNotRescheduleAfterExceedingMaxNumberOfAttempts() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS);
        addDeliveryAttempts(pendingScheduledService, MAX_ATTEMPTS);

        getStrategy().execute(failedScheduledService.getGroup(), failedScheduledService.getChannelType());

        verify(schedulerService, times(0)).scheduleTask(any());
    }

    @Test
    public void shouldNotRescheduleWhenAllServicesAreDelivered() throws Exception {
        ScheduledServiceGroup scheduledServiceGroup = messagingGroupService.saveOrUpdate(
                new ScheduledServiceGroupBuilder()
                        .withPatientId(DEFAULT_PATIENT_ID)
                        .withActorId(DEFAULT_PATIENT_ID)
                        .withScheduledServices(wrap(new ScheduledServiceBuilder()
                                .withChannelType(CHANNEL_TYPE_1_NAME)
                                .withStatus(ServiceStatus.DELIVERED)
                                .build()))
                        .build());

        getStrategy().execute(scheduledServiceGroup, CHANNEL_TYPE_1_NAME);
        verifyIfTaskIsNotCreated();
    }

    @Test
    public void shouldNotRescheduleWhenThereAreNoServicesToReschedule() throws Exception {
        ScheduledServiceGroup scheduledServiceGroup = messagingGroupService.saveOrUpdate(
                new ScheduledServiceGroupBuilder()
                        .withPatientId(DEFAULT_PATIENT_ID)
                        .withActorId(DEFAULT_PATIENT_ID)
                        .build());

        getStrategy().execute(scheduledServiceGroup, CHANNEL_TYPE_1_NAME);
        verifyIfTaskIsNotCreated();
    }

    protected abstract ReschedulingStrategy getStrategy();

    protected TaskDefinition getCreatedTask() throws SchedulerException {
        verify(schedulerService, times(2)).saveTaskDefinition(taskCaptor.capture());
        return taskCaptor.getValue();
    }

    protected void verifyIfTaskIsNotCreated() throws SchedulerException {
        verify(schedulerService, times(0)).scheduleTask(any());
    }

    protected ScheduledExecutionContext getExecutionContext(TaskDefinition task) {
        return gson.fromJson(
                task.getProperty(EXECUTION_CONTEXT),
                ScheduledExecutionContext.class);
    }

    protected void addDeliveryAttempts(ScheduledService failedScheduledService, int numberOfAttempts) {
        DeliveryAttemptBuilder deliveryAttemptBuilder = new DeliveryAttemptBuilder();
        List<DeliveryAttempt> deliveryAttempts = failedScheduledService.getDeliveryAttempts();
        for (int i = 0; i < numberOfAttempts; ++i) {
            deliveryAttempts.add(deliveryAttemptBuilder
                    .withScheduledService(failedScheduledService)
                    .buildAsNew());
        }
    }

    private <T> List<T> wrap(T toWrap) {
        ArrayList<T> list = new ArrayList<>();
        list.add(toWrap);
        return list;
    }
}
