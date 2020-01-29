/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.exception.EntityNotFoundException;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.impl.MessagingServiceImpl;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;

@RunWith(MockitoJUnitRunner.class)
public class MessagingServiceTest extends BaseTest {

    private static final String TEXT_RESPONSE = "Dummy text response";

    private static final int QUESTION_ID = 1;
    private static final int RESPONSE_ID = 2;

    @Captor
    private ArgumentCaptor<ScheduledService> serviceCaptor;

    @Captor
    private ArgumentCaptor<ActorResponse> responseCaptor;

    @Mock
    private MessagingDao dao;

    @Mock
    private ConfigService configService;

    @Mock
    private ReschedulingStrategy reschedulingStrategy;

    @Mock
    private ConceptService conceptService;

    @Mock
    private ActorResponseDao responseDao;

    @Mock
    private Concept questionConcept;

    @Mock
    private Concept responseConcept;

    @InjectMocks
    private MessagingServiceImpl messagingService;

    @Before
    public void setUp() {
        when(configService.getReschedulingStrategy(any())).thenReturn(reschedulingStrategy);
        when(conceptService.getConcept(eq(QUESTION_ID))).thenReturn(questionConcept);
        when(conceptService.getConcept(eq(RESPONSE_ID))).thenReturn(responseConcept);
    }

    @Test
    public void registerAttemptShouldRegisterFirstAttempt() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerAttempt(scheduledService.getId(), newStatus, timestamp, serviceExecution);

        verify(dao).saveOrUpdate(serviceCaptor.capture());

        ScheduledService actualScheduledService = serviceCaptor.getValue();
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(0);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(1, actualScheduledService.getDeliveryAttempts().size());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(timestamp, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }

    @Test
    public void registerAttemptShouldRegisterNextAttempt() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final DeliveryAttempt deliveryAttempt = new DeliveryAttemptBuilder()
                .withScheduledService(scheduledService)
                .withStatus(ServiceStatus.PENDING)
                .build();
        scheduledService.getDeliveryAttempts().add(deliveryAttempt);
        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerAttempt(scheduledService.getId(), newStatus, timestamp, serviceExecution);

        verify(dao).saveOrUpdate(serviceCaptor.capture());
        ScheduledService actualScheduledService = serviceCaptor.getValue();
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(1);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(2, actualScheduledService.getDeliveryAttempts().size());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(timestamp, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }

    @Test
    public void registerResponseAndStatusShouldPersistAttemptAndResponse() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);

        validateRegisterResponseAndStatusResult(scheduledService, newStatus, serviceExecution, timestamp);
    }

    @Test
    public void registerResponseAndStatusShouldPersistAttemptAndResponseWhenPassedServiceStatusAsString() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final String newStatus = "DELIVERED";
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);

        validateRegisterResponseAndStatusResult(scheduledService, ServiceStatus.valueOf(newStatus),
                serviceExecution, timestamp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerResponseAndStatusShouldThrowExceptionIfStatusIsNotValidServiceStatus() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final String newStatus = "NotValidServiceStatus";
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerResponseAndStatusShouldThrowExceptionIfStatusIsPending() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final ServiceStatus newStatus = ServiceStatus.PENDING;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerResponseAndStatusShouldThrowExceptionIfStringStatusIsPending() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final String newStatus = "PENDING";
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerResponseAndStatusShouldThrowExceptionIfStatusIsFuture() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final ServiceStatus newStatus = ServiceStatus.FUTURE;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerResponseAndStatusShouldThrowExceptionIfStringStatusIsFuture() {
        final ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withStatus(ServiceStatus.PENDING)
                .withServiceExec("123")
                .build();
        final String newStatus = "FUTURE";
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        when(dao.getById(eq(scheduledService.getId()))).thenReturn(scheduledService);
        when(dao.saveOrUpdate(eq(scheduledService))).thenReturn(scheduledService);

        messagingService.registerResponseAndStatus(scheduledService.getId(), QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);
    }

    @Test(expected = EntityNotFoundException.class)
    public void registerResponseAndStatusShouldThrowExceptionIfScheduledServiceNotExists() {
        final String newStatus = "DELIVERED";
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        messagingService.registerResponseAndStatus(Constant.NOT_EXISTING_ID, QUESTION_ID, RESPONSE_ID,
                TEXT_RESPONSE, newStatus, timestamp, serviceExecution);
    }

    private void validateRegisterResponseAndStatusResult(ScheduledService scheduledService, ServiceStatus newStatus,
                                                         String serviceExecution, Date timestamp) {
        verify(responseDao).saveOrUpdate(responseCaptor.capture());
        verify(dao).saveOrUpdate(serviceCaptor.capture());

        ActorResponse response = responseCaptor.getValue();
        assertEquals(scheduledService, response.getScheduledService());
        assertEquals(questionConcept, response.getQuestion());
        assertEquals(responseConcept, response.getResponse());
        assertEquals(TEXT_RESPONSE, response.getTextResponse());
        assertEquals(timestamp, response.getAnsweredTime());

        ScheduledService actualScheduledService = serviceCaptor.getValue();
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(0);
        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(1, actualScheduledService.getDeliveryAttempts().size());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(timestamp, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }
}

