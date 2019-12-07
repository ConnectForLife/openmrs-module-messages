package org.openmrs.module.messages.api.mappers;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.model.Actor;

import java.util.ArrayList;
import java.util.List;

public class ActorMapper {

    public List<ActorDTO> toDtos(List<Actor> daos) {
        List<ActorDTO> dtos = new ArrayList<ActorDTO>();
        for (Actor dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    public ActorDTO toDto(Actor dao) {
        RelationshipType relationshipType = dao.getRelationship().getRelationshipType();
        String typeName = dao.getRelationship().getPersonA().getId().equals(dao.getTarget().getId()) ?
                relationshipType.getaIsToB() :
                relationshipType.getbIsToA();
        return new ActorDTO()
                .setActorId(dao.getTarget().getPersonId())
                .setActorName(dao.getTarget().getPersonName().getFullName())
                .setActorTypeId(dao.getRelationship().getId())
                .setActorTypeName(typeName);
    }
}
