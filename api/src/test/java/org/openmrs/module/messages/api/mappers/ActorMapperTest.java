/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.mappers;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.builder.PersonBuilder;
import org.openmrs.module.messages.builder.RelationshipBuilder;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class ActorMapperTest {

    private Actor dao;

    private ActorMapper actorMapper;

    private Person person;

    private Relationship relationship;

    private List<Actor> daos;

    @Before
    public void setUp() {
        actorMapper = new ActorMapper();
        person = new PersonBuilder().build();
        relationship = new RelationshipBuilder().build();
        dao = new Actor(person, relationship);
        daos = Arrays.asList(new Actor(person, relationship), new Actor(person, relationship));
    }

    @Test
    public void shouldMapFromDAOToDTO() {
        ActorDTO actorDTO = actorMapper.toDto(dao);

        assertThat(actorDTO, is(notNullValue()));
        assertEquals(dao.getTarget().getId(), actorDTO.getActorId());
        assertEquals(dao.getTarget().getPersonName().getFullName(), actorDTO.getActorName());
        assertEquals(dao.getRelationship().getId(), actorDTO.getActorTypeId());
        assertEquals(dao.getRelationship().getRelationshipType().getId(), actorDTO.getRelationshipTypeId());
        assertEquals(dao.getRelationship().getRelationshipType().getUuid(), actorDTO.getRelationshipTypeUuid());
    }

    @Test
    public void shouldMapFromDAOsToDTOs() {
        List<ActorDTO> actorDTOs = actorMapper.toDtos(daos);

        assertThat(actorDTOs, is(notNullValue()));
        assertEquals(daos.size(), actorDTOs.size());
        for (int i = 0; i < actorDTOs.size(); i++) {
            assertEquals(daos.get(i).getTarget().getId(), actorDTOs.get(i).getActorId());
            assertEquals(daos.get(i).getTarget().getPersonName().getFullName(), actorDTOs.get(i).getActorName());
            assertEquals(daos.get(i).getRelationship().getId(), actorDTOs.get(i).getActorTypeId());
            assertEquals(daos.get(i).getRelationship().getRelationshipType().getId(),
                    actorDTOs.get(i).getRelationshipTypeId());
            assertEquals(daos.get(i).getRelationship().getRelationshipType().getUuid(),
                    actorDTOs.get(i).getRelationshipTypeUuid());
        }
    }
}
