package org.openmrs.module.messages.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity(name = "messages.ScheduleServiceParameter")
@Table(name = "messages_schedule_service_parameter")
public class ScheduledServiceParameter extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = 1573774741059669932L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_schedule_service_parameter_id")
    private Integer id;
    
    @JoinColumn(name = "scheduled_message")
    private ScheduledService scheduledMessage;
    
    @Column(name = "parameter_type")
    private String parameterType;
    
    @Column(name = "parameter_value", columnDefinition = "text")
    private String parameterValue;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public ScheduledService getScheduledMessage() {
        return scheduledMessage;
    }
    
    public void setScheduledMessage(ScheduledService scheduledMessage) {
        this.scheduledMessage = scheduledMessage;
    }
    
    public String getParameterType() {
        return parameterType;
    }
    
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
    
    public String getParameterValue() {
        return parameterValue;
    }
    
    public void setParameterValue(String parameterValue) {
        this.parameterValue = parameterValue;
    }
}
