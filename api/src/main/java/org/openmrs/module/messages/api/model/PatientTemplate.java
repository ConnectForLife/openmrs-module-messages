package org.openmrs.module.messages.api.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "messages.PatientTemplate")
@Table(name = "messages_patient_template")
public class PatientTemplate extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 4808798852617376186L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_patient_template_id")
    private Integer id;
    
    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "actor_id", nullable = false)
    private Person actor;
    
    @OneToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "actor_type")
    private Relationship actorType;
    
    @Column(name = "service_query", columnDefinition = "text", nullable = false)
    private String serviceQuery;
    
    @Column(name = "service_query_type", nullable = false)
    private String serviceQueryType;
    
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private TemplateFieldValue templateFieldValue;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
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
        return serviceQuery;
    }
    
    public void setServiceQuery(String serviceQuery) {
        this.serviceQuery = serviceQuery;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public TemplateFieldValue getTemplateFieldValue() {
        return templateFieldValue;
    }

    public void setTemplateFieldValue(TemplateFieldValue templateFieldValue) {
        this.templateFieldValue = templateFieldValue;
    }
    
    public String getServiceQueryType() {
        return serviceQueryType;
    }
    
    public void setServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
    }
}
