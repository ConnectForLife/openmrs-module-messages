package org.openmrs.module.messages.web.model;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class PatientTemplatesWrapperTest {

    private static final Integer PATIENT_ID = 2;

    private static final Integer TEMPLATE_ID = 5;

    private static final Integer ACTOR_ID = 3;

    private static final Integer ACTOR_TYPE_ID = 8;

    private static final String UUID = "f08a77d6-f122-438c-aa63-e3a9230780a9";

    private PatientTemplatesWrapper patientTemplatesWrapper;

    private PatientTemplatesWrapper patientTemplatesWrapper2;

    private List<PatientTemplateDTO> patientTemplates;

    private List<PatientTemplateDTO> patientTemplates2;

    @Before
    public void setUp() {
        patientTemplates = Arrays.asList(buildPatientTemplateDTOObject());
        patientTemplates2 = Arrays.asList(buildPatientTemplateDTOObject());
    }

    @Test
    public void shouldCreateInstancesSuccessfully() {
        patientTemplatesWrapper = new PatientTemplatesWrapper(patientTemplates);
        assertThat(patientTemplatesWrapper, is(notNullValue()));
        assertEquals(patientTemplates, patientTemplatesWrapper.getPatientTemplates());

        patientTemplatesWrapper2 = new PatientTemplatesWrapper();
        patientTemplatesWrapper2.setPatientTemplates(patientTemplates2);
        assertThat(patientTemplatesWrapper2, is(notNullValue()));
        assertEquals(patientTemplates2, patientTemplatesWrapper2.getPatientTemplates());

        assertFalse(patientTemplatesWrapper.equals(patientTemplatesWrapper2));
    }

    private PatientTemplateDTO buildPatientTemplateDTOObject() {
        PatientTemplateDTO dto = new PatientTemplateDTO()
                .withActorId(ACTOR_ID)
                .withActorTypeId(ACTOR_TYPE_ID)
                .withPatientId(PATIENT_ID)
                .withTemplateId(TEMPLATE_ID)
                .withUuid(UUID);
        return dto;
    }
}