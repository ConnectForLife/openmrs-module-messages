package org.openmrs.module.messages.api.helper;

import org.openmrs.Relationship;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.util.TestConstants;

public final class RelationshipHelper {
    
    private RelationshipHelper() {
    }
    
    public static Relationship createTestInstance() {
        Relationship relationship = new Relationship();
        relationship.setPersonA(Context.getPersonService().getPerson(TestConstants.TEST_PERSON_A_ID));
        relationship.setPersonB(Context.getPersonService().getPerson(TestConstants.TEST_PERSON_B_ID));
        relationship.setRelationshipType(Context.getPersonService()
                .getRelationshipType(TestConstants.TEST_RELATIONSHIP_TYPE_ID));
        
        return Context.getPersonService().saveRelationship(relationship);
    }
}
