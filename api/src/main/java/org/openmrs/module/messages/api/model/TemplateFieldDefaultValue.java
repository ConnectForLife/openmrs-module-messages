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

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.util.ActorUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity(name = "messages.TemplateFieldDefaultValue")
@Table(name = "messages_template_field_default_values")
public class TemplateFieldDefaultValue extends AbstractBaseOpenmrsData {

    private static final long serialVersionUID = -6119668715226641370L;

    @Id
    @GeneratedValue
    @Column(name = "messages_template_field_default_values_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "relationship_type", nullable = false)
    private RelationshipType relationshipType;

    @Enumerated(EnumType.STRING)
    @Column(name = "relationship_direction", nullable = false)
    private RelationshipTypeDirection direction;

    @ManyToOne
    @JoinColumn(name = "template_field", nullable = false)
    private TemplateField templateField;

    @Column(name = "default_value", columnDefinition = "text")
    private String defaultValue;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TemplateFieldDefaultValue#" + id;
    }

    public RelationshipType getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(RelationshipType relationshipType) {
        this.relationshipType = relationshipType;
    }

    public RelationshipTypeDirection getDirection() {
        return direction;
    }

    public void setDirection(RelationshipTypeDirection direction) {
        this.direction = direction;
    }

    public TemplateField getTemplateField() {
        return templateField;
    }

    public void setTemplateField(TemplateField templateField) {
        this.templateField = templateField;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Transient
    public boolean isRelatedTo(Actor actor) {
        RelationshipTypeDirection actorDirection =
            ActorUtil.getRelationShipTypeDirection(actor);
        String actorRelationshipName = actor.getRelationship().getRelationshipType().getaIsToB();

        boolean isDirectionsMatches = this.getDirection() == actorDirection;

        return isDirectionsMatches ?
            this.getRelationshipType().getaIsToB().equals(actorRelationshipName) :
            this.getRelationshipType().getbIsToA().equals(actorRelationshipName);
    }
}
