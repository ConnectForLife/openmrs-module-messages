/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.builder;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.ActorUtil;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class PatientTemplateBuilder implements Builder<PatientTemplate> {

    private Template template;
    private Actor actor;
    private Patient patient;

    public PatientTemplateBuilder(Template template, Patient patient) {
        this.template = template;
        this.patient = patient;
        validateFields();
    }

    public PatientTemplateBuilder(Template template, Actor actor, Patient patient) {
        this.template = template;
        this.actor = actor;
        this.patient = patient;
        validateFields();
    }

    @Override
    public PatientTemplate build() {
        return actor == null ? buildForPatient() : buildForActor();
    }

    private PatientTemplate buildForPatient() {
        List<TemplateFieldValue> tfvList = new ArrayList<>(template.getTemplateFields().size());
        PatientTemplate patientTemplate = new PatientTemplate(patient.getPerson(), null, patient, tfvList, template);
        for (TemplateField tf : template.getTemplateFields()) {
            tfvList.add(new TemplateFieldValue(getDefaultValue(tf), tf, patientTemplate));
        }
        return patientTemplate;
    }

    private String getDefaultValue(TemplateField tf) {
        String defaultValue = tf.getDefaultValue();
        if (TemplateFieldType.START_OF_MESSAGES == tf.getTemplateFieldType() && StringUtils.isBlank(defaultValue)) {
            defaultValue = DateUtil.formatToServerSideDateTime(DateUtil.now());
        }
        return defaultValue;
    }

    private PatientTemplate buildForActor() {
        List<TemplateFieldValue> tfvList = new ArrayList<>();
        PatientTemplate patientTemplate =
                new PatientTemplate(ActorUtil.getActorPerson(actor), actor.getRelationship(), patient, tfvList, template);
        for (TemplateField tf : template.getTemplateFields()) {
            if (!ActorUtil.isActorPatient(actor, patient.getId())) {
                tfvList.add(new TemplateFieldValue(getDefaultValueForActor(tf, actor), tf, patientTemplate));
            }
        }
        return patientTemplate;
    }

    private String getDefaultValueForActor(TemplateField tf, Actor actor) {
        final String defaultValue;

        if (TemplateFieldType.START_OF_MESSAGES == tf.getTemplateFieldType()) {
            defaultValue = DateUtil.formatToServerSideDateTime(DateUtil.now());
        } else {
            defaultValue = ofNullable(tf.getDefaultValue(actor)).orElseGet(tf::getDefaultValue);
        }

        return defaultValue;
    }

    private void validateFields() throws IllegalArgumentException {
        if (patient == null) {
            throw new IllegalArgumentException("Patient cannot be null");
        }

        if (template == null) {
            throw new IllegalArgumentException("Template cannot be null");
        }
    }
}
