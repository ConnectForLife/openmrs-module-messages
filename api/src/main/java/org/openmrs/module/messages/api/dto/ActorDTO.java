/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

/**
 * Represents an actor DTO
 */
public class ActorDTO extends BaseDTO {
    private static final long serialVersionUID = 4702818718878938436L;

    private Integer actorId;

    private String actorName;

    private String actorTypeName;

    private Integer actorTypeId;

    private Integer relationshipTypeId;

    private String relationshipTypeUuid;

    public Integer getActorId() {
        return actorId;
    }

    public ActorDTO setActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public String getActorName() {
        return actorName;
    }

    public ActorDTO setActorName(String actorName) {
        this.actorName = actorName;
        return this;
    }

    public String getActorTypeName() {
        return actorTypeName;
    }

    public ActorDTO setActorTypeName(String actorTypeName) {
        this.actorTypeName = actorTypeName;
        return this;
    }

    public Integer getActorTypeId() {
        return actorTypeId;
    }

    public ActorDTO setActorTypeId(Integer actorTypeId) {
        this.actorTypeId = actorTypeId;
        return this;
    }

    public Integer getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public ActorDTO setRelationshipTypeId(Integer relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
        return this;
    }

    public String getRelationshipTypeUuid() {
        return relationshipTypeUuid;
    }

    public ActorDTO setRelationshipTypeUuid(String relationshipTypeUuid) {
        this.relationshipTypeUuid = relationshipTypeUuid;
        return this;
    }
}
