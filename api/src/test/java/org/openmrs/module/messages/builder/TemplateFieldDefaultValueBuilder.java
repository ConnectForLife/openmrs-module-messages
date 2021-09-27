package org.openmrs.module.messages.builder;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;

import java.util.Collections;

public class TemplateFieldDefaultValueBuilder extends AbstractBuilder<TemplateFieldDefaultValue> {

    private Integer id;
    private RelationshipType relationshipType;
    private RelationshipTypeDirection direction;
    private TemplateField templateField;
    private String defaultValue;

    public TemplateFieldDefaultValueBuilder() {
        id = getInstanceNumber();
        relationshipType = new RelationshipTypeBuilder().build();
        direction = RelationshipTypeDirection.A;
        defaultValue = "Default value for related actor";
    }

    @Override
    public TemplateFieldDefaultValue build() {
        TemplateFieldDefaultValue templateFieldDefaultValue = new TemplateFieldDefaultValue();
        if (templateField == null) {
            templateField = new TemplateFieldBuilder()
                    .withDefaultValues(Collections.singletonList(templateFieldDefaultValue))
                    .build();
        }
        templateFieldDefaultValue.setId(id);
        templateFieldDefaultValue.setTemplateField(templateField);
        templateFieldDefaultValue.setDefaultValue(defaultValue);
        templateFieldDefaultValue.setDirection(direction);
        templateFieldDefaultValue.setRelationshipType(relationshipType);
        return templateFieldDefaultValue;
    }

    @Override
    public TemplateFieldDefaultValue buildAsNew() {
        return withId(null).build();
    }

    public TemplateFieldDefaultValueBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateFieldDefaultValueBuilder withRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
        return this;
    }

    public TemplateFieldDefaultValueBuilder withDirection(RelationshipTypeDirection direction) {
        this.direction = direction;
        return this;
    }

    public TemplateFieldDefaultValueBuilder withTemplateField(TemplateField templateField) {
        this.templateField = templateField;
        return this;
    }

    public TemplateFieldDefaultValueBuilder withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }
}
