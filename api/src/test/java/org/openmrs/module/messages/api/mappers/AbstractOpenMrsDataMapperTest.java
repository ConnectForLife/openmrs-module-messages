package org.openmrs.module.messages.api.mappers;

import org.junit.Test;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractOpenMrsDataMapperTest {

    @Test
    public void shouldVoidPatientTemplate() {
        PatientTemplate patientTemplate = createTestPatientTemplate();
        assertFalse(patientTemplate.getVoided());

        AbstractOpenMrsDataMapper<PatientTemplateDTO, PatientTemplate> mapper = new PatientTemplateMapper();
        mapper.doSafeDelete(patientTemplate);

        assertTrue(patientTemplate.getVoided());
    }

    private PatientTemplate createTestPatientTemplate() {
        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setId(100);
        patientTemplate.setVoided(false);
        return patientTemplate;
    }
}


