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

import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDefaultValueDTO;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.ArrayList;
import java.util.List;

public final class TemplateFieldDTOBuilder extends AbstractBuilder<TemplateFieldDTO> {

    private Integer id;

    private String name;

    private Boolean mandatory;

    private String defaultValue;

    private String type;

    private String uuid;

    private List<TemplateFieldDefaultValueDTO> defaultValues = new ArrayList<>();

    public TemplateFieldDTOBuilder() {
        super();
        TemplateField temmplateField = new TemplateFieldBuilder().build();
        id = temmplateField.getId();
        name = temmplateField.getName();
        mandatory = temmplateField.getMandatory();
        defaultValue = temmplateField.getDefaultValue();
        type = temmplateField.getTemplateFieldType().name();
        uuid = temmplateField.getUuid();
    }

    @Override
    public TemplateFieldDTO build() {
        return new TemplateFieldDTO()
                .setId(id)
                .setName(name)
                .setMandatory(mandatory)
                .setDefaultValue(defaultValue)
                .setType(type)
                .setUuid(uuid)
                .setDefaultValues(defaultValues);
    }

    @Override
    public TemplateFieldDTO buildAsNew() {
        return withId(null).withUuid(null).build();
    }

    public TemplateFieldDTOBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public TemplateFieldDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public TemplateFieldDTOBuilder withMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
        return this;
    }

    public TemplateFieldDTOBuilder withDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public TemplateFieldDTOBuilder withType(String type) {
        this.type = type;
        return this;
    }

    public TemplateFieldDTOBuilder withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public TemplateFieldDTOBuilder withDefaultValues(List<TemplateFieldDefaultValueDTO> defaultValues) {
        this.defaultValues = defaultValues;
        return this;
    }
}
