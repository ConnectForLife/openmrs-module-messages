package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.RelationshipType;

public class ActorType {

    private RelationshipType relationshipType;

    private RelationshipTypeDirection direction;

    public ActorType(RelationshipType relationshipType,
            RelationshipTypeDirection direction) {
        this.relationshipType = relationshipType;
        this.direction = direction;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public RelationshipTypeDirection getDirection() {
        return direction;
    }

    public void setDirection(RelationshipTypeDirection direction) {
        this.direction = direction;
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
