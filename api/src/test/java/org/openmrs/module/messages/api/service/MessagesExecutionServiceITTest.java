/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.JsonUtil;
import org.openmrs.module.messages.api.util.ScheduledExecutionContextUtil;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition.EXECUTION_CONTEXT;
import static org.openmrs.module.messages.api.service.DatasetConstants.DELIVERED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.FAILED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.PENDING_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.PENDING_SCHEDULED_SERVICE_IN_ANOTHER_CHANNEL;
import static org.openmrs.module.messages.api.service.DatasetConstants.SCHEDULED_SERVICE_GROUP;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

public class MessagesExecutionServiceITTest extends ContextSensitiveTest {

    private static final String CHANNEL_TYPE_1_NAME = Constant.CHANNEL_TYPE_CALL;
    private static final String EXECUTION_ID = "executionId123";
    private final Gson gson = JsonUtil.getGson();
    @Autowired
    @Qualifier("schedulerService")
    private SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env
    @Autowired
    @Qualifier("messages.messagingGroupService")
    private MessagingGroupService messagingGroupService;
    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;
    @Autowired
    @Qualifier("messages.messagesExecutionService")
    private MessagesExecutionService executionService;
    @Captor
    private ArgumentCaptor<TaskDefinition> taskCaptor;
    private ScheduledServiceGroup scheduledServiceGroup;

    private ScheduledService deliveredScheduledService;
    private ScheduledService pendingScheduledService;
    private ScheduledService pendingScheduledServiceInAnotherChannel;
    private ScheduledService failedScheduledService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml");
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");

        scheduledServiceGroup = messagingGroupService.getById(SCHEDULED_SERVICE_GROUP);

        deliveredScheduledService = messagingService.getById(DELIVERED_SCHEDULED_SERVICE);
        pendingScheduledService = messagingService.getById(PENDING_SCHEDULED_SERVICE);
        pendingScheduledServiceInAnotherChannel = messagingService.getById(PENDING_SCHEDULED_SERVICE_IN_ANOTHER_CHANNEL);
        failedScheduledService = messagingService.getById(FAILED_SCHEDULED_SERVICE);

