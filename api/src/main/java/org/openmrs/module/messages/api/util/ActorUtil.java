package org.openmrs.module.messages.api.util;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.model.Actor;

public final class ActorUtil {

    public static String getActorTypeName(Actor actor) {
        RelationshipType relationshipType = actor.getRelationship().getRelationshipType();
        return actor.getRelationship().getPersonA().getId().equals(actor.getTarget().getId()) ?
            relationshipType.getaIsToB() :
            relationshipType.getbIsToA();
    }

    private ActorUtil() {
    }
}
