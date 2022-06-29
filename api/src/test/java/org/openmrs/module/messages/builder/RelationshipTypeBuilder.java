/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.RelationshipType;

import static org.openmrs.module.messages.Constant.CAREGIVER_RELATIONSHIP;
import static org.openmrs.module.messages.Constant.CARETAKER_RELATIONSHIP;

public class RelationshipTypeBuilder extends AbstractBuilder<RelationshipType> {

    private Integer id;
    private String uuid;
    private Integer relationshipTypeId;
    private String aIsToB;
    private String bIsToA;
    private Integer weight;
    private Boolean preferred;

    public RelationshipTypeBuilder() {
        this.id = getInstanceNumber();
        this.relationshipTypeId = id;
        this.aIsToB = CAREGIVER_RELATIONSHIP;
        this.bIsToA = CARETAKER_RELATIONSHIP;
        this.weight = 0;
        this.preferred = false;
    }

    @Override
    public RelationshipType build() {
        RelationshipType relationshipType = new RelationshipType(relationshipTypeId);
        relationshipType.setId(id);
        relationshipType.setUuid(uuid);
        relationshipType.setaIsToB(aIsToB);
        relationshipType.setbIsToA(bIsToA);
        relationshipType.setWeight(weight);
        relationshipType.setPreferred(preferred);
        return relationshipType;
    }

    @Override
    public RelationshipType buildAsNew() {
        RelationshipType type = build();
        type.setId(null);
        return type;
    }

    public RelationshipTypeBuilder withUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }
}
