/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.impl.MessagingServiceImpl;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MessagingServiceTest extends BaseTest {

    @Captor
    private ArgumentCaptor<ScheduledService> captor;

    @Mock
    private MessagingDao dao;

    @Mock
    private ConfigService configService;

    @Mock
    private ReschedulingStrategy reschedulingStrategy;

    @InjectMocks
    private MessagingServiceImpl messagingService;

    @Before
    public void setUp() {
        when(configService.getReschedulingStrategy(any())).thenReturn(reschedulingStrategy);
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

        verify(dao).saveOrUpdate(captor.capture());

        ScheduledService actualScheduledService = captor.getValue();
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

        verify(dao).saveOrUpdate(captor.capture());
        ScheduledService actualScheduledService = captor.getValue();
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(1);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(2, actualScheduledService.getDeliveryAttempts().size());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(timestamp, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }
}
