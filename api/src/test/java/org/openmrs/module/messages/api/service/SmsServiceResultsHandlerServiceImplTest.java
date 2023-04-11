/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openmrs.module.messages.api.constants.MessagesConstants.SMS_INITIATE_EVENT;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.service.impl.AbstractTextMessageServiceResultsHandlerService;
import org.openmrs.module.messages.api.service.impl.SmsServiceResultsHandlerServiceImpl;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.ScheduledExecutionContextBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceParameterBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class})
public class SmsServiceResultsHandlerServiceImplTest {

    private static final String PHONE_NUMBER = "612345987";

    private static final String WHATSAPP_PARSED_TEMPLATE = "{ id:\"111\",language:\"EN\",visitId:\"666\" }";

    private static final String SMS_EXPECTED_MESSAGE = "This is message.";
    private static final String SMS_PARSED_TEMPLATE = String.format("{ message:\"%s\" }", SMS_EXPECTED_MESSAGE);

    private static final String SMS_CONFIG_GP_NAME = "nexmo";
    private static final String SMS_CONFIG_CONTEXT_NAME = "turnIO";

    @Mock
    private PersonService personService;
    @Mock
    private MessagesEventService messagesEventService;
    @Mock
    private NotificationTemplateService notificationTemplateService;
    @Mock
    private MessagingService messagingService;
    @Mock
    private MessagesExecutionService messagesExecutionService;
    @Mock
    private Person person;
    @Mock
    private PersonAttribute personAttribute;

    @Captor
    private ArgumentCaptor<MessagesEvent> messagesEventCaptor;

    @InjectMocks
    private SmsServiceResultsHandlerServiceImpl smsServiceResultsHandlerService;

    private ScheduledService scheduledService;
    private PatientTemplate patientTemplate;

    @Before
    public void setUp() {
        mockStatic(Context.class);
        patientTemplate = new PatientTemplateBuilder().build();
        scheduledService = new ScheduledServiceBuilder()
                .withTemplate(patientTemplate)
                .build();


        when(person.getAttribute(ConfigConstants.PERSON_PHONE_ATTR)).thenReturn(personAttribute);
        when(personAttribute.getValue()).thenReturn(PHONE_NUMBER);

        AdministrationService administrationService = mock(AdministrationService.class);
        PowerMockito.when(Context.getAdministrationService()).thenReturn(administrationService);
        when(administrationService.getGlobalProperty(ConfigConstants.SMS_CONFIG)).thenReturn(SMS_CONFIG_GP_NAME);
    }

    @Test
    public void shouldSendEventWithProperEventParamsForVisitReminderSms() {
        prepareVisitReminderService();

        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();
        when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);
        when(notificationTemplateService.parseTemplate(eq(patientTemplate), anyMap())).thenReturn(SMS_PARSED_TEMPLATE);

        smsServiceResultsHandlerService.handle(Collections.singletonList(scheduledService), scheduledExecutionContext);

        verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

        MessagesEvent messagesEvent = messagesEventCaptor.getValue();
        Map<String, Object> params = messagesEvent.getParameters();

        assertThat(messagesEvent.getSubject(), is(SMS_INITIATE_EVENT));
        assertThat(params.get(SmsEventParamConstants.RECIPIENTS), is(Collections.singletonList(PHONE_NUMBER)));
        assertThat(params.get(SmsEventParamConstants.MESSAGE), is(SMS_EXPECTED_MESSAGE));
        assertThat(params.get(SmsEventParamConstants.CONFIG), is(SMS_CONFIG_GP_NAME));
    }

    @Test
    public void shouldSendEventWithProperEventParamsForVisitReminderWhatsapp() {
        prepareVisitReminderService();

        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();
        when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);
        when(notificationTemplateService.parseTemplate(eq(patientTemplate), anyMap())).thenReturn(WHATSAPP_PARSED_TEMPLATE);

        smsServiceResultsHandlerService.handle(Collections.singletonList(scheduledService), scheduledExecutionContext);

        verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

        MessagesEvent messagesEvent = messagesEventCaptor.getValue();
        Map<String, Object> params = messagesEvent.getParameters();

        assertThat(messagesEvent.getSubject(), is(SMS_INITIATE_EVENT));
        assertThat(params.get(SmsEventParamConstants.RECIPIENTS), is(Collections.singletonList(PHONE_NUMBER)));
        assertThat(params.get(SmsEventParamConstants.CONFIG), is(SMS_CONFIG_GP_NAME));
    }

    @Test
    public void shouldSendEventWithProperSmsConfigFromExecutionContext() {
        prepareVisitReminderService();

        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();
        scheduledExecutionContext.getChannelConfiguration().put(AbstractTextMessageServiceResultsHandlerService.CONFIG_KEY, SMS_CONFIG_CONTEXT_NAME);
        when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);
        when(notificationTemplateService.parseTemplate(eq(patientTemplate), anyMap())).thenReturn(WHATSAPP_PARSED_TEMPLATE);

        smsServiceResultsHandlerService.handle(Collections.singletonList(scheduledService), scheduledExecutionContext);

        verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

        MessagesEvent messagesEvent = messagesEventCaptor.getValue();
        Map<String, Object> params = messagesEvent.getParameters();

        assertThat(messagesEvent.getSubject(), is(SMS_INITIATE_EVENT));
        assertThat(params.get(SmsEventParamConstants.RECIPIENTS), is(Collections.singletonList(PHONE_NUMBER)));
        assertThat(params.get(SmsEventParamConstants.CONFIG), is(SMS_CONFIG_CONTEXT_NAME));
    }

    private void prepareVisitReminderService() {
        ScheduledServiceParameter visitId = new ScheduledServiceParameterBuilder()
                .withType("visitId")
                .withValue("666")
                .build();
        ScheduledServiceParameter visitTypeId = new ScheduledServiceParameterBuilder()
                .withType("visitTypeId")
                .withValue("12")
                .build();
        scheduledService = new ScheduledServiceBuilder()
                .withTemplate(patientTemplate)
                .withScheduledServiceParameters(Arrays.asList(visitId, visitTypeId))
                .build();
    }
}
