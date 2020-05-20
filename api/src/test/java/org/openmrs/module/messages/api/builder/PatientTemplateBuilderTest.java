package org.openmrs.module.messages.api.builder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;

public class PatientTemplateBuilderTest {

    public static final String DEFAULT_TF_VALUE = "default tf value";
    private Patient patient;
    private Template template;

    @Before
    public void setUp() {
        patient = new Patient(1);
        template = new TemplateBuilder().build();
        TemplateField tf = new TemplateFieldBuilder()
            .withDefaultValue(DEFAULT_TF_VALUE)
            .build();
        template.getTemplateFields().add(tf);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBuilderForNullTemplate() {
        new PatientTemplateBuilder(null, new Patient());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateBuilderForNullPatient() {
        new PatientTemplateBuilder(new Template(), null);
    }

    @Test(expected = Test.None.class /* no exception expected */)
    public void shouldCreateBuilderForNullActor() {
        new PatientTemplateBuilder(new Template(), new Patient());
    }

    @Test
    public void shouldBuildPatientTemplateProperly() {
        PatientTemplate patientTemplate = new PatientTemplateBuilder(template, patient).build();
        Assert.assertNotNull(patientTemplate);
        Assert.assertEquals(patient.getId(), patientTemplate.getPatient().getId());
        Assert.assertEquals("default tf value",
            patientTemplate.getTemplateFieldValues().get(0).getValue());
        Assert.assertEquals(patient.getPerson(), patientTemplate.getActor());
    }
}
