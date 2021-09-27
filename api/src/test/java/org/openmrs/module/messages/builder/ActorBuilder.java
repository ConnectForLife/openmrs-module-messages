package org.openmrs.module.messages.builder;

import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.model.Actor;

public class ActorBuilder extends AbstractBuilder<Actor> {

    private Person target;

    private Relationship relationship;

    public ActorBuilder() {
        relationship = new RelationshipBuilder().build();
        target = relationship.getPersonA();
    }

    @Override
    public Actor build() {
        return new Actor(target, relationship);
    }

    @Override
    public Actor buildAsNew() {
        return new Actor(target, relationship);
    }

    public ActorBuilder withActorId(int actorId) {
        relationship.setPersonA(new PersonBuilder().withId(actorId).build());
        target = relationship.getPersonA();
        return this;
    }

    public ActorBuilder withInvertedRelationship() {
        target = relationship.getPersonB();
        return this;
    }

    public ActorBuilder withRelationshipNames(String aIsToB, String bIsToA) {
        this.relationship.getRelationshipType().setaIsToB(aIsToB);
        this.relationship.getRelationshipType().setbIsToA(bIsToA);
        return this;
    }
}
