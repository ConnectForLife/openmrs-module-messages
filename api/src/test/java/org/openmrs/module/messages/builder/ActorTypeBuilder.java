package org.openmrs.module.messages.builder;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

public class ActorTypeBuilder extends AbstractBuilder<ActorType> {

    private RelationshipType relationshipType;

    private RelationshipTypeDirection direction;

    public ActorTypeBuilder() {
    }

    @Override
    public ActorType build() {
        return new ActorType(relationshipType, direction);
    }

    @Override
    public ActorType buildAsNew() {
        return build();
    }

    public ActorTypeBuilder withType(RelationshipType type) {
        this.relationshipType = type;
        return this;
    }

    public ActorTypeBuilder withDirection(RelationshipTypeDirection direction) {
        this.direction = direction;
        return this;
    }
}
