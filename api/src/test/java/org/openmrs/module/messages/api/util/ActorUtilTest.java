package org.openmrs.module.messages.api.util;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.builder.ActorBuilder;

public class ActorUtilTest {

    private static final String CAREGIVER_RELATIONSHIP = "Caregiver";
    private static final String CARETAKER_RELATIONSHIP = "Caretaker";

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
}
