/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Person;
import org.openmrs.Relationship;

import java.io.Serializable;

public class Actor implements Comparable<Actor>, Serializable {

    private static final long serialVersionUID = 7591031916726299702L;

    private Person target;

    private Relationship relationship;

    public Actor(Person target, Relationship relationship) {
        this.target = target;
        this.relationship = relationship;
    }

    public Person getTarget() {
        return target;
    }

    public void setTarget(Person target) {
        this.target = target;
    }

    public Relationship getRelationship() {
        return relationship;
    }

    public void setRelationship(Relationship relationship) {
        this.relationship = relationship;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public int compareTo(Actor actor) {
        return new CompareToBuilder()
                .append(this.getTarget().getPersonId(), actor.getTarget().getPersonId())
                .toComparison();
    }
}
