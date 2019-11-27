package org.openmrs.module.messages.api.helper;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.util.TestConstants;

public final class RelationshipTypeHelper {
    
    private RelationshipTypeHelper() {
    }
    
    public static RelationshipType createTestInstance() {
        RelationshipType relationshipType = new RelationshipType();
        relationshipType.setaIsToB("father");
        relationshipType.setbIsToA("child");
        relationshipType.setPreferred(true);
        relationshipType.setWeight(TestConstants.TEST_WEIGHT);
        relationshipType.setRetired(true);
        
        return relationshipType;
    }
}
