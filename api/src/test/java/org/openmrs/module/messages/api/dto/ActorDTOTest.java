package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ActorDTOTest {

    private static final Integer ACTOR_ID = 1;

    private static final String ACTOR_NAME = "John Doe";

    private static final String ACTOR_TYPE = "Caregiver";

    private static final Integer ACTOR_TYPE_ID = 3;

    private static final Integer RELATIONSHIP_TYPE_ID = 8;

    private static final String RELATIONSHIP_TYPE_UUID = "2a5f4ff4-a179-4b8a-aa4c-40f71956ebbc";

    private ActorDTO actorDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        actorDTO = buildActorDTOObject();

        assertThat(actorDTO, is(notNullValue()));
        assertEquals(ACTOR_ID, actorDTO.getActorId());
        assertEquals(ACTOR_NAME, actorDTO.getActorName());
        assertEquals(ACTOR_TYPE, actorDTO.getActorTypeName());
        assertEquals(ACTOR_TYPE_ID, actorDTO.getActorTypeId());
        assertEquals(RELATIONSHIP_TYPE_ID, actorDTO.getRelationshipTypeId());
        assertEquals(RELATIONSHIP_TYPE_UUID, actorDTO.getRelationshipTypeUuid());
    }

    private ActorDTO buildActorDTOObject() {
        ActorDTO dto = new ActorDTO()
                .setActorId(ACTOR_ID)
                .setActorName(ACTOR_NAME)
                .setActorTypeName(ACTOR_TYPE)
                .setActorTypeId(ACTOR_TYPE_ID)
                .setRelationshipTypeId(RELATIONSHIP_TYPE_ID)
                .setRelationshipTypeUuid(RELATIONSHIP_TYPE_UUID);
        return dto;
    }
}
