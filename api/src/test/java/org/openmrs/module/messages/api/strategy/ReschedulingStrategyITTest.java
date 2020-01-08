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
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.MapperUtil;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition.EXECUTION_CONTEXT;
import static org.openmrs.module.messages.api.service.DatasetConstants.DELIVERED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.FAILED_SCHEDULED_SERVICE;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

public class ReschedulingStrategyITTest extends ContextSensitiveTest {

    private static final Long NEVER_REPEAT = 0L;
    private static final int MAX_ATTEMPTS = 3;

    @Autowired
    @Qualifier("messages.failedMessageReschedulingStrategy")
    private ReschedulingStrategy reschedulingStrategy;

    @Autowired
    @Qualifier("schedulerService")
    private SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @Captor
    private ArgumentCaptor<TaskDefinition> taskCaptor;

    private final Gson gson = MapperUtil.getGson();
    private final Date date = new DateBuilder().build();

    private ScheduledService deliveredScheduledService;
    private ScheduledService failedScheduledService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml");
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");

        deliveredScheduledService = messagingService.getById(DELIVERED_SCHEDULED_SERVICE);
        failedScheduledService = messagingService.getById(FAILED_SCHEDULED_SERVICE);

        reset(schedulerService);
    }

    @Test
    public void shouldRescheduleFailedDeliveryWithCorrectTime() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS - 1);

        reschedulingStrategy.execute(failedScheduledService);

        TaskDefinition task = getCreatedTask();
        assertNotNull(task);
        assertEquals(NEVER_REPEAT, task.getRepeatInterval());

        ScheduledExecutionContext executionContext = getExecutionContext(task);
        assertEquals(failedScheduledService.getGroup().getActor().getId().intValue(), executionContext.getActorId());
        assertEquals(executionContext.getServiceIdsToExecute(), wrap(failedScheduledService.getId()));
    }

    @Test
    public void shouldNotRescheduleNotFailedDelivery() throws Exception {
        reschedulingStrategy.execute(deliveredScheduledService);

        verify(schedulerService, times(0)).scheduleTask(any());
    }

    @Test
    public void shouldNotRescheduleAfterExceedingMaxNumberOfAttempts() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS);

        reschedulingStrategy.execute(failedScheduledService);

        verify(schedulerService, times(0)).scheduleTask(any());
    }

    private TaskDefinition getCreatedTask() throws SchedulerException {
        verify(schedulerService, times(1)).scheduleTask(taskCaptor.capture());
        return taskCaptor.getValue();
    }

    private ScheduledExecutionContext getExecutionContext(TaskDefinition task) {
        return gson.fromJson(
                task.getProperty(EXECUTION_CONTEXT),
                ScheduledExecutionContext.class);
    }

    private void addDeliveryAttempts(ScheduledService failedScheduledService, int numberOfAttempts) {
        DeliveryAttemptBuilder deliveryAttemptBuilder = new DeliveryAttemptBuilder();
        List<DeliveryAttempt> deliveryAttempts = failedScheduledService.getDeliveryAttempts();
        for (int i = 0; i < numberOfAttempts; ++i) {
            deliveryAttempts.add(deliveryAttemptBuilder
                    .withScheduledService(failedScheduledService)
                    .buildAsNew());
        }
    }

    private List<Integer> wrap(Integer id) {
        ArrayList<Integer> ids = new ArrayList<>();
        ids.add(id);
        return ids;
    }
}
