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
import org.openmrs.module.messages.api.model.ActorType;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

public class ActorTypeBuilder extends AbstractBuilder<ActorType> {

    private RelationshipType relationshipType;

    private RelationshipTypeDirection direction;

    public ActorTypeBuilder() {
    }

    @Override
    public ActorType build() {
        return new ActorType(relationshipType, direction);
    }

    @Override
    public ActorType buildAsNew() {
        return build();
    }

    public ActorTypeBuilder withType(RelationshipType type) {
        this.relationshipType = type;
        return this;
    }

    public ActorTypeBuilder withDirection(RelationshipTypeDirection direction) {
        this.direction = direction;
        return this;
    }
}
