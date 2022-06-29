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
