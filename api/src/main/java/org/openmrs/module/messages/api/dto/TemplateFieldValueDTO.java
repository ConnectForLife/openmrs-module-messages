/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.openmrs.BaseOpenmrsObject;

import javax.validation.constraints.NotNull;

/**
 * Represents a template field value DTO
 */
public class TemplateFieldValueDTO extends BaseOpenmrsObject implements DTO {

    private static final long serialVersionUID = -1685385476367160219L;

    private Integer id;

    @NotNull
    private Integer templateFieldId;

    private String value;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public TemplateFieldValueDTO withId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getTemplateFieldId() {
        return templateFieldId;
    }

    public TemplateFieldValueDTO withTemplateFieldId(Integer templateFieldId) {
        this.templateFieldId = templateFieldId;
        return this;
    }

    public String getValue() {
        return value;
    }

    public TemplateFieldValueDTO withValue(String value) {
        this.value = value;
        return this;
    }

    public TemplateFieldValueDTO withUuid(String uuid) {
        setUuid(uuid);
        return this;
    }
}
