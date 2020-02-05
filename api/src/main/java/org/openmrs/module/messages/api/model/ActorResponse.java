package org.openmrs.module.messages.api.model;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity(name = "messages.ActorResponse")
@Table(name = "messages_actor_response")
public class ActorResponse extends AbstractBaseOpenmrsData {

    private static final long serialVersionUID = -2630763023031521760L;

    @Id
    @GeneratedValue
    @Column(name = "messages_actor_response_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Person actor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "source_id", columnDefinition = "text")
    private String sourceId;

    @ManyToOne
    @JoinColumn(name = "source_type", nullable = false)
    private ActorResponseType sourceType;

    @ManyToOne
    @JoinColumn(name = "question")
    private Concept question;

    @ManyToOne
    @JoinColumn(name = "response")
    private Concept response;

    @Column(name = "text_question", columnDefinition = "text")
    private String textQuestion;

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

    public Person getActor() {
        return actor;
    }

    public void setActor(Person actor) {
        this.actor = actor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public ActorResponseType getSourceType() {
        return sourceType;
    }

    public void setSourceType(ActorResponseType sourceType) {
        this.sourceType = sourceType;
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

    public String getTextQuestion() {
        return textQuestion;
    }

    public void setTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
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
