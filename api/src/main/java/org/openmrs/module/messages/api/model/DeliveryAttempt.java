package org.openmrs.module.messages.api.model;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity(name = "messages.DeliveryAttempt")
@Table(name = "messages_delivery_attempt")
public class DeliveryAttempt extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 8564832003377818271L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_delivery_attempt_id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "scheduled_service_id", nullable = false)
    private Concept scheduledService;
    
    @Column(name = "timestamp", nullable = false)
    private Date timestamp;
    
    @Column(name = "new_status", nullable = false)
    private ServiceStatus status;
    
    @Column(name = "attempt_number", nullable = false)
    private Integer attemptNumber;
    
    @Column(name = "service_execution_id", nullable = false)
    private String serviceExecution;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public Concept getScheduledService() {
        return scheduledService;
    }
    
    public void setScheduledService(Concept scheduledService) {
        this.scheduledService = scheduledService;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    
    public ServiceStatus getStatus() {
        return status;
    }
    
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }
    
    public Integer getAttemptNumber() {
        return attemptNumber;
    }
    
    public void setAttemptNumber(Integer attemptNumber) {
        this.attemptNumber = attemptNumber;
    }
    
    public String getServiceExecution() {
        return serviceExecution;
    }
    
    public void setServiceExecution(String serviceExecution) {
        this.serviceExecution = serviceExecution;
    }
}
