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

import java.util.ArrayList;
import java.util.List;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public final class PatientTemplateBuilder extends AbstractBuilder<PatientTemplate> {

    private Integer id;
    private Person actor;
    private Relationship actorType;
    private Patient patient;
    private Template template;
    private List<TemplateFieldValue> templateFieldValues = new ArrayList<>();

    public PatientTemplateBuilder() {
        super();
        id = getInstanceNumber();
        actor = new Person(1);
        actorType = new RelationshipBuilder().build();
        patient = new Patient(new Person(1)); //TODO:CFLM-248:Consider adding Patient/Person builder
        template = new TemplateBuilder().build();
    }

    @Override
    public PatientTemplate build() {
        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setId(id);
        patientTemplate.setActor(actor);
        patientTemplate.setActorType(actorType);
        patientTemplate.setPatient(patient);
        patientTemplate.setTemplate(template);
        patientTemplate.setTemplateFieldValues(templateFieldValues);
        return patientTemplate;
    }

    @Override
    public PatientTemplate buildAsNew() {
        return withId(null).build();
    }

    public PatientTemplateBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PatientTemplateBuilder withActor(Person actor) {
        this.actor = actor;
        return this;
    }

    public PatientTemplateBuilder withActorType(Relationship actorType) {
        this.actorType = actorType;
        return this;
    }

    public PatientTemplateBuilder withPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public PatientTemplateBuilder withTemplate(Template template) {
        this.template = template;
        return this;
    }

    public PatientTemplateBuilder withTemplateFieldValues(List<TemplateFieldValue> templateFieldValues) {
        this.templateFieldValues = templateFieldValues;
        return this;
    }
}
