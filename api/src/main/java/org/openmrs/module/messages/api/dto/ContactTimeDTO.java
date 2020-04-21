package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

/**
 * Represents a contact time DTO
 */
public class ContactTimeDTO implements Serializable {

    private static final long serialVersionUID = -2593240814911309883L;

    private Integer personId;

    private String time;

    public Integer getPersonId() {
        return personId;
    }

    public ContactTimeDTO setPersonId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public String getTime() {
        return time;
    }

    public ContactTimeDTO setTime(String time) {
        this.time = time;
        return this;
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
