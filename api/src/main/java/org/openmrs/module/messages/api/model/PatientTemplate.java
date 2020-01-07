package org.openmrs.module.messages.api.model;

import java.util.ArrayList;
import java.util.List;
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
import org.apache.commons.lang.StringUtils;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;

@Entity(name = "messages.PatientTemplate")
@Table(name = "messages_patient_template")
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
    
    @Column(name = "service_query", columnDefinition = "text", nullable = false)
    private String serviceQuery;
    
    @Column(name = "service_query_type", nullable = false)
    private String serviceQueryType;
    
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patientTemplate", orphanRemoval = true)
    private List<TemplateFieldValue> templateFieldValues = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;
    
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
        return StringUtils.isBlank(serviceQuery) ? getTemplate().getServiceQuery() : this.serviceQuery;
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
    
    public void setServiceQueryType(String serviceQueryType) {
        this.serviceQueryType = serviceQueryType;
    }
    
    @Transient
    public Integer getServiceId() {
        return 0; // TODO
    }
}
