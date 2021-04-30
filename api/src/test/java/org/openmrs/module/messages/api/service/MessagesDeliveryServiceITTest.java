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
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.execution.GroupedServiceResult;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.GroupedServiceResultListKey;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.util.JsonUtil;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ScheduledExecutionContextBuilder;
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
import static org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition.EXECUTION_CONTEXT;
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

    private Gson gson = JsonUtil.getGson();

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
        ScheduledExecutionContext executionContext = getExecutionContext(resultList);

        deliveryService.scheduleDelivery(executionContext);

        TaskDefinition task = getCreatedTask();
        assertNotNull(task);
        assertEquals(NEVER_REPEAT, task.getRepeatInterval());
        assertEquals(date, task.getNextExecutionTime());

        ScheduledExecutionContext savedExecutionContext = getExecutionContext(task);
        assertEquals(executionContext, savedExecutionContext);
    }

    private ScheduledExecutionContext getExecutionContext(ServiceResultList resultList) {
        List<GroupedServiceResult> groupedServiceResults = new ArrayList<GroupedServiceResult>();
        for (ServiceResult serviceResult : resultList.getResults()) {
            groupedServiceResults.add(new GroupedServiceResult(resultList.getChannelType(), serviceResult));
        }

        GroupedServiceResultListKey groupedServiceResultListKey =
                new GroupedServiceResultListKey(resultList.getChannelType(), resultList.getResults().get(0),
                        resultList.getActorType());
        GroupedServiceResultList groupResults =
                new GroupedServiceResultList(groupedServiceResultListKey, groupedServiceResults);

        ScheduledServiceGroup group = groupMapper.fromDto(groupResults);
        group = messagingGroupService.saveOrUpdate(group);
        return new ScheduledExecutionContextBuilder()
                .withScheduledServices(group.getScheduledServices())
                .withChannelType(group.getChannelType())
                .withExecutionDate(groupResults.getKey().getDate())
                .withActorId(group.getActor().getId())
                .withActorType(groupResults.getKey().getActorType())
                .withGroupId(group.getId())
                .build();
    }

    private TaskDefinition getCreatedTask() throws SchedulerException {
        verify(schedulerService, times(2)).saveTaskDefinition(taskCaptor.capture());
        return taskCaptor.getValue();
    }

    private ScheduledExecutionContext getExecutionContext(TaskDefinition task) {
        return gson.fromJson(task.getProperty(EXECUTION_CONTEXT), ScheduledExecutionContext.class);
    }

    private ServiceResultList createServiceResults(Date date) {
        List<ServiceResult> results = new ArrayList<ServiceResult>();
        results.add(getServiceResult(date, Constant.CHANNEL_TYPE_SMS));
        results.add(getServiceResult(date, Constant.CHANNEL_TYPE_SMS));
        return getDefaultPersonResultList(results, Constant.CHANNEL_TYPE_SMS);
    }

    private ServiceResult getServiceResult(Date date, String channelType) {
        return getServiceResult(date, channelType, new HashMap<String, Object>());
    }

    private ServiceResult getServiceResult(Date date, String channelType, Map<String, Object> additionalParams) {
        return new ServiceResultBuilder()
                .withExecutionDate(date)
                .withPatientTemplate(DEFAULT_PATIENT_TEMPLATE_ID)
                .withParams(additionalParams)
                .withChannelType(channelType)
                .build();
    }

    private ServiceResultList getDefaultPersonResultList(List<ServiceResult> results, String channelType) {
        return getResultList(results, DEFAULT_PERSON_ID, DEFAULT_PERSON_ID, channelType);
    }

    private ServiceResultList getResultList(List<ServiceResult> results, Integer actorId, Integer patientId, String channelType) {
        return new ServiceResultListBuilder()
                .withActorId(actorId)
                .withPatientId(patientId)
                .withChannelType(channelType)
                .withServiceName(DEFAULT_TEMPLATE_NAME)
                .withServiceResults(results)
                .build();
    }
}
