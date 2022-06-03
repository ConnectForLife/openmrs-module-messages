/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
