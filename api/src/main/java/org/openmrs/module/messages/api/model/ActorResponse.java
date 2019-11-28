package org.openmrs.module.messages.api.model;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.openmrs.Concept;

@Entity(name = "messages.ActorResponse")
@Table(name = "messages_actor_response")
public class ActorResponse extends AbstractBaseOpenmrsData {
    
    private static final long serialVersionUID = -2630763023031521760L;
    
    @Id
    @GeneratedValue
    @Column(name = "messages_actor_response_id")
    private Integer id;
    
    @JoinColumn(name = "scheduled_service_id", nullable = false)
    private ScheduledService scheduledService;
    
    @OneToOne
    @JoinColumn(name = "question")
    private Concept question;
    
    @OneToOne
    @JoinColumn(name = "response")
    private Concept response;
    
    @Column(name = "text_response", columnDefinition = "text")
    private String textResponse;
    
    @Column(name = "answered_time")
    private Date answeredTime;
    
    @Override
    public Integer getId() {
        return id;
    }
    
    @Override
    public void setId(Integer id) {
        this.id = id;
    }
    
    public ScheduledService getScheduledService() {
        return scheduledService;
    }
    
    public void setScheduledService(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
    }
    
    public Concept getQuestion() {
        return question;
    }
    
    public void setQuestion(Concept question) {
        this.question = question;
    }
    
    public Concept getResponse() {
        return response;
    }
    
    public void setResponse(Concept response) {
        this.response = response;
    }
    
    public String getTextResponse() {
        return textResponse;
    }
    
    public void setTextResponse(String textResponse) {
        this.textResponse = textResponse;
    }
    
    public Date getAnsweredTime() {
        return answeredTime;
    }
    
    public void setAnsweredTime(Date answeredTime) {
        this.answeredTime = answeredTime;
    }
}
