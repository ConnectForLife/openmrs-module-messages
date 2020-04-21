package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

import javax.validation.constraints.NotNull;

/**
 * Represents a template field default value DTO
 */
public class TemplateFieldDefaultValueDTO implements DTO {

    private Integer id;

    @NotNull
    private Integer relationshipTypeId;

    @NotNull
    private RelationshipTypeDirection direction;

    private Integer templateFieldId;

    private String defaultValue;

    public Integer getId() {
        return id;
    }

    public TemplateFieldDefaultValueDTO setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getRelationshipTypeId() {
        return relationshipTypeId;
    }

    public TemplateFieldDefaultValueDTO setRelationshipTypeId(Integer relationshipTypeId) {
        this.relationshipTypeId = relationshipTypeId;
        return this;
    }

    public RelationshipTypeDirection getDirection() {
        return direction;
    }

    public TemplateFieldDefaultValueDTO setDirection(RelationshipTypeDirection direction) {
        this.direction = direction;
        return this;
    }

    public Integer getTemplateFieldId() {
        return templateFieldId;
    }

    public TemplateFieldDefaultValueDTO setTemplateFieldId(Integer templateFieldId) {
        this.templateFieldId = templateFieldId;
        return this;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public TemplateFieldDefaultValueDTO setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
        return this;
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
}
