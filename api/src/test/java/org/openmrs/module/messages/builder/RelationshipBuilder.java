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

import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.Date;

public class RelationshipBuilder extends AbstractBuilder<Relationship> {

    private Integer relationshipId;
    private Person personA;
    private RelationshipType relationshipType;
    private Person personB;
    private Date startDate;
    private Date endDate;

    public RelationshipBuilder() {
        super();
        relationshipId = getInstanceNumber();
        personA = new Person(1);
        relationshipType = new RelationshipTypeBuilder().build();
        personB = new Person(2);
        startDate = DateUtil.toDate(DateUtil.now().plusSeconds(1));
        endDate = DateUtil.toDate(DateUtil.now().plusSeconds(1));
    }

    @Override
    public Relationship build() {
        Relationship relationship = new Relationship(personA, personB, relationshipType);
        relationship.setRelationshipId(relationshipId);
        relationship.setStartDate(startDate);
        relationship.setEndDate(endDate);
        return relationship;
    }

    @Override
    public Relationship buildAsNew() {
        Relationship relationship = build();
        relationship.setRelationshipId(null);
        return relationship;
    }

    public RelationshipBuilder withRelationshipId(Integer relationshipId) {
        this.relationshipId = relationshipId;
        return this;
    }

    public RelationshipBuilder withPersonA(Person personA) {
        this.personA = personA;
        return this;
    }

    public RelationshipBuilder withRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
        return this;
    }

    public RelationshipBuilder withPersonB(Person personB) {
        this.personB = personB;
        return this;
    }

    public RelationshipBuilder withStartDate(Date startDate) {
        this.startDate = startDate;
        return this;
    }

    public RelationshipBuilder withEndDate(Date endDate) {
        this.endDate = endDate;
        return this;
    }
}
