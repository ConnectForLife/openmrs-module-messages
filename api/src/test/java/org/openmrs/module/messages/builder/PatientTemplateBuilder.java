package org.openmrs.module.messages.builder;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.User;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.util.ArrayList;
import java.util.List;

public final class PatientTemplateBuilder extends AbstractBuilder<PatientTemplate> {

    private static final int DEFAULT_ACTOR_ID = 1;
    private static final int DEFAULT_PATIENT_ID = 2;

    private Integer id;
    private Person actor;
    private Relationship actorType;
    private Patient patient;
    private Template template;
    private List<TemplateFieldValue> templateFieldValues = new ArrayList<>();
    private User creator;

    public PatientTemplateBuilder() {
        super();
        id = getInstanceNumber();
        actor = new PersonBuilder().withId(DEFAULT_ACTOR_ID).build();
        actorType = new RelationshipBuilder().build();
        patient = new PatientBuilder().withId(DEFAULT_PATIENT_ID).build();
        template = new TemplateBuilder().build();
        creator = new User();
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
        patientTemplate.setCreator(creator);
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

    public PatientTemplateBuilder withRelationship(Relationship relationship) {
        this.actorType = relationship;
        return this;
    }
}
