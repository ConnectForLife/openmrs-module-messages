package org.openmrs.module.messages.api.util;

import org.openmrs.Person;
import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

public final class ActorUtil {

    public static boolean isActorPatient(Actor actor, Integer patientId) {
        if (actor.getRelationship() == null) {
            return false;
        }
        Person personA = actor.getRelationship().getPersonA();
        Person personB = actor.getRelationship().getPersonB();
        return getRelationShipTypeDirection(actor) == RelationshipTypeDirection.A ?
            personA != null && personA.getId().equals(patientId) :
            personB != null && personB.getId().equals(patientId);
    }

    public static Person getActorPerson(Actor actor) {
        return getRelationShipTypeDirection(actor) == RelationshipTypeDirection.A ?
            actor.getRelationship().getPersonA() :
            actor.getRelationship().getPersonB();
    }

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

    public static RelationshipTypeDirection getRelationShipTypeDirection(Actor actor) {
        return actor.getRelationship().getPersonA().getId().equals(actor.getTarget().getId()) ?
            RelationshipTypeDirection.A : RelationshipTypeDirection.B;
    }

    private ActorUtil() {
    }
}
