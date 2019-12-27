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
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.execution.ChannelType;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;
import org.openmrs.module.messages.api.util.MapperUtil;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ServiceResultBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;
import org.openmrs.scheduler.SchedulerException;
import org.openmrs.scheduler.SchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.messages.api.execution.ChannelType.CALL;
import static org.openmrs.module.messages.api.execution.ChannelType.SMS;
import static org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition.EXECUTION_CONTEXT;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PATIENT_TEMPLATE_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_PERSON_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.DEFAULT_TEMPLATE_NAME;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

public class MessagesDeliveryServiceITTest extends ContextSensitiveTest {

    public static final Long NEVER_REPEAT = 0L;

    @Autowired
    @Qualifier("messages.deliveryService")
    private MessagesDeliveryService deliveryService;

    @Autowired
    @Qualifier("messages.messagingGroupService")
    private MessagingGroupService messagingGroupService;

    @Autowired
    @Qualifier("messages.scheduledGroupMapper")
    private ScheduledGroupMapper groupMapper;

    @Autowired
    @Qualifier("schedulerService")
    private SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env

    @Captor
    private ArgumentCaptor<TaskDefinition> taskCaptor;

    private Gson gson = MapperUtil.getGson();

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");

        reset(schedulerService);
    }

    @Test
    public void shouldSaveScheduledServices() throws Exception {
        Date date = new DateBuilder().build();

        ServiceResultList resultList = createServiceResults(date);
        ScheduledServicesExecutionContext executionContext = getExecutionContext(date, resultList);

        deliveryService.schedulerDelivery(executionContext);

        TaskDefinition task = getCreatedTask();
        assertNotNull(task);
        assertEquals(NEVER_REPEAT, task.getRepeatInterval());
        assertEquals(date, task.getNextExecutionTime());

        ScheduledServicesExecutionContext savedExecutionContext = getExecutionContext(task);
        assertEquals(executionContext, savedExecutionContext);
    }

    private ScheduledServicesExecutionContext getExecutionContext(Date date, ServiceResultList resultList) {
        GroupedServiceResultList groupResults = new GroupedServiceResultList(DEFAULT_PATIENT_ID, date, resultList);
        ScheduledServiceGroup group = groupMapper.fromDto(groupResults);
        group = messagingGroupService.saveOrUpdate(group);
        return new ScheduledServicesExecutionContext(
                group.getScheduledServices(),
                groupResults.getExecutionDate(),
                group.getActor());
    }

    private TaskDefinition getCreatedTask() throws SchedulerException {
        verify(schedulerService, times(1)).scheduleTask(taskCaptor.capture());
        return taskCaptor.getValue();
    }

    private ScheduledServicesExecutionContext getExecutionContext(TaskDefinition task) {
        return gson.fromJson(
                task.getProperty(EXECUTION_CONTEXT),
                ScheduledServicesExecutionContext.class);
    }

    private ServiceResultList createServiceResults(Date date) {
        List<ServiceResult> results = new ArrayList<>();
        results.add(getServiceResult(date, SMS));
        results.add(getServiceResult(date, CALL));
        return getDefaultPersonResultList(results);
    }

    private ServiceResult getServiceResult(Date date, ChannelType channelType) {
        return getServiceResult(date, channelType, new HashMap<>());
    }

    private ServiceResult getServiceResult(Date date, ChannelType channelType, Map<String, Object> additionalParams) {
        return new ServiceResultBuilder().withExecutionDate(date)
                .withPatientTemplate(DEFAULT_PATIENT_TEMPLATE_ID)
                .withParams(additionalParams)
                .withChannelType(channelType).build();
    }

    private ServiceResultList getDefaultPersonResultList(List<ServiceResult> results) {
        return getResultList(results, DEFAULT_PERSON_ID, DEFAULT_PERSON_ID);
    }

    private ServiceResultList getResultList(List<ServiceResult> results, Integer actorId, Integer patientId) {
        return new ServiceResultListBuilder()
                .withActorId(actorId).withPatientId(patientId)
                .withServiceName(DEFAULT_TEMPLATE_NAME)
                .withServiceResults(results)
                .build();
    }
}
