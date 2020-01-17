package org.openmrs.module.messages.api.model;

import org.openmrs.RelationshipType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
}
