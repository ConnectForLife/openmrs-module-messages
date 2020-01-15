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
