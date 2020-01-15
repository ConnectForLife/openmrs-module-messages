package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.ActorTypeDTO;
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

import java.util.ArrayList;
import java.util.List;

public class ActorTypeMapper {

    public List<ActorTypeDTO> toDtos(List<ActorType> daos) {
        List<ActorTypeDTO> dtos = new ArrayList<ActorTypeDTO>();
        for (ActorType dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    public ActorTypeDTO toDto(ActorType dao) {
        return new ActorTypeDTO(dao.getRelationshipType().getUuid(), getDisplay(dao));
    }

    private String getDisplay(ActorType actorType) {
        if (actorType.getDirection().equals(RelationshipTypeDirection.A)) {
            return actorType.getRelationshipType().getbIsToA();
        } else {
            return actorType.getRelationshipType().getaIsToB();
        }
    }
}
