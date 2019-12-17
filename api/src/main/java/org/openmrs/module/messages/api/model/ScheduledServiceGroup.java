package org.openmrs.module.messages.api.model;

import org.openmrs.Patient;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "messages.ScheduledServiceGroup")
@Table(name = "messages_scheduled_service_group")
public class ScheduledServiceGroup extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = -2938591098039855643L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_scheduled_service_group_id")
    private Integer id;
    
    @Column(name = "msg_send_time")
    private Date msgSendTime;
    
    @OneToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;
    
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<ScheduledService> scheduledServices = new ArrayList<>();
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Date getMsgSendTime() {
        return msgSendTime;
    }
    
    public void setMsgSendTime(Date msgSendTime) {
        this.msgSendTime = msgSendTime;
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void setPatient(Patient patient) {
        this.patient = patient;
    }
    
    public ServiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }

    public List<ScheduledService> getScheduledServices() {
        return scheduledServices;
    }

    public void setScheduledServices(List<ScheduledService> scheduledServices) {
        this.scheduledServices = scheduledServices;
    }
}