        reset(schedulerService);
    }

    @Test
    public void executionCompletedShouldRegisterStatusForNotDeliveredServices() {
        final int previousAttemptsNumForDeliveredService = deliveredScheduledService.getNumberOfAttempts();
        final int previousAttemptsNumForFailedService = failedScheduledService.getNumberOfAttempts();
        final int previousAttemptsNumForPendingService = pendingScheduledService.getNumberOfAttempts();

        executionService.executionCompleted(SCHEDULED_SERVICE_GROUP, EXECUTION_ID, CHANNEL_TYPE_1_NAME);

        deliveredScheduledService = messagingService.getById(DELIVERED_SCHEDULED_SERVICE);
        failedScheduledService = messagingService.getById(FAILED_SCHEDULED_SERVICE);
        pendingScheduledService = messagingService.getById(PENDING_SCHEDULED_SERVICE);

        assertThat(deliveredScheduledService.getNumberOfAttempts(), is(previousAttemptsNumForDeliveredService));
        assertThat(failedScheduledService.getNumberOfAttempts(), is(previousAttemptsNumForFailedService + 1));
        assertThat(pendingScheduledService.getNumberOfAttempts(), is(previousAttemptsNumForPendingService + 1));
    }

    @Test
    public void executionCompletedShouldChangeGroupStatusToFailedIfNotAllServicesAreDelivered() {
        final ServiceStatus previousGroupStatus = scheduledServiceGroup.getStatus();
        assumeThat(previousGroupStatus, not(isOneOf(ServiceStatus.FAILED, ServiceStatus.DELIVERED)));

        executionService.executionCompleted(SCHEDULED_SERVICE_GROUP, EXECUTION_ID, CHANNEL_TYPE_1_NAME);

        scheduledServiceGroup = messagingGroupService.getById(SCHEDULED_SERVICE_GROUP);
        assertThat(scheduledServiceGroup.getStatus(), is(ServiceStatus.FAILED));
    }

    @Test
    public void executionCompletedShouldChangeGroupStatusToDeliveredIfAllServicesAreDelivered() {
        final ServiceStatus previousGroupStatus = scheduledServiceGroup.getStatus();
        assumeThat(previousGroupStatus, not(isOneOf(ServiceStatus.FAILED, ServiceStatus.DELIVERED)));

        messagingService.registerAttempt(pendingScheduledService, ServiceStatus.DELIVERED, DateUtil.toDate(DateUtil.now()),
                EXECUTION_ID);
        messagingService.registerAttempt(failedScheduledService, ServiceStatus.DELIVERED, DateUtil.toDate(DateUtil.now()),
                EXECUTION_ID);
        messagingService.registerAttempt(pendingScheduledServiceInAnotherChannel, ServiceStatus.DELIVERED,
                DateUtil.toDate(DateUtil.now()), EXECUTION_ID);

        executionService.executionCompleted(SCHEDULED_SERVICE_GROUP, EXECUTION_ID, CHANNEL_TYPE_1_NAME);

        scheduledServiceGroup = messagingGroupService.getById(SCHEDULED_SERVICE_GROUP);
        assertThat(scheduledServiceGroup.getStatus(), is(ServiceStatus.DELIVERED));
    }

    @Test
    public void executionCompletedShouldBeCompletedSuccessfulWithNullExecutionId() {
        final int previousAttemptsNumForFailedService = failedScheduledService.getNumberOfAttempts();
        assumeThat(failedScheduledService.getDeliveryAttempts(), not(hasItem(hasProperty("serviceExecution", nullValue()))));

        executionService.executionCompleted(SCHEDULED_SERVICE_GROUP, null, CHANNEL_TYPE_1_NAME);

        failedScheduledService = messagingService.getById(FAILED_SCHEDULED_SERVICE);
        assertThat(failedScheduledService.getNumberOfAttempts(), is(previousAttemptsNumForFailedService + 1));
        assertThat(failedScheduledService.getDeliveryAttempts(), hasItem(hasProperty("serviceExecution", nullValue())));
    }

    @Test(expected = EntityNotFoundException.class)
    public void executionCompletedShouldThrowExceptionWhenGroupWithPassedIdDoesNotExist() {
        executionService.executionCompleted(Constant.NOT_EXISTING_ID, EXECUTION_ID, CHANNEL_TYPE_1_NAME);
    }

    @Test
    public void executionCompletedShouldInvokeReschedulingAlsoForFailedAndPendingServices() {
        assumeThat(pendingScheduledService.getStatus(), is(ServiceStatus.PENDING));
        assumeThat(failedScheduledService.getStatus(), is(ServiceStatus.FAILED));

        executionService.executionCompleted(SCHEDULED_SERVICE_GROUP, EXECUTION_ID, CHANNEL_TYPE_1_NAME);
        TaskDefinition task = getCreatedTask();
        assertNotNull(task);

        ScheduledExecutionContext executionContext = getExecutionContext(task);
        assertThat(executionContext.getGroupId(), is(SCHEDULED_SERVICE_GROUP));
        assertThat(executionContext.getServiceIdsToExecute(),
                hasItems(failedScheduledService.getId(), pendingScheduledService.getId()));
        assertThat(executionContext.getServiceIdsToExecute(),
                allOf(not(hasItems(pendingScheduledServiceInAnotherChannel.getId())),
                        not(hasItems(deliveredScheduledService.getId()))));
    }

    @Test
    public void executionCompletedShouldInvokeReschedulingAlsoWhenAllServicesHavePendingStatus() {
        ArrayList<Integer> servicesWithPendingStatusIds = new ArrayList<>();
        for (ScheduledService ss : scheduledServiceGroup.getScheduledServices()) {
            ss.setStatus(ServiceStatus.PENDING);
            servicesWithPendingStatusIds.add(ss.getId());
        }
        scheduledServiceGroup = messagingGroupService.saveOrUpdate(scheduledServiceGroup);
        assumeThat(scheduledServiceGroup.getScheduledServices(),
                everyItem(hasProperty("status", is(ServiceStatus.PENDING))));

        executionService.executionCompleted(SCHEDULED_SERVICE_GROUP, EXECUTION_ID, CHANNEL_TYPE_1_NAME);
        TaskDefinition task = getCreatedTask();
        assertNotNull(task);

        ScheduledExecutionContext executionContext = getExecutionContext(task);
        assertThat(executionContext.getGroupId(), is(SCHEDULED_SERVICE_GROUP));
        assertThat(executionContext.getServiceIdsToExecute(), Matchers.is(servicesWithPendingStatusIds));
    }

    private TaskDefinition getCreatedTask() {
        verify(schedulerService, atLeastOnce()).saveTaskDefinition(taskCaptor.capture());
        return taskCaptor.getValue();
    }

    private ScheduledExecutionContext getExecutionContext(TaskDefinition task) {
        return ScheduledExecutionContextUtil.fromJson(task.getProperty(EXECUTION_CONTEXT));
    }
}
