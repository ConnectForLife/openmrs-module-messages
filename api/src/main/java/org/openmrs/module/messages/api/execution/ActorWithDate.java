package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class ActorWithDate {

    private Integer actorId;
    private String actorType;
    private Date date;

    public ActorWithDate(Integer actorId, String actorType, Date date) {
        this.actorId = actorId;
        this.actorType = actorType;
        this.date = date;
    }

    public Integer getActorId() {
        return actorId;
    }

    public String getActorType() {
        return actorType;
    }

    public Date getDate() {
        return date;
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
}
