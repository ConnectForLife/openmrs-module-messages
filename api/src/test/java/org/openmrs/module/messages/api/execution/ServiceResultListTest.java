package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.DateRange;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
        DateUtils.addDays(new Date(), 16),
        DateUtils.addDays(new Date(), 21)
    );

    private static final List<String> MSG_IDS = Arrays.asList("ID_1", "ID 2", "abcdef");
    private static final List<String> CHANNEL_NAMES = Arrays.asList("Call", "Sms", "Deactivate service");
    private static final List<ServiceStatus> SERVICE_STATUSES = Arrays.asList(
            ServiceStatus.DELIVERED,
            ServiceStatus.PENDING,
            ServiceStatus.FAILED
    );

    @Mock
    private PatientTemplate patientTemplate;

    @Mock
    private Template template;

    @Mock
    private Person actor;

    @Mock
    private Patient patient;

    @Test
    public void shouldParseList() {
        when(template.getName()).thenReturn(SERVICE_NAME);
        when(patientTemplate.getActor()).thenReturn(actor);
        when(patientTemplate.getPatient()).thenReturn(patient);
        when(patientTemplate.getTemplate()).thenReturn(template);
        when(patientTemplate.getActorTypeAsString()).thenReturn(ACTOR_TYPE);
        when(patient.getPatientId()).thenReturn(PATIENT_ID);
        when(actor.getPersonId()).thenReturn(ACTOR_ID);
        when(patientTemplate.getServiceId()).thenReturn(SERVICE_ID);
        DateRange dateRange = new DateRange(START_DATE, END_DATE);

        ServiceResultList resultList = ServiceResultList.createList(buildRows(), patientTemplate, dateRange);

        assertEquals(PATIENT_ID, resultList.getPatientId().intValue());
        assertEquals(ACTOR_ID, resultList.getActorId().intValue());
        assertEquals(ACTOR_TYPE, resultList.getActorType());
        assertEquals(SERVICE_ID, resultList.getServiceId().intValue());
        assertDate(START_DATE, resultList.getStartDate());
        assertDate(END_DATE, resultList.getEndDate());

        assertEquals(EXEC_DATES.size(), resultList.getResults().size());
        for (int i = 0; i < EXEC_DATES.size(); i++) {
            ServiceResult result = resultList.getResults().get(i);

            assertEquals(EXEC_DATES.get(i), result.getExecutionDate());
            assertEquals(MSG_IDS.get(i), result.getMessageId());
            assertEquals(ChannelType.fromName(CHANNEL_NAMES.get(i).toUpperCase()),
                result.getChannelType());
        }
    }

    private List<Map<String, Object>> buildRows() {
        List<Map<String, Object>> rows = new ArrayList<>();
        for (int i = 0; i < EXEC_DATES.size(); i++) {
            Map<String, Object> row = new HashMap<>();

            row.put(ServiceResult.EXEC_DATE_ALIAS, EXEC_DATES.get(i));
            row.put(ServiceResult.MSG_ID_ALIAS, MSG_IDS.get(i));
            row.put(ServiceResult.CHANNEL_NAME_ALIAS, CHANNEL_NAMES.get(i));
            row.put(ServiceResult.STATUS_COL_ALIAS, SERVICE_STATUSES.get(i).toString());

            rows.add(row);
        }

        return rows;
    }

    private void assertDate(Date expected, Date other) {
        assertEquals(expected.getYear(), other.getYear());
        assertEquals(expected.getMonth(), other.getMonth());
        assertEquals(expected.getDate(), other.getDate());
        assertEquals(0, other.getHours());
        assertEquals(0, other.getMinutes());
        assertEquals(0, other.getSeconds());
    }
}
