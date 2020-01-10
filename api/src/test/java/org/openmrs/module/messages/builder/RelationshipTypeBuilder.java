package org.openmrs.module.messages.builder;

import org.openmrs.RelationshipType;

public class RelationshipTypeBuilder extends AbstractBuilder<RelationshipType> {

    private Integer id;
    private Integer relationshipTypeId;
    private String aIsToB;
    private String bIsToA;
    private Integer weight;
    private Boolean preferred;

    public RelationshipTypeBuilder() {
        this.id = getInstanceNumber();
        this.relationshipTypeId = id;
        this.aIsToB = "Caregiver";
        this.bIsToA = "Caretaker";
        this.weight = 0;
        this.preferred = false;
    }

    @Override
    public RelationshipType build() {
        RelationshipType relationshipType = new RelationshipType(relationshipTypeId);
        relationshipType.setId(id);
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
}
