package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.util.Collections;
import java.util.List;

public final class TemplateFieldValueBuilder extends AbstractBuilder<TemplateFieldValue> {

    private Integer id;
    private String value;
    private TemplateField templateField;
    private List<PatientTemplate> patientTemplates;

    public TemplateFieldValueBuilder() {
        super();
        id = getInstanceNumber();
        value = "test value";
        templateField = new TemplateFieldBuilder().build();
        patientTemplates = Collections.emptyList();

    }

    @Override
    public TemplateFieldValue build() {
        TemplateFieldValue templateFieldValue = new TemplateFieldValue();
        templateFieldValue.setId(id);
        templateFieldValue.setValue(value);
        templateFieldValue.setTemplateField(templateField);
        templateFieldValue.setPatientTemplates(patientTemplates);
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

    public TemplateFieldValueBuilder withPatientTemplates(List<PatientTemplate> patientTemplates) {
        this.patientTemplates = patientTemplates;
        return this;
    }
}
