package org.openmrs.module.messages.api.service;

import org.hibernate.SQLQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.messages.api.service.impl.HealthTipServiceImpl;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class HealthTipServiceTest {

    @Mock
    private ConceptService conceptService;

    @Mock
    private DbSessionFactory dbSessionFactory;

    @Mock
    private DbSession dbSession;

    @Mock
    private SQLQuery sqlQuery;

    @InjectMocks
    private HealthTipService healthTipService = new HealthTipServiceImpl();

    private final Patient patient = new Patient(1);

    private final Person actor = new Person(1);

    @Before
    public void setUp() {
        mockStatic(Context.class);

        when(dbSessionFactory.getCurrentSession()).thenReturn(dbSession);
        when(dbSession.createSQLQuery(anyString())).thenReturn(sqlQuery);
        when(sqlQuery.setParameter(any(), any())).thenReturn(sqlQuery);
    }

    @Test
    public void shouldReturnBlankHealthTipConceptToPlay() {
        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getPatientId(), actor.getPersonId(), "HT_PREVENTION");

        assertNull(actual);
        verify(conceptService, times(0)).getConcept(anyInt());
    }

    @Test
    public void shouldReturnBlankHealthTipCategories() {
        String actual = healthTipService.getHealthTipCategories(patient.getPatientId(), actor.getPersonId());

        assertNull(actual);
        verify(conceptService, times(0)).getConcept(anyInt());
    }

    @Test
    public void shouldReturnBlankHealthTipText() {
        String actual = healthTipService.getNextHealthTipText(patient.getPatientId(), actor.getPersonId(), "HT_PREVENTION");

        assertNull(actual);
        verify(conceptService, times(0)).getConcept(anyInt());
    }
}
