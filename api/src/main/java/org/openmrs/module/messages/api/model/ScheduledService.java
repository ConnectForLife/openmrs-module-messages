package org.openmrs.module.messages.api.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.openmrs.Concept;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

@Entity(name = "messages.ScheduledService")
@Table(name = "messages_scheduled_service")
public class ScheduledService extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 8861114900550389044L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_scheduled_service_id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "group_id")
    private ScheduledServiceGroup group;
    
    @OneToOne
    @JoinColumn(name = "service")
    private Concept service;
    
    @ManyToOne
    @JoinColumn(name = "patient_template_id", nullable = false)
    private PatientTemplate patientTemplate;
    
    @OneToOne
    @JoinColumn(name = "channel_type", nullable = false)
    private Concept channelType;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scheduledService", orphanRemoval = true)
    private List<DeliveryAttempt> deliveryAttempts = new ArrayList<>();
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scheduledMessage", orphanRemoval = true)
    private List<ScheduledServiceParameter> scheduledServiceParameters = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "scheduledService", orphanRemoval = true)
    private List<ActorResponse> actorResponses = new ArrayList<>();
    
    @Column(name = "last_service_execution_id")
    private String lastServiceExecution;
    
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
    
    public String getLastServiceExecution() {
        return lastServiceExecution;
    }
    
    public void setLastServiceExecution(String lastServiceExecution) {
        this.lastServiceExecution = lastServiceExecution;
    }
    
    public List<DeliveryAttempt> getDeliveryAttempts() {
        return deliveryAttempts;
    }
    
    public void setDeliveryAttempts(List<DeliveryAttempt> deliveryAttempts) {
        this.deliveryAttempts = deliveryAttempts;
    }
    
    public List<ScheduledServiceParameter> getScheduledServiceParameters() {
        return scheduledServiceParameters;
    }
    
    public void setScheduledServiceParameters(List<ScheduledServiceParameter> scheduledServiceParameters) {
        this.scheduledServiceParameters = scheduledServiceParameters;
    }
    
    public List<ActorResponse> getActorResponses() {
        return actorResponses;
    }
    
    public void setActorResponses(List<ActorResponse> actorResponses) {
        this.actorResponses = actorResponses;
    }
}
