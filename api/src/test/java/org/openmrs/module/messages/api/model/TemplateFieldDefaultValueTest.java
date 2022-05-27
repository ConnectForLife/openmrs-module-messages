package org.openmrs.module.messages.api.model;

import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TemplateFieldDefaultValueTest {

  private final Person patient = new Person(1);

  private final Person caregiver = new Person(2);

  @Test
  public void shouldCheckRelationWhenRelationshipTypeDirectionIsB() {
    TemplateFieldDefaultValue templateFieldDefaultValue = new TemplateFieldDefaultValue();
    templateFieldDefaultValue.setRelationshipType(
        createTestRelationshipType("Patient", "Caregiver"));
    templateFieldDefaultValue.setDirection(RelationshipTypeDirection.B);
    Actor actor = new Actor(caregiver, createTestRelationshipWithDefaultRelationshipType());

    boolean actual = templateFieldDefaultValue.isRelatedTo(actor);

    assertTrue(actual);
  }

  @Test
  public void shouldCheckRelationWhenRelationshipTypeDirectionIsA() {
    TemplateFieldDefaultValue templateFieldDefaultValue = new TemplateFieldDefaultValue();
    templateFieldDefaultValue.setRelationshipType(
            createTestRelationshipType("Patient", "Caregiver"));
    templateFieldDefaultValue.setDirection(RelationshipTypeDirection.A);
    Actor actor = new Actor(caregiver, createTestRelationshipWithDefaultRelationshipType());

    boolean actual = templateFieldDefaultValue.isRelatedTo(actor);

    assertFalse(actual);
  }

  private Relationship createTestRelationshipWithDefaultRelationshipType() {
    Relationship relationship = new Relationship();
    relationship.setRelationshipType(createTestRelationshipType("Patient", "Caregiver"));
    relationship.setPersonA(patient);
    relationship.setPersonB(caregiver);
    return relationship;
  }

  private RelationshipType createTestRelationshipType(String aToB, String bToA) {
    RelationshipType relationshipType = new RelationshipType();
    relationshipType.setaIsToB(aToB);
    relationshipType.setbIsToA(bToA);
    return relationshipType;
  }
}
