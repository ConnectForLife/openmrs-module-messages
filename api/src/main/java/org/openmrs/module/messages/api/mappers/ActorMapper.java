package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.model.Actor;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.module.messages.api.util.ActorUtil;

public class ActorMapper {

    public List<ActorDTO> toDtos(List<Actor> daos) {
        List<ActorDTO> dtos = new ArrayList<ActorDTO>();
        for (Actor dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    public ActorDTO toDto(Actor dao) {

        String typeName = ActorUtil.getActorTypeName(dao);
        return new ActorDTO()
                .setActorId(dao.getTarget().getPersonId())
                .setActorName(dao.getTarget().getPersonName().getFullName())
                .setActorTypeId(dao.getRelationship().getId())
                .setActorTypeName(typeName)
                .setRelationshipTypeId(dao.getRelationship().getRelationshipType().getId());
    }
}
