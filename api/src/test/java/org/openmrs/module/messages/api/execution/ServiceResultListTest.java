package org.openmrs.module.messages.api.execution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, DateUtil.class})
public class ServiceResultListTest {

  private static final int PATIENT_ID = 1;
  private static final int ACTOR_ID = 2;
  private static final String ACTOR_TYPE = "Caregiver";
  private static final ZonedDateTime START_DATE =
      ZonedDateTime.ofInstant(Instant.ofEpochSecond(1625756392L), ZoneId.of("UTC"));
  private static final ZonedDateTime END_DATE = START_DATE.plusMonths(2);
  private static final String SERVICE_NAME = "TestName";

  private static final List<ZonedDateTime> EXEC_DATES =
      Arrays.asList(
          START_DATE.plusDays(10),
          START_DATE.plusDays(10),
          START_DATE.plusDays(16),
          START_DATE.plusDays(10),
          START_DATE.plusDays(21));

  private static final List<String> MSG_IDS =
      Arrays.asList("ID_0", "ID_1", "ID 2", "ID 3", "abcdef");
  private static final List<Integer> PATIENT_IDS = Arrays.asList(0, 1, 2, 3, 4);
  private static final List<String> CHANNEL_NAMES =
      Arrays.asList("Call", "Call", "Sms", "Call", "Deactivate service");
  private static final List<ServiceStatus> SERVICE_STATUSES =
      Arrays.asList(
          ServiceStatus.FUTURE,
          ServiceStatus.DELIVERED,
          ServiceStatus.PENDING,
          null,
          ServiceStatus.FAILED);
  private static final int EXPECTED_SIZE = 3;
  private static final int EXPECTED_IDENTIFIER_DELIVERED = 1;
  private static final int EXPECTED_IDENTIFIER_PENDING = 2;
  private static final int EXPECTED_IDENTIFIER_FAILED = 4;
  private Range<ZonedDateTime> dateRange;

  @Mock private PatientTemplate patientTemplate;

  @Mock private Template template;

  @Mock private Person actor;

  @Mock private Patient patient;

  @Mock private Person person;

  @Mock private PatientTemplateService patientTemplateService;

  @Mock private PatientService patientService;

  @Mock private CountryPropertyService countryPropertyService;

  @Mock private MessagingGroupService messagingGroupService;

  @Mock private PersonService personService;

  @Before
  public void setUp() throws IllegalAccessException {
    mockStatic(Context.class);
    when(Context.getService(PatientTemplateService.class)).thenReturn(patientTemplateService);
    when(Context.getPatientService()).thenReturn(patientService);
    when(Context.getService(CountryPropertyService.class)).thenReturn(countryPropertyService);
    when(Context.getRegisteredComponent(
            MessagesConstants.MESSAGING_GROUP_SERVICE, MessagingGroupService.class))
        .thenReturn(messagingGroupService);
    when(Context.getPersonService()).thenReturn(personService);

    // Sets DateUtil's clock to predefined and fixed point in time
    PowerMockito.field(DateUtil.class, "clock")
        .set(null, Clock.fixed(START_DATE.toInstant(), START_DATE.getZone()));

    dateRange = new Range<>(START_DATE, END_DATE);
  }

  @Test
  public void shouldParseListAndOverrideFutureEventsIfWereExecuted() {
    when(template.getName()).thenReturn(SERVICE_NAME);
    when(patientTemplate.getActor()).thenReturn(actor);
    when(patientTemplate.getPatient()).thenReturn(patient);
    when(patientTemplate.getTemplate()).thenReturn(template);
    when(patientTemplate.getActorTypeAsString()).thenReturn(ACTOR_TYPE);
    when(patient.getPatientId()).thenReturn(PATIENT_ID);
    when(actor.getPersonId()).thenReturn(ACTOR_ID);
    dateRange = new Range<>(START_DATE, END_DATE);

    ServiceResultList resultList =
        ServiceResultList.createList(buildRows(), patientTemplate, dateRange);

    assertEquals(PATIENT_ID, resultList.getPatientId().intValue());
    assertEquals(ACTOR_ID, resultList.getActorId().intValue());
    assertEquals(ACTOR_TYPE, resultList.getActorType());
    assertEquals(START_DATE, resultList.getStartDate());
    assertEquals(END_DATE, resultList.getEndDate());

    assertEquals(EXPECTED_SIZE, resultList.getResults().size());
    assertResultEntities(resultList, EXPECTED_IDENTIFIER_DELIVERED, 0);
    assertResultEntities(resultList, EXPECTED_IDENTIFIER_PENDING, 1);
    assertResultEntities(resultList, EXPECTED_IDENTIFIER_FAILED, 2);
  }

  @Test
  public void shouldParseServiceResultListFromTemplateQuery() {
    when(patientTemplateService.findOneByCriteria(any())).thenReturn(patientTemplate);
    when(personService.getPerson(any())).thenReturn(person);
    when(countryPropertyService.getCountryPropertyValue(
            Mockito.any(Concept.class), Mockito.eq(ConfigConstants.BEST_CONTACT_TIME_KEY)))
        .thenReturn(Optional.of("10:00"));
    when(patientTemplate.getActorTypeAsString()).thenReturn(ACTOR_TYPE);

    List<ServiceResultList> serviceResultLists =
        ServiceResultList.createList(buildRows(), template, dateRange);

    assertServiceResults(serviceResultLists);
  }

  private void assertServiceResults(List<ServiceResultList> serviceResultLists) {
    assertEquals(5, serviceResultLists.size());

    for (int i = 0; i < serviceResultLists.size(); i++) {
      assertEquals(i, serviceResultLists.get(i).getPatientId().intValue());
      assertEquals(i, serviceResultLists.get(i).getActorId().intValue());
      assertEquals(ACTOR_TYPE, serviceResultLists.get(i).getActorType());
      assertEquals(START_DATE, serviceResultLists.get(i).getStartDate());
      assertEquals(END_DATE, serviceResultLists.get(i).getEndDate());
    }
  }

  private void assertResultEntities(
      ServiceResultList resultList, int expectedIdentifier, int actualIdentifier) {
    ServiceResult result = resultList.getResults().get(actualIdentifier);
    assertEquals(EXEC_DATES.get(expectedIdentifier), result.getExecutionDate());
    assertEquals(MSG_IDS.get(expectedIdentifier), result.getMessageId());
    assertEquals(CHANNEL_NAMES.get(expectedIdentifier), result.getChannelType());
  }

  private List<Map<String, Object>> buildRows() {
    List<Map<String, Object>> rows = new ArrayList<>();
    for (int i = 0; i < EXEC_DATES.size(); i++) {
      Map<String, Object> row = new HashMap<>();

      row.put(ServiceResult.EXEC_DATE_ALIAS, DateUtil.toDate(EXEC_DATES.get(i)));
      row.put(ServiceResult.MSG_ID_ALIAS, MSG_IDS.get(i));
      row.put(ServiceResult.CHANNEL_NAME_ALIAS, CHANNEL_NAMES.get(i));
      row.put(
          ServiceResult.STATUS_COL_ALIAS,
          SERVICE_STATUSES.get(i) == null ? null : SERVICE_STATUSES.get(i).toString());
      row.put(ServiceResult.PATIENT_ID_ALIAS, PATIENT_IDS.get(i));

      rows.add(row);
    }

    return rows;
  }
}
