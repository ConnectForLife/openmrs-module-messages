package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.service.impl.AdherenceFeedbackServiceImpl;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class AdherenceFeedbackServiceTest {

    @Mock
    private PersonService personService;

    @Mock
    private PatientService patientService;

    @InjectMocks
    private AdherenceFeedbackService adherenceFeedbackService = new AdherenceFeedbackServiceImpl();

    @Before
    public void setUp() {
        mockStatic(Context.class);

        when(Context.getPatientService()).thenReturn(patientService);
        when(Context.getPersonService()).thenReturn(personService);
    }

    @Test
    public void shouldGetAdherenceFeedbackWhenPatientAndActorAreNotBlank() {
        when(personService.getPerson(1)).thenReturn(new Person(1));
        when(patientService.getPatient(1)).thenReturn(new Patient(1));

        adherenceFeedbackService.getAdherenceFeedback(1, 1);

        verify(personService).getPerson(anyInt());
        verify(patientService).getPatient(anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPatientIsBlank() {
        when(personService.getPerson(1)).thenReturn(new Person(1));
        when(patientService.getPatient(1)).thenReturn(null);

        adherenceFeedbackService.getAdherenceFeedback(1, 1);

        verify(personService).getPerson(anyInt());
        verify(patientService).getPatient(anyInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenActorIsBlank() {
        when(personService.getPerson(1)).thenReturn(null);
        when(patientService.getPatient(1)).thenReturn(new Patient(1));

        adherenceFeedbackService.getAdherenceFeedback(1, 1);

        verify(personService).getPerson(anyInt());
        verify(patientService).getPatient(anyInt());
    }

}
