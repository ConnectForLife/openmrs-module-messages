package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity(name = "messages.ActorResponseType")
@Table(name = "messages_actor_response_type")
public class ActorResponseType implements Serializable {

    private static final long serialVersionUID = 8521432405404466075L;

    @Id
    @Column(name = "name")
    private String name;

    public ActorResponseType() { }

    public ActorResponseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "ActorResponseType#" + name;
    }
}
