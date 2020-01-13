/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition.EXECUTION_CONTEXT;
import static org.openmrs.module.messages.api.service.DatasetConstants.DELIVERED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.FAILED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.PENDING_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.PENDING_SCHEDULED_SERVICE_IN_ANOTHER_CHANNEL;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

import com.google.gson.Gson;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.MapperUtil;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@SuppressWarnings("VisibilityModifier")
public abstract class BaseReschedulingStrategyITTest extends ContextSensitiveTest {

    protected static final Long NEVER_REPEAT = 0L;
    protected static final int MAX_ATTEMPTS = 3;

    @Autowired
    @Qualifier("schedulerService")
    protected SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env

    @Autowired
    @Qualifier("messages.messagingService")
    protected MessagingService messagingService;

    @Captor
    private ArgumentCaptor<TaskDefinition> taskCaptor;

    private final Gson gson = MapperUtil.getGson();

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

    @Test
    public void shouldRescheduleFailedDelivery() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS - 1);

        getStrategy().execute(failedScheduledService);

        TaskDefinition task = getCreatedTask();
        assertNotNull(task);
        assertEquals(NEVER_REPEAT, task.getRepeatInterval());

        ScheduledExecutionContext executionContext = getExecutionContext(task);
        assertEquals(failedScheduledService.getGroup().getActor().getId().intValue(), executionContext.getActorId());
        assertThat(executionContext.getServiceIdsToExecute(), hasItem(failedScheduledService.getId()));
    }

    @Test
    public void shouldNotRescheduleNotFailedDelivery() throws Exception {
        getStrategy().execute(deliveredScheduledService);

        verify(schedulerService, times(0)).scheduleTask(any());
    }

    @Test
    public void shouldNotRescheduleAfterExceedingMaxNumberOfAttempts() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS);

        getStrategy().execute(failedScheduledService);

        verify(schedulerService, times(0)).scheduleTask(any());
    }

    protected abstract ReschedulingStrategy getStrategy();

    protected TaskDefinition getCreatedTask() throws SchedulerException {
        verify(schedulerService, times(1)).scheduleTask(taskCaptor.capture());
        return taskCaptor.getValue();
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
}