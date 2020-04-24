package org.openmrs.module.messages.api.dto;

import org.junit.Test;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ActorTypeDTOTest {

    private static final String UUID = "be8263e3-f283-4613-92d1-573efcf159d0";

    private static final String UUID_2 = "be8263e3-f283-4613-92d1-573efcf159d1";

    private static final String DISPLAY = "Caregiver";

    private static final String DISPLAY_2 = "Parent";

    private static final Integer RELATIONSHIP_TYPE_ID = 4;

    private static final Integer RELATIONSHIP_TYPE_ID_2 = 8;

    private static final RelationshipTypeDirection DIRECTION = RelationshipTypeDirection.B;

    private static final RelationshipTypeDirection DIRECTION_2 = RelationshipTypeDirection.A;

    private ActorTypeDTO actorTypeDTO;

    private ActorTypeDTO actorTypeDTO2;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        actorTypeDTO = new ActorTypeDTO(UUID, DISPLAY, RELATIONSHIP_TYPE_ID, DIRECTION);

        assertThat(actorTypeDTO, is(notNullValue()));
        assertEquals(UUID, actorTypeDTO.getUuid());
        assertEquals(DISPLAY, actorTypeDTO.getDisplay());
        assertEquals(RELATIONSHIP_TYPE_ID, actorTypeDTO.getRelationshipTypeId());
        assertEquals(DIRECTION, actorTypeDTO.getRelationshipTypeDirection());
    }

    @Test
    public void shouldChangeCreatedDtoInstanceSuccessfully() {
        actorTypeDTO2 = changeActorTypeDTOObject();

        assertThat(actorTypeDTO2, is(notNullValue()));
        assertEquals(UUID_2, actorTypeDTO2.getUuid());
        assertEquals(DISPLAY_2, actorTypeDTO2.getDisplay());
        assertEquals(RELATIONSHIP_TYPE_ID_2, actorTypeDTO2.getRelationshipTypeId());
        assertEquals(DIRECTION_2, actorTypeDTO2.getRelationshipTypeDirection());
    }

    private ActorTypeDTO changeActorTypeDTOObject() {
        ActorTypeDTO dto = new ActorTypeDTO(UUID, DISPLAY, RELATIONSHIP_TYPE_ID, DIRECTION);
        dto.setUuid(UUID_2);
        dto.setDisplay(DISPLAY_2);
        dto.setRelationshipTypeId(RELATIONSHIP_TYPE_ID_2);
        dto.setRelationshipTypeDirection(DIRECTION_2);
        return dto;
    }
}
