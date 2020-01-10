package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ActorScheduleDTO implements Comparable<ActorScheduleDTO> {

    private Integer actorId;

    private String actorType;

    private String schedule;

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
}
