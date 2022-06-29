/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
