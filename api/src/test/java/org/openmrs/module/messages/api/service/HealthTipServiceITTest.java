package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.PatientService;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HealthTipServiceITTest extends ContextSensitiveTest {

    private static final String DATA_SET_PATH = "datasets/healthTipTestData/";

    @Autowired
    private PatientService patientService;

    @Autowired
    private ConceptService conceptService;

    @Autowired
    @Qualifier("messages.templateService")
    private TemplateService templateService;

    @Autowired
    @Qualifier("messages.healthTipService")
    private HealthTipService healthTipService;

    private Patient patient;

    @Before
    public void setUp() throws Exception {
        executeDataSet(DATA_SET_PATH + "BasicDataSet.xml");
        executeDataSet(DATA_SET_PATH + "HealthTipConceptsDataSet.xml");
        patient = patientService.getPatient(100);
    }

    @Test
    public void shouldReturnFirstHealthTipConceptFromSecondCategory() throws Exception {
        executeDataSet(DATA_SET_PATH + "HealthTipOneResponseDataSet.xml");

        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getId(), patient.getId(), "");

        assertNotNull(actual);
        assertEquals((Integer) 201, actual.getConceptId());
        assertEquals("tb_sideeffects_001", actual.getName().getName());
    }

    @Test
    public void shouldReturnFirstHealthTipConceptFromThirdCategory() throws Exception {
        executeDataSet(DATA_SET_PATH + "HealthTipTwoResponsesDataSet.xml");

        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getId(), patient.getId(), "");

        assertNotNull(actual);
        assertEquals((Integer) 301, actual.getConceptId());
        assertEquals("tb_treatment_001", actual.getName().getName());
    }

    @Test
    public void shouldReturnSecondHealthTipConceptFromFirstCategory() throws Exception {
        executeDataSet(DATA_SET_PATH + "HealthTipThreeResponsesDataSet.xml");

        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getId(), patient.getId(), "");

        assertNotNull(actual);
        assertEquals((Integer) 102, actual.getConceptId());
        assertEquals("tb_prevention_002", actual.getName().getName());
    }

    @Test
    public void shouldReturnFirstHealthTipConceptFromFirstCategoryWhenAllHealthTipsExhausted() throws Exception {
        executeDataSet(DATA_SET_PATH + "HealthTipAllResponsesDataSet.xml");

        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getId(), patient.getId(), "");

        assertNotNull(actual);
        assertEquals((Integer) 101, actual.getConceptId());
        assertEquals("tb_prevention_001", actual.getName().getName());
    }

    @Test
    public void shouldReturnSecondHealthTipConceptFromFirstCategoryWhenWeLookingForHealthTipFromParticularCategory() throws Exception {
        executeDataSet(DATA_SET_PATH + "HealthTipOneResponseDataSet.xml");

        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getId(), patient.getId(), "HT_PREVENTION");

        assertNotNull(actual);
        assertEquals((Integer) 102, actual.getConceptId());
        assertEquals("tb_prevention_002", actual.getName().getName());
    }

    @Test
    public void shouldReturnSecondHealthTipConceptFromSecondCategoryWhenWeLookingForHealthTipFromParticularCategory() throws Exception {
        executeDataSet(DATA_SET_PATH + "HealthTipTwoResponsesDataSet.xml");

        Concept actual = healthTipService.getNextHealthTipToPlay(patient.getId(), patient.getId(), "HT_SIDE_EFFECTS");

        assertNotNull(actual);
        assertEquals((Integer) 202, actual.getConceptId());
        assertEquals("tb_sideeffects_002", actual.getName().getName());
    }
}
