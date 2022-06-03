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

import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;
import org.openmrs.module.messages.api.model.TemplateFieldType;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class TemplateFieldBuilder extends AbstractBuilder<TemplateField> {

    private Integer id;
    private String name;
    private Boolean mandatory;
    private String defaultValue;
    private Template template;
    private TemplateFieldType templateFieldType;
    private Set<TemplateFieldDefaultValue> defaultValues;

    public TemplateFieldBuilder() {
        super();
        id = getInstanceNumber();
        name = "Template field " + id;
        mandatory = false;
        defaultValue = "default value";
        template = new TemplateBuilder().build();
        templateFieldType = TemplateFieldType.SERVICE_TYPE;
        defaultValues = new HashSet<>();
    }

    @Override
    public TemplateField build() {
        TemplateField templateField = new TemplateField();
        templateField.setId(id);
        templateField.setName(name);
        templateField.setMandatory(mandatory);
        templateField.setDefaultValue(defaultValue);
        templateField.setTemplate(template);
        templateField.setTemplateFieldType(templateFieldType);
        templateField.setDefaultValues(defaultValues);
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

    public TemplateFieldBuilder withTemplateFieldType(TemplateFieldType templateFieldType) {
        this.templateFieldType = templateFieldType;
        return this;
    }

    public TemplateFieldBuilder withDefaultValues(Collection<TemplateFieldDefaultValue> defaultValues) {
        this.defaultValues = new HashSet<>(defaultValues);
        return this;
    }
}
