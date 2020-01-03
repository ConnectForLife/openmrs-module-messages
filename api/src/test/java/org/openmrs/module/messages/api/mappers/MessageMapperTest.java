package org.openmrs.module.messages.api.mappers;

import org.junit.Test;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.StaticMappersProvider;
import org.openmrs.module.messages.builder.PatientBuilder;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateBuilder;

import java.util.ArrayList;
import java.util.List;

public class MessageMapperTest {

    private MessageMapper messageMapper = StaticMappersProvider.getMessageMapper();

    private static final int PATIENT_2_ID = 682;
    private static final int PATIENT_1_ID = 328;
    private static final int TEMPLATE_1_ID = 284;
    private static final int TEMPLATE_2_ID = 720;

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPatientTemplatesListIsEmpty() {
        messageMapper.toDto(new ArrayList<>());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfPatientTemplatesHaveDifferentTemplates() {
        List<PatientTemplate> templates = new ArrayList<>();

        Template template1 = new TemplateBuilder()
                .withId(TEMPLATE_1_ID).build();

        Template template2 = new TemplateBuilder()
                .withId(TEMPLATE_2_ID).build();

        templates.add(buildPatientTemplate(PATIENT_1_ID, template1));
        templates.add(buildPatientTemplate(PATIENT_2_ID, template2));

        messageMapper.toDto(templates);
    }

    private PatientTemplate buildPatientTemplate(int patientId, Template baseTemplate) {
        return new PatientTemplateBuilder()
                .withPatient(new PatientBuilder()
                        .withId(patientId)
                        .build())
                .withTemplate(baseTemplate)
                .build();
    }
}
