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
