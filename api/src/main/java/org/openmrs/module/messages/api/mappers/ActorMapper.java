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

import org.openmrs.module.messages.api.dto.ActorDTO;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.util.ActorUtil;

import java.util.ArrayList;
import java.util.List;

public class ActorMapper {

    public List<ActorDTO> toDtos(List<Actor> daos) {
        List<ActorDTO> dtos = new ArrayList<>(daos.size());
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
                .setRelationshipTypeUuid(dao.getRelationship().getRelationshipType().getUuid())
                .setRelationshipTypeId(dao.getRelationship().getRelationshipType().getId());
    }
}
