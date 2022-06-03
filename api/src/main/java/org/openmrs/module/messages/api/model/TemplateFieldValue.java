/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "messages.TemplateFieldValue")
@Table(name = "messages_template_field_value")
public class TemplateFieldValue extends AbstractBaseOpenmrsData {

    private static final long serialVersionUID = 1398231018730368434L;

    @Id
    @GeneratedValue
    @Column(name = "messages_template_field_value_id")
    private Integer id;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "template_field_id", nullable = false)
    private TemplateField templateField;

    @ManyToOne
    @JoinColumn(name = "patient_template_id", nullable = false)
    private PatientTemplate patientTemplate;

    public TemplateFieldValue() {
    }

    public TemplateFieldValue(String value, TemplateField templateField,
                              PatientTemplate patientTemplate) {
        this.value = value;
        this.templateField = templateField;
        this.patientTemplate = patientTemplate;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public TemplateField getTemplateField() {
        return templateField;
    }

    public void setTemplateField(TemplateField templateField) {
        this.templateField = templateField;
    }

    public PatientTemplate getPatientTemplate() {
        return patientTemplate;
    }

    public void setPatientTemplate(PatientTemplate patientTemplate) {
        this.patientTemplate = patientTemplate;
    }

    @Override
    public String toString() {
        return "TemplateFieldValue#" + id;
    }
}
