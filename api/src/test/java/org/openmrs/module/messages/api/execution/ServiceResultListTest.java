package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.PatientService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class ServiceResultListTest {

    private static final int PATIENT_ID = 1;
    private static final int ACTOR_ID = 2;
    private static final String ACTOR_TYPE = "Caregiver";
    private static final int SERVICE_ID = 11;
    private static final Date START_DATE = new Date();
    private static final Date END_DATE = DateUtils.addMonths(new Date(), 2);
    private static final String SERVICE_NAME = "TestName";

    private static final List<Date> EXEC_DATES = Arrays.asList(
        DateUtils.addDays(new Date(), 10),
        DateUtils.addDays(new Date(), 10),
        DateUtils.addDays(new Date(), 16),
        DateUtils.addDays(new Date(), 10),
        DateUtils.addDays(new Date(), 21)
    );

    private static final List<String> MSG_IDS = Arrays.asList("ID_0", "ID_1", "ID 2", "ID 3", "abcdef");
    private static final List<Integer> PATIENT_IDS = Arrays.asList(0, 1, 2, 3, 4);
    private static final List<String> CHANNEL_NAMES = Arrays.asList("Call", "Call", "Sms",
            "Call", "Deactivate service");
    private static final List<ServiceStatus> SERVICE_STATUSES = Arrays.asList(
            ServiceStatus.FUTURE,
            ServiceStatus.DELIVERED,
            ServiceStatus.PENDING,
            null,
            ServiceStatus.FAILED
    );
    private static final int EXPECTED_SIZE = 3;
    private static final int EXPECTED_IDENTIFIER_DELIVERED = 1;
    private static final int EXPECTED_IDENTIFIER_PENDING = 2;
    private static final int EXPECTED_IDENTIFIER_FAILED = 4;
    private Range<Date> dateRange;

    @Mock
    private PatientTemplate patientTemplate;

    @Mock
    private Template template;

    @Mock
    private Person actor;

    @Mock
    private Patient patient;

    @Mock
    private PatientTemplateService patientTemplateService;

    @Mock
    private PatientService patientService;

    @Mock
    private AdministrationService administrationService;

    @Mock
    private MessagingGroupService messagingGroupService;

    @Before
    public void setUp() {
        mockStatic(Context.class);
        when(Context.getService(PatientTemplateService.class)).thenReturn(patientTemplateService);
        when(Context.getPatientService()).thenReturn(patientService);
        when(Context.getAdministrationService()).thenReturn(administrationService);
        when(Context.getRegisteredComponent(MessagesConstants.MESSAGING_GROUP_SERVICE, MessagingGroupService.class))
                .thenReturn(messagingGroupService);

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
        when(patientTemplate.getServiceId()).thenReturn(SERVICE_ID);
        dateRange = new Range<>(START_DATE, END_DATE);

        ServiceResultList resultList = ServiceResultList.createList(buildRows(), patientTemplate, dateRange);

        assertEquals(PATIENT_ID, resultList.getPatientId().intValue());
        assertEquals(ACTOR_ID, resultList.getActorId().intValue());
        assertEquals(ACTOR_TYPE, resultList.getActorType());
        assertEquals(SERVICE_ID, resultList.getServiceId().intValue());
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
        when(patientService.getPatient(any())).thenReturn(patient);
        when(administrationService.getGlobalProperty(ConfigConstants.BEST_CONTACT_TIME_KEY)).thenReturn("10:00");
        when(patientTemplate.getActorTypeAsString()).thenReturn(ACTOR_TYPE);
        when(patientTemplate.getServiceId()).thenReturn(SERVICE_ID);

        List<ServiceResultList> serviceResultLists = ServiceResultList.createList(buildRows(), template, dateRange);

        assertServiceResults(serviceResultLists);
    }

    private void assertServiceResults(List<ServiceResultList> serviceResultLists) {
        assertEquals(5, serviceResultLists.size());

        for (int i = 0; i < serviceResultLists.size(); i++) {
            assertEquals(i, serviceResultLists.get(i).getPatientId().intValue());
            assertEquals(i, serviceResultLists.get(i).getActorId().intValue());
            assertEquals(ACTOR_TYPE, serviceResultLists.get(i).getActorType());
            assertEquals(SERVICE_ID, serviceResultLists.get(i).getServiceId().intValue());
            assertEquals(START_DATE, serviceResultLists.get(i).getStartDate());
            assertEquals(END_DATE, serviceResultLists.get(i).getEndDate());
        }
    }

    private void assertResultEntities(ServiceResultList resultList, int expectedIdentifier, int actualIdentifier) {
        ServiceResult result = resultList.getResults().get(actualIdentifier);
        assertEquals(EXEC_DATES.get(expectedIdentifier), result.getExecutionDate());
        assertEquals(MSG_IDS.get(expectedIdentifier), result.getMessageId());
        assertEquals(CHANNEL_NAMES.get(expectedIdentifier),
            result.getChannelType());
    }

    private List<Map<String, Object>> buildRows() {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 0; i < EXEC_DATES.size(); i++) {
            Map<String, Object> row = new HashMap<>();

            row.put(ServiceResult.EXEC_DATE_ALIAS, EXEC_DATES.get(i));
            row.put(ServiceResult.MSG_ID_ALIAS, MSG_IDS.get(i));
            row.put(ServiceResult.CHANNEL_NAME_ALIAS, CHANNEL_NAMES.get(i));
            row.put(ServiceResult.STATUS_COL_ALIAS, SERVICE_STATUSES.get(i) == null ? null
                    : SERVICE_STATUSES.get(i).toString());
            row.put(ServiceResult.PATIENT_ID_ALIAS, PATIENT_IDS.get(i));

            rows.add(row);
        }

        return rows;
    }
}
