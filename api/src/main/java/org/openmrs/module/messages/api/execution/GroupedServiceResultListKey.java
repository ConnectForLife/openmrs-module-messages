package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Date;

public class GroupedServiceResultListKey {

    private final String channelType;
    private final Integer actorId;
    private final Integer patientId;
    private final String actorType;
    private final Date date;

    public GroupedServiceResultListKey(final String singleChannelType, final ServiceResult serviceResult,
                                       final String actorType) {
        this.channelType = singleChannelType;
        this.actorId = serviceResult.getActorId();
        this.patientId = serviceResult.getPatientId();
        this.actorType = actorType;
        this.date = serviceResult.getExecutionDate();
    }

    public String getChannelType() {
        return channelType;
    }

    public Integer getActorId() {
        return actorId;
    }

    public Integer getPatientId() {
        return patientId;
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

    @Override
    public String toString() {
        return "GroupedServiceResultListKey {channelType='" + channelType + "', actorId=" + actorId + ", actorType='" +
                actorType + "', date=" + date + '}';
    }
}
