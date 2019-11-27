package org.openmrs.module.messages.api.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

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
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "template_field_id", nullable = false)
    private TemplateField templateField;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "templateFieldValue", orphanRemoval = true)
    private List<PatientTemplate> patientTemplates;
    
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
    
    public List<PatientTemplate> getPatientTemplates() {
        return patientTemplates;
    }
    
    public void setPatientTemplates(List<PatientTemplate> patientTemplates) {
        this.patientTemplates = patientTemplates;
    }
}

