package org.openmrs.module.messages.api.util;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

public final class ActorUtil {

    public static String getActorTypeName(Actor actor) {
        RelationshipType relationshipType = actor.getRelationship().getRelationshipType();
        return actor.getRelationship().getPersonA().getId().equals(actor.getTarget().getId()) ?
                relationshipType.getaIsToB() :
                relationshipType.getbIsToA();
    }

    public static String getActorTypeName(ActorType actorType) {
        if (actorType.getDirection().equals(RelationshipTypeDirection.A)) {
            return actorType.getRelationshipType().getaIsToB();
        } else {
            return actorType.getRelationshipType().getbIsToA();
        }
    }

    private ActorUtil() {
    }
}
