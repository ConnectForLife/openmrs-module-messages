package org.openmrs.module.messages.builder;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;

public final class TemplateFieldBuilder extends AbstractBuilder<TemplateField> {

    private Integer id;
    private String name;
    private Boolean mandatory;
    private String defaultValue;
    private Template template;
    private Concept valueConcept;

    public TemplateFieldBuilder() {
        super();
        id = getInstanceNumber();
        name = "Template field " + id;
        mandatory = false;
        defaultValue = "default value";
        template = new TemplateBuilder().build();
        valueConcept = new Concept(1); //TODO:CFLM-248:Consider adding concept builder
    }

    @Override
    public TemplateField build() {
        TemplateField templateField = new TemplateField();
        templateField.setId(id);
        templateField.setName(name);
        templateField.setMandatory(mandatory);
        templateField.setDefaultValue(defaultValue);
        templateField.setTemplate(template);
        templateField.setValueConcept(valueConcept);
        return templateField;
    }

    @Override
    public TemplateField buildAsNew() {
        return withId(null).build();
    }

    public TemplateFieldBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateFieldBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TemplateFieldBuilder withMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public TemplateFieldBuilder withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TemplateFieldBuilder withTemplate(Template template) {
        this.template = template;
        return this;
    }

    public TemplateFieldBuilder withValueConcept(Concept valueConcept) {
        this.valueConcept = valueConcept;
        return this;
    }
}
