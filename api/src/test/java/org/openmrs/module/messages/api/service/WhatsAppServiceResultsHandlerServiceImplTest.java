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
import org.openmrs.module.messages.api.service.impl.WhatsAppServiceResultsHandlerServiceImpl;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.ScheduledExecutionContextBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceParameterBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Context.class})
public class WhatsAppServiceResultsHandlerServiceImplTest {
  private static final String PHONE_NUMBER = "100200300";

  private static final String WHATSAPP_EXPECTED_MESSAGE = "Test message";

  private static final String WHATSAPP_PARSED_TEMPLATE = String.format("{ message:\"%s\" }",
    WHATSAPP_EXPECTED_MESSAGE);

  private static final String WHATSAPP_CONFIG_NAME = "nexmo-whatsapp";

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
  private WhatsAppServiceResultsHandlerServiceImpl whatsAppServiceResultsHandlerService;

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
    when(administrationService.getGlobalProperty(ConfigConstants.WHATSAPP_CONFIG_GP_KEY,
      ConfigConstants.WHATSAPP_CONFIG_GP_DEFAULT_VALUE)).thenReturn(WHATSAPP_CONFIG_NAME);
  }

  @Test
  public void shouldSendEventWithProperEventParamsForWhatsAppVisitReminder() {
    prepareVisitReminderService();

    ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContextBuilder().build();
    when(personService.getPerson(scheduledExecutionContext.getActorId())).thenReturn(person);
    when(notificationTemplateService.parseTemplate(eq(patientTemplate), anyMap())).thenReturn(WHATSAPP_PARSED_TEMPLATE);

    whatsAppServiceResultsHandlerService.handle(Collections.singletonList(scheduledService), scheduledExecutionContext);

    verify(messagesEventService).sendEventMessage(messagesEventCaptor.capture());

    MessagesEvent messagesEvent = messagesEventCaptor.getValue();
    Map<String, Object> params = messagesEvent.getParameters();

    assertThat(messagesEvent.getSubject(), is(SMS_INITIATE_EVENT));
    assertThat(params.get(SmsEventParamConstants.RECIPIENTS), is(Collections.singletonList(PHONE_NUMBER)));
    assertThat(params.get(SmsEventParamConstants.MESSAGE), is(WHATSAPP_EXPECTED_MESSAGE));
    assertThat(params.get(SmsEventParamConstants.CONFIG), is(WHATSAPP_CONFIG_NAME));
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
