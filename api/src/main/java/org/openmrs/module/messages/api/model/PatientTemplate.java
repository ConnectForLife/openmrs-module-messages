/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.util.FieldDateUtil;
import org.openmrs.module.messages.api.util.PatientTemplateFieldUtil;
import validate.annotation.ValidPatientTemplate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.openmrs.module.messages.api.constants.MessagesConstants.PATIENT_DEFAULT_ACTOR_TYPE;

@Entity(name = "messages.PatientTemplate")
@Table(name = "messages_patient_template")
@ValidPatientTemplate
public class PatientTemplate extends AbstractBaseOpenmrsData {

    private static final long serialVersionUID = 4808798852617376186L;

    @Id
    @GeneratedValue
    @Column(name = "messages_patient_template_id")
    private Integer id;

    @OneToOne
    @JoinColumn(name = "actor_id", nullable = false)
    private Person actor;

    @OneToOne
    @JoinColumn(name = "actor_type")
    private Relationship actorType;

    @Column(name = "service_query", columnDefinition = "text")
    private String serviceQuery;

    @Column(name = "service_query_type")
    private String serviceQueryType;

    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientTemplate", orphanRemoval = true)
    private List<TemplateFieldValue> templateFieldValues = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    public PatientTemplate() { }

    public PatientTemplate(Person actor, Relationship actorType, Patient patient,
                           List<TemplateFieldValue> templateFieldValues, Template template) {
        this.actor = actor;
        this.actorType = actorType;
        this.patient = patient;
        this.templateFieldValues = templateFieldValues;
        this.template = template;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "PatientTemplate#" + id;
    }

    public Person getActor() {
        return actor;
    }

    public void setActor(Person actor) {
        this.actor = actor;
    }

    public Relationship getActorType() {
        return actorType;
    }

    public void setActorType(Relationship actorType) {
        this.actorType = actorType;
    }

    public String getServiceQuery() {
        return StringUtils.isBlank(serviceQuery) ? getTemplate().getServiceQuery() : serviceQuery;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public List<TemplateFieldValue> getTemplateFieldValues() {
        return templateFieldValues;
    }

    public void setTemplateFieldValues(List<TemplateFieldValue> templateFieldValues) {
        this.templateFieldValues = templateFieldValues;
    }

    public String getServiceQueryType() {
        return StringUtils.isBlank(serviceQueryType) ? getTemplate().getServiceQueryType() : serviceQueryType;
    }

    @Transient
    public Integer getServiceId() {
        return 0; // TODO
    }

    @Transient
    public Date getStartOfMessages() {
        return FieldDateUtil.getStartDate(getTemplateFieldValues());
    }

    @Transient
    public Date getEndOfMessages() {
        return FieldDateUtil.getEndDate(getTemplateFieldValues(), getTemplate().getName());
    }

    @Transient
    public boolean isDeactivated() {
        return PatientTemplateFieldUtil.isDeactivated(this);
    }

    @Transient
    public String getActorTypeAsString() {
        return getActorType() == null ? PATIENT_DEFAULT_ACTOR_TYPE :
                getPatient().getId().equals(actorType.getPersonA().getId()) ?
                        actorType.getRelationshipType().getbIsToA() :
                        actorType.getRelationshipType().getaIsToB();
    }
}
