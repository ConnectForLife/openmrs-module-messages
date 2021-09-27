package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class PatientTemplateDTOTest {

    private static final Integer ID = 11;

    private static final Integer PATIENT_ID = 22;

    private static final Integer ACTOR_ID = 33;

    private static final Integer ACTOR_TYPE_ID = 44;

    private static final String UUID = "67de2ff7-2067-4ef9-b0f5-f49265d41032";

    private static final Integer TEMPLATE_ID = 55;

    private List<TemplateFieldValueDTO> templateFieldsValues = new ArrayList<>();

    private PatientTemplateDTO patientTemplateDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        patientTemplateDTO = buildPatientTemplateDTOObject();

        assertThat(patientTemplateDTO, is(notNullValue()));
        assertEquals(ID, patientTemplateDTO.getId());
        assertEquals(PATIENT_ID, patientTemplateDTO.getPatientId());
        assertEquals(ACTOR_ID, patientTemplateDTO.getActorId());
        assertEquals(ACTOR_TYPE_ID, patientTemplateDTO.getActorTypeId());
        assertEquals(UUID, patientTemplateDTO.getUuid());
        assertEquals(templateFieldsValues, patientTemplateDTO.getTemplateFieldValues());
        assertEquals(TEMPLATE_ID, patientTemplateDTO.getTemplateId());
    }

    private PatientTemplateDTO buildPatientTemplateDTOObject() {
         PatientTemplateDTO dto = new PatientTemplateDTO()
                .withId(ID)
                .withPatientId(PATIENT_ID)
                .withActorId(ACTOR_ID)
                .withActorTypeId(ACTOR_TYPE_ID)
                .withUuid(UUID)
                .withTemplateFieldValues(templateFieldsValues)
                .withTemplateId(TEMPLATE_ID);
        return dto;
    }
}
