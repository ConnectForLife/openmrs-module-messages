package org.openmrs.module.messages.builder;

import java.util.Date;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.util.DateUtil;

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
        relationshipType = new RelationshipType(1);
        personB = new Person(2);
        startDate = DateUtil.getDatePlusSeconds(1);
        endDate = DateUtil.getDatePlusSeconds(2);
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
