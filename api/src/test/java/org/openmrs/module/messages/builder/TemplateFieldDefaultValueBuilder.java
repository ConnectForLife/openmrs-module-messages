/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
