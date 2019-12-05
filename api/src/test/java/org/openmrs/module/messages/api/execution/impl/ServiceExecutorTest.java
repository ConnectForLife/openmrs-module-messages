package org.openmrs.module.messages.api.execution.impl;

import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.Range;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.messages.api.execution.ExecutionContext;
import org.openmrs.module.messages.api.execution.ExecutionEngine;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServiceExecutorTest {

    private static final Date START_DATE = new Date();
    private static final Date END_DATE = DateUtils.addDays(new Date(), 3);
    private static final Integer PATIENT_ID = 1;

    private ServiceExecutor serviceExecutor;

    @Mock
    private ExecutionEngineManager executionEngineManager;

    @Mock
    private ExecutionEngine executionEngine;

    @Mock
    private PatientTemplate patientTemplate;

    @Mock
    private Patient patient;

    @Captor
    private ArgumentCaptor<ExecutionContext> ecCaptor;

    @Before
    public void setUp() {
        serviceExecutor = new ServiceExecutorImpl(executionEngineManager);
    }

    @Test
    public void shouldExecuteService() throws ExecutionException {
        when(patientTemplate.getServiceQueryType()).thenReturn(ExecutionEngineManager.SQL_KEY);
        when(patientTemplate.getPatient()).thenReturn(patient);
        when(patientTemplate.getActor()).thenReturn(patient);
        when(patient.getPatientId()).thenReturn(PATIENT_ID);
        when(executionEngineManager.getEngine(ExecutionEngineManager.SQL_KEY)).thenReturn(executionEngine);
        Range<Date> dateRange = new Range<>(START_DATE, END_DATE);

        serviceExecutor.execute(patientTemplate, dateRange);

        verify(executionEngine).execute(ecCaptor.capture());
        assertEquals(patientTemplate, ecCaptor.getValue().getPatientTemplate());
        assertEquals(dateRange, ecCaptor.getValue().getDateRange());
    }
}
