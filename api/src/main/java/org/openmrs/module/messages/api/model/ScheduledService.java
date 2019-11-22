package org.openmrs.module.messages.api.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.openmrs.Concept;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity(name = "messages.ScheduledService")
@Table(name = "messages_scheduled_service")
public class ScheduledService extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 8861114900550389044L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_scheduled_service_id")
    private Integer id;
    
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "group_id")
    private ScheduledServiceGroup group;
    
    @OneToOne
    @JoinColumn(name = "service")
    private Concept service;
    
    @ManyToOne
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "patient_template_id", nullable = false)
    private PatientTemplate patientTemplate;
    
    @OneToOne
    @JoinColumn(name = "channel_type", nullable = false)
    private Concept channelType;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    
    @Column(name = "last_service_execution_id")
    private Integer lastServiceExecution;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public ScheduledServiceGroup getGroup() {
        return group;
    }
    
    public void setGroup(ScheduledServiceGroup group) {
        this.group = group;
    }
    
    public Concept getService() {
        return service;
    }
    
    public void setService(Concept service) {
        this.service = service;
    }
    
    public PatientTemplate getPatientTemplate() {
        return patientTemplate;
    }
    
    public void setPatientTemplate(PatientTemplate patientTemplate) {
        this.patientTemplate = patientTemplate;
    }
    
    public Concept getChannelType() {
        return channelType;
    }
    
    public void setChannelType(Concept channelType) {
        this.channelType = channelType;
    }
    
    public ServiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }
    
    public Integer getLastServiceExecution() {
        return lastServiceExecution;
    }
    
    public void setLastServiceExecution(Integer lastServiceExecution) {
        this.lastServiceExecution = lastServiceExecution;
    }
}
