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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_CONFIG;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_FLOW;
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

    @Before
    public void setUp() {
        mockStatic(Context.class);
    }

    @Test
    public void shouldSendEventWithProperEventParams() {
        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();
        ScheduledService scheduledService = new ScheduledServiceBuilder()
                .withTemplate(patientTemplate)
                .build();
        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();

        when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);
        when(person.getAttribute(ConfigConstants.PERSON_PHONE_ATTR)).thenReturn(personAttribute);
        when(personAttribute.getValue()).thenReturn(PHONE_NUMBER);

        callFlowServiceResultsHandlerService.handle(Collections.singletonList(scheduledService),
                new ScheduledExecutionContextBuilder().build());

        verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

        MessagesEvent messagesEvent = messagesEventCaptor.getValue();
        Map<String, Object> params = messagesEvent.getParameters();
        Map<String, Object> additionalParams = (Map<String, Object>) params.get(ADDITIONAL_PARAMS);

        assertThat(messagesEvent.getSubject(), is(CALL_FLOW_INITIATE_CALL_EVENT));
        assertThat(params.get(CONFIG), is(CALLFLOWS_DEFAULT_CONFIG));
        assertThat(params.get(FLOW_NAME), is(CALLFLOWS_DEFAULT_FLOW));
        assertThat(additionalParams.get(PHONE), is(PHONE_NUMBER));
        assertThat(additionalParams.get(ACTOR_ID), is(Integer.toString(scheduledExecutionContext.getActorId())));
        assertThat(additionalParams.get(ACTOR_TYPE), is(scheduledExecutionContext.getActorType()));
        assertThat(additionalParams.get(REF_KEY), is(Integer.toString(scheduledExecutionContext.getGroupId())));
    }
}
