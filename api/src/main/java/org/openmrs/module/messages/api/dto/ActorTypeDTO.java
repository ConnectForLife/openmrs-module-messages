/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

/**
 * Represents an actor type DTO
 */
public class ActorTypeDTO extends BaseDTO {

    private static final long serialVersionUID = -6029047922656126583L;

    private String uuid;

    private String display;

    private Integer relationshipTypeId;

    private RelationshipTypeDirection relationshipTypeDirection;

    /**
     * Constructor of an ActorType DTO object
     *
     * @param uuid uuid of relationship type
     * @param display displayed name from relationship type, depending on the relationship type direction
     * @param relationshipTypeId id of relationship type
     * @param relationshipTypeDirection relationship type direction e.g. A or B
     */
    public ActorTypeDTO(String uuid, String display, Integer relationshipTypeId,
                        RelationshipTypeDirection relationshipTypeDirection) {
        this.uuid = uuid;
        this.display = display;
        this.relationshipTypeId = relationshipTypeId;
        this.relationshipTypeDirection = relationshipTypeDirection;
    }

    public String getUuid() {
        return uuid;
    }

    public ActorTypeDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getDisplay() {
        return display;
    }

    public ActorTypeDTO setDisplay(String display) {
        this.display = display;
        return this;
    }

    public Integer getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public void setRelationshipTypeId(Integer relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
    }

    public RelationshipTypeDirection getRelationshipTypeDirection() {
        return relationshipTypeDirection;
    }

    public void setRelationshipTypeDirection(RelationshipTypeDirection relationshipTypeDirection) {
        this.relationshipTypeDirection = relationshipTypeDirection;
    }
}
