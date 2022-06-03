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
import org.openmrs.api.context.Daemon;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.service.impl.CallFlowServiceResultsHandlerServiceImpl;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.ScheduledExecutionContextBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALL_FLOW_INITIATE_CALL_EVENT;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ACTOR_ID;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ACTOR_TYPE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ADDITIONAL_PARAMS;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.CONFIG;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.FLOW_NAME;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.PHONE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.REF_KEY;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, Daemon.class})
public class CallFlowServiceResultsHandlerServiceImplTest {

    private static final String PHONE_NUMBER = "612345987";
    private static final String CALL_FLOW_NAME = "WelcomeFlow";
    private static final String CONFIG_NAME = "nexmo";

    @Mock
    private PersonService personService;

    @Mock
    private MessagesEventService messagesEventService;

    @Mock
    private Person person;

    @Mock
    private PersonAttribute personAttribute;

    @Captor
    private ArgumentCaptor<MessagesEvent> messagesEventCaptor;

    @InjectMocks
    private CallFlowServiceResultsHandlerServiceImpl callFlowServiceResultsHandlerService;

    private ScheduledService scheduledService;

    @Before
    public void setUp() {
        mockStatic(Context.class);
        AdministrationService administrationService = mock(AdministrationService.class);
        when(administrationService.getGlobalProperty(ConfigConstants.CALL_CONFIG,
                ConfigConstants.CALL_CONFIG_DEFAULT_VALUE)).thenReturn(ConfigConstants.CALL_CONFIG_DEFAULT_VALUE);
        when(administrationService.getGlobalProperty(ConfigConstants.CALL_DEFAULT_FLOW,
                ConfigConstants.CALL_DEFAULT_FLOW_DEFAULT_VALUE))
                .thenReturn(ConfigConstants.CALL_DEFAULT_FLOW_DEFAULT_VALUE);
        PowerMockito.when(Context.getAdministrationService()).thenReturn(administrationService);

        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();
        scheduledService = new ScheduledServiceBuilder()
                .withTemplate(patientTemplate)
                .build();

        when(person.getAttribute(ConfigConstants.PERSON_PHONE_ATTR)).thenReturn(personAttribute);
        when(personAttribute.getValue()).thenReturn(PHONE_NUMBER);
    }

    @Test
    public void shouldSendEventWithProperEventParams() {
        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();
        when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);

        callFlowServiceResultsHandlerService.handle(Collections.singletonList(scheduledService), scheduledExecutionContext);

        verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

        MessagesEvent messagesEvent = messagesEventCaptor.getValue();
        Map<String, Object> params = messagesEvent.getParameters();
        Map<String, Object> additionalParams = (Map<String, Object>) params.get(ADDITIONAL_PARAMS);

        assertThat(messagesEvent.getSubject(), is(CALL_FLOW_INITIATE_CALL_EVENT));
        assertThat(params.get(CONFIG), is(ConfigConstants.CALL_CONFIG_DEFAULT_VALUE));
        assertThat(params.get(FLOW_NAME), is(ConfigConstants.CALL_DEFAULT_FLOW_DEFAULT_VALUE));
        assertThat(additionalParams.get(PHONE), is(PHONE_NUMBER));
        assertThat(additionalParams.get(ACTOR_ID), is(Integer.toString(scheduledExecutionContext.getActorId())));
        assertThat(additionalParams.get(ACTOR_TYPE), is(scheduledExecutionContext.getActorType()));
        assertThat(additionalParams.get(REF_KEY), is(Integer.toString(scheduledExecutionContext.getGroupId())));
    }

    @Test
    public void shouldSendEventWithProperCallConfigAndCallFlowName() {
        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();
        scheduledExecutionContext.getChannelConfiguration().put(
                CallFlowServiceResultsHandlerServiceImpl.CALL_CHANNEL_CONF_FLOW_NAME, CALL_FLOW_NAME);
        scheduledExecutionContext.getChannelConfiguration().put(
                CallFlowServiceResultsHandlerServiceImpl.CALL_CHANNEL_CONFIG_NAME, CONFIG_NAME);

        when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);

        callFlowServiceResultsHandlerService.handle(Collections.singletonList(scheduledService), scheduledExecutionContext);

        verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

        MessagesEvent messagesEvent = messagesEventCaptor.getValue();
        Map<String, Object> params = messagesEvent.getParameters();

        assertThat(params.get(CONFIG), is(CONFIG_NAME));
        assertThat(params.get(FLOW_NAME), is(CALL_FLOW_NAME));
    }
}
