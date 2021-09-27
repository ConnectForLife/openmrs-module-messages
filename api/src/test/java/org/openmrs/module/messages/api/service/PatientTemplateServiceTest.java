package org.openmrs.module.messages.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PatientTemplateServiceTest extends BaseTest {

    private PatientTemplate patientTemplate;

    @Before
    public void setUp() {
        patientTemplate = new PatientTemplateBuilder().build();
    }

    @Test
    public void shouldCreatePatientTemplateProperly() {
        Assert.assertEquals("SELECT * FROM template", patientTemplate.getServiceQuery());
    }
}
