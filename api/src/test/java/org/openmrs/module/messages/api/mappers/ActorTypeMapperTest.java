package org.openmrs.module.messages.api.mappers;

import org.junit.Test;
import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.dto.ActorTypeDTO;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.util.StaticMappersProvider;
import org.openmrs.module.messages.builder.ActorTypeBuilder;
import org.openmrs.module.messages.builder.RelationshipTypeBuilder;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.openmrs.module.messages.Constant.CAREGIVER_RELATIONSHIP;
import static org.openmrs.module.messages.Constant.CARETAKER_RELATIONSHIP;

public class ActorTypeMapperTest {

    private static final String TYPE_UUID = "511cc270-2541-4a9b-b08e-d70b058f86ab";

    private final ActorTypeMapper actorTypeMapper = StaticMappersProvider.getActorTypeMapper();

    @Test
    public void shouldMapToActorTypeForDirectionA() {
        RelationshipType relationshipType = new RelationshipTypeBuilder()
                .withUuid(TYPE_UUID).build();
        ActorType actorType = new ActorTypeBuilder()
                .withDirection(RelationshipTypeDirection.A)
                .withType(relationshipType).build();

        ActorTypeDTO result = actorTypeMapper.toDto(actorType);

        assertEquals(TYPE_UUID, result.getUuid());
        assertEquals(CAREGIVER_RELATIONSHIP, result.getDisplay());
    }

    @Test
    public void shouldMapToActorTypeForDirectionB() {
        RelationshipType relationshipType = new RelationshipTypeBuilder()
                .withUuid(TYPE_UUID).build();
        ActorType actorType = new ActorTypeBuilder()
                .withDirection(RelationshipTypeDirection.B)
                .withType(relationshipType).build();

        ActorTypeDTO result = actorTypeMapper.toDto(actorType);

        assertEquals(TYPE_UUID, result.getUuid());
        assertEquals(CARETAKER_RELATIONSHIP, result.getDisplay());
    }

    @Test
    public void shouldMapDAOsToDTOs() {
        RelationshipType relationshipType = new RelationshipTypeBuilder().withUuid(TYPE_UUID).build();
        List<ActorType> actorTypes = Arrays.asList(new ActorTypeBuilder().withDirection(RelationshipTypeDirection.A).withType(relationshipType).build(),
                new ActorTypeBuilder().withDirection(RelationshipTypeDirection.B).withType(relationshipType).build());

        List<ActorTypeDTO> result = actorTypeMapper.toDtos(actorTypes);

        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
