package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public final class TemplateFieldValueBuilder extends AbstractBuilder<TemplateFieldValue> {

    private Integer id;
    private String value;
    private TemplateField templateField;
    private PatientTemplate patientTemplate;

    public TemplateFieldValueBuilder() {
        super();
        id = getInstanceNumber();
        value = "test value";
        templateField = new TemplateFieldBuilder().build();
        patientTemplate = new PatientTemplateBuilder().build();

    }

    @Override
    public TemplateFieldValue build() {
        TemplateFieldValue templateFieldValue = new TemplateFieldValue();
        templateFieldValue.setId(id);
        templateFieldValue.setValue(value);
        templateFieldValue.setTemplateField(templateField);
        templateFieldValue.setPatientTemplate(patientTemplate);
        return templateFieldValue;
    }

    @Override
    public TemplateFieldValue buildAsNew() {
        return withId(null).build();
    }

    public TemplateFieldValueBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateFieldValueBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public TemplateFieldValueBuilder withTemplateField(TemplateField templateField) {
        this.templateField = templateField;
        return this;
    }

    public TemplateFieldValueBuilder withPatientTemplate(PatientTemplate patientTemplate) {
        this.patientTemplate = patientTemplate;
        return this;
    }
}
