package org.openmrs.module.messages.builder;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;

public final class PatientTemplateBuilder extends AbstractBuilder<PatientTemplate> {
    
    private Integer id;
    private Person actor;
    private Relationship actorType;
    private String serviceQuery;
    private String serviceQueryType;
    private Patient patient;
    private Template template;
    
    public PatientTemplateBuilder() {
        super();
        id = getInstanceNumber();
        actor = new Person(1);
        actorType = new Relationship();
        serviceQuery = "SELECT * FROM SERVICE";
        serviceQueryType = "SQL";
        patient = new Patient(new Person(1)); //TODO:CFLM-248:Consider adding Patient/Person builder
        template = new TemplateBuilder().build();
    }
    
    @Override
    public PatientTemplate build() {
        PatientTemplate patientTemplate = new PatientTemplate();
        patientTemplate.setId(id);
        patientTemplate.setActor(actor);
        patientTemplate.setActorType(actorType);
        patientTemplate.setServiceQuery(serviceQuery);
        patientTemplate.setServiceQueryType(serviceQueryType);
        patientTemplate.setPatient(patient);
        patientTemplate.setTemplate(template);
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
    
    public PatientTemplateBuilder withServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
        return this;
    }
    
    public PatientTemplateBuilder withServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
        return this;
    }
    
    public PatientTemplateBuilder withPatient(Patient patient) {
        this.patient = patient;
        return this;
    }
    
}
