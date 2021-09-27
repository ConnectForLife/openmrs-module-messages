package org.openmrs.module.messages.api.util;

import org.junit.Test;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.builder.ActorBuilder;
import org.openmrs.module.messages.builder.RelationshipBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.openmrs.module.messages.Constant.CAREGIVER_RELATIONSHIP;
import static org.openmrs.module.messages.Constant.CARETAKER_RELATIONSHIP;

public class ActorUtilTest {

    @Test
    public void shouldReturnActorTypeNameForAToBRelationship() {
        Actor actor = new ActorBuilder()
            .withRelationshipNames(CAREGIVER_RELATIONSHIP, CARETAKER_RELATIONSHIP)
            .build();

        String actorTypeName = ActorUtil.getActorTypeName(actor);
        assertThat(actorTypeName, is(equalTo(CAREGIVER_RELATIONSHIP)));
    }

    @Test
    public void shouldReturnActorTypeNameForBToARelationship() {
        Actor actor = new ActorBuilder()
            .withRelationshipNames(CAREGIVER_RELATIONSHIP, CARETAKER_RELATIONSHIP)
            .withInvertedRelationship()
            .build();

        String actorTypeName = ActorUtil.getActorTypeName(actor);
        assertThat(actorTypeName, is(equalTo(CARETAKER_RELATIONSHIP)));
    }

    @Test
    public void shouldReturnTrueIfActorIsPatient() {
        Actor actor = new ActorBuilder()
                .withRelationshipNames(CAREGIVER_RELATIONSHIP, CARETAKER_RELATIONSHIP)
                .build();
        Integer personId = new RelationshipBuilder().build().getPersonA().getPersonId();
        assertTrue(ActorUtil.isActorPatient(actor, personId));
    }

    @Test
    public void shouldReturnFalseIfActorIsNotPatient() {
        Actor actor = new ActorBuilder()
                .withRelationshipNames(CAREGIVER_RELATIONSHIP, CARETAKER_RELATIONSHIP)
                .withInvertedRelationship()
                .build();
        Integer personId = new RelationshipBuilder().build().getPersonA().getPersonId();
        assertFalse(ActorUtil.isActorPatient(actor, personId));
    }

    @Test
    public void shouldReturnFalseIfRelationshipIsNull() {
        Actor actor = new ActorBuilder()
                .withRelationshipNames(CAREGIVER_RELATIONSHIP, CARETAKER_RELATIONSHIP)
                .build();
        actor.setRelationship(null);
        Integer personId = new RelationshipBuilder().build().getPersonA().getPersonId();
        assertFalse(ActorUtil.isActorPatient(actor, personId));
    }
}
