package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

public class ActorScheduleDTO implements Comparable<ActorScheduleDTO>, DTO {
    private Integer actorId;
    private String actorType;
    private String schedule;
    private Integer patientId;
    private String patientName;

    public ActorScheduleDTO(Integer actorId, String actorType, String schedule, Integer patientId, String patientName) {
        this.actorId = actorId;
        this.actorType = actorType;
        this.schedule = schedule;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public ActorScheduleDTO(Integer actorId, String actorType, String schedule) {
        this.actorId = actorId;
        this.actorType = actorType;
        this.schedule = schedule;
    }

    public ActorScheduleDTO(String actorType, String schedule) {
        this.actorType = actorType;
        this.schedule = schedule;
    }

    public ActorScheduleDTO(Integer actorId, String actorType) {
        this.actorId = actorId;
        this.actorType = actorType;
    }

    public ActorScheduleDTO() { }

    public Integer getActorId() {
        return actorId;
    }

    public String getActorType() {
        return actorType;
    }

    public String getSchedule() {
        return schedule;
    }

    @Override
    @JsonIgnore
    public Integer getId() {
        throw new NotImplementedException("not implemented yet");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public int compareTo(ActorScheduleDTO o) {
        return new CompareToBuilder()
            .append(this.getActorId(), o.getActorId())
            .toComparison();
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
}
