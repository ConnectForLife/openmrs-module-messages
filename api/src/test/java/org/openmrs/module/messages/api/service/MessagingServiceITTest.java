/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.hamcrest.Matchers;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.builder.ActorResponseBuilder;
import org.openmrs.scheduler.SchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MessagingServiceITTest extends ContextSensitiveTest {

    private static final String QUESTION_UUID = "16f1ce79-ef8a-47ca-bc40-fee648a835b4";

    private static final String RESPONSE_UUID = "9b251fd0-b900-4b11-9b77-b5174a0368b8";

    private static final String SCHEDULE_UUID = "b3de6d76-3e31-41cf-955d-ad14b9db07ff";

    private static final Date TIMESTAMP = new Date(2019, Calendar.NOVEMBER, 21);

    private Concept question;

    private Concept response;

    private ScheduledService scheduledService;

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @Autowired
    @Qualifier("messages.MessagingDao")
    private MessagingDao messagingDao;

    @Autowired
    @Qualifier("messages.ActorResponseDao")
    private ActorResponseDao actorResponseDao;

    @Autowired
    @Qualifier("schedulerService")
    private SchedulerService schedulerService; // this is mocked in xml - in does not work in testing env

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml");
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");
        question = Context.getConceptService().getConceptByUuid(QUESTION_UUID);
        response = Context.getConceptService().getConceptByUuid(RESPONSE_UUID);
        scheduledService = messagingDao.getByUuid(SCHEDULE_UUID);

        reset(schedulerService);
    }

    @Test
    public void shouldReturnRegisteredActorResponse() {
        ActorResponseBuilder builder = new ActorResponseBuilder();
        ActorResponse expected = builder.withScheduledService(scheduledService)
                .withQuestion(question)
                .withResponse(response)
                .withTextResponse(scheduledService.getPatientTemplate().getTemplateFieldValues().get(0).getValue())
                .withAnsweredTime(TIMESTAMP)
                .build();

        ActorResponse actual = messagingService.registerResponse(scheduledService.getId(),
                question.getId(),
                response.getId(),
                scheduledService.getPatientTemplate().getTemplateFieldValues().get(0).getValue(),
                TIMESTAMP);

        assertThat(actual, not(nullValue()));
        assertThat(actual.getId(), not(nullValue()));
        assertThat(actorResponseDao.getByUuid(actual.getUuid()), not(nullValue()));
        assertThat(actual.getTextResponse(), is(expected.getTextResponse()));
        assertThat(actual.getAnsweredTime(), is(expected.getAnsweredTime()));

        assertThat(actual.getQuestion().getConceptId(),
                is(expected.getQuestion().getConceptId()));
        assertThat(actual.getQuestion().getPreferredName(Locale.ENGLISH),
                is(expected.getQuestion().getPreferredName(Locale.ENGLISH)));

        assertThat(actual.getResponse().getConceptId(),
                is(expected.getResponse().getConceptId()));
        assertThat(actual.getResponse().getPreferredName(Locale.ENGLISH),
                is(expected.getResponse().getPreferredName(Locale.ENGLISH)));

        assertThat(actual.getScheduledService().getId(),
                is(expected.getScheduledService().getId()));
        assertThat(actual.getScheduledService().getLastServiceExecution(),
                is(expected.getScheduledService().getLastServiceExecution()));
        assertThat(actual.getScheduledService().getStatus(),
                is(expected.getScheduledService().getStatus()));
    }

    @Test(expected = EntityNotFoundException.class)
    public void shouldThrowExceptionWhenScheduledServiceIsNotSaved() {
        messagingService.registerResponse(Constant.NOT_EXISTING_ID,
                question.getId(),
                response.getId(),
                scheduledService.getPatientTemplate().getTemplateFieldValues().get(0).getValue(),
                TIMESTAMP);
    }

    @Test
    public void registerAttemptShouldAddFirstAttemptAndUpdateScheduledService() {
        Assume.assumeThat(scheduledService.getNumberOfAttempts(), Matchers.is(0));

        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";

        ScheduledService actualScheduledService = messagingService
                .registerAttempt(scheduledService.getId(), newStatus, TIMESTAMP, serviceExecution);
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(0);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(1, actualScheduledService.getNumberOfAttempts());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(TIMESTAMP, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }

    @Test
    public void registerAttemptShouldAddNextAttemptAndUpdateScheduledService() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), ServiceStatus.FAILED,
                new Date(), UUID.randomUUID().toString());
        assertEquals(1, scheduledService.getNumberOfAttempts());
        assertEquals(ServiceStatus.FAILED, scheduledService.getStatus());
        assertEquals(ServiceStatus.FAILED, scheduledService.getDeliveryAttempts().get(0).getStatus());

        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";

        ScheduledService actualScheduledService = messagingService
                .registerAttempt(scheduledService.getId(), newStatus, TIMESTAMP, serviceExecution);
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(1);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(2, actualScheduledService.getNumberOfAttempts());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(TIMESTAMP, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }

    @Test
    public void registerAttemptShouldParseStringStatusAddNextAttemptAndUpdateScheduledService() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), "FAILED",
                new Date(), UUID.randomUUID().toString());
        assertEquals(1, scheduledService.getNumberOfAttempts());
        assertEquals(ServiceStatus.FAILED, scheduledService.getStatus());
        assertEquals(ServiceStatus.FAILED, scheduledService.getDeliveryAttempts().get(0).getStatus());

        final String newStatus = "DELIVERED";
        final String serviceExecution = "321";

        ScheduledService actualScheduledService = messagingService
                .registerAttempt(scheduledService.getId(), newStatus, TIMESTAMP, serviceExecution);
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(1);

        assertEquals(ServiceStatus.DELIVERED, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(2, actualScheduledService.getNumberOfAttempts());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(ServiceStatus.DELIVERED, actualAttempt.getStatus());
        assertEquals(TIMESTAMP, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }

    @Test(expected = EntityNotFoundException.class)
    public void registerAttemptShouldThrowExceptionIfServiceNotPersisted() {
        scheduledService = messagingService.registerAttempt(Constant.NOT_EXISTING_ID, ServiceStatus.FAILED,
                new Date(), UUID.randomUUID().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerAttemptShouldThrowExceptionIfCannotParseStringStatus() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), "notValidStatus",
                new Date(), UUID.randomUUID().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerAttemptShouldThrowExceptionIfStatusIsPending() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), ServiceStatus.PENDING,
                new Date(), UUID.randomUUID().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerAttemptShouldThrowExceptionIfStringStatusIsPending() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), "PENDING",
                new Date(), UUID.randomUUID().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerAttemptShouldThrowExceptionIfStatusIsFuture() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), ServiceStatus.FUTURE,
                new Date(), UUID.randomUUID().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerAttemptShouldThrowExceptionIfStringStatusIsFuture() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), "FUTURE",
                new Date(), UUID.randomUUID().toString());
    }

    @Test
    public void registerAttemptShouldRescheduleFailedAttempt() throws Exception {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), ServiceStatus.FAILED,
                new Date(), UUID.randomUUID().toString());

        verify(schedulerService, times(1)).scheduleTask(any());
    }

    @Test
    public void registerAttemptShouldRescheduleSuccessfulAttempt() throws Exception {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), ServiceStatus.DELIVERED,
                new Date(), UUID.randomUUID().toString());

        verify(schedulerService, times(0)).scheduleTask(any());
    }
}
