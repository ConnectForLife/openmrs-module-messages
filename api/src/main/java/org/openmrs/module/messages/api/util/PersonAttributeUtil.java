/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.module.messages.api.model.PersonStatus;

/**
 * Provides the useful set of method which can be used during work with person attributes
 */
public final class PersonAttributeUtil {

    /**
     * Returns the actual person's contact time attribute if it exists
     * @param person - related person
     * @return - the actual contact time or null if not exists
     */
    public static PersonAttribute getBestContactTimeAttribute(Person person) {
        if (person != null) {
            return person.getAttribute(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
        }
        return null;
    }

    /**
     * Returns the actual person's status if it exists
     * @param person - related person
     * @return - the actual person status
     */
    public static PersonStatus getPersonStatus(Person person) {
        PersonAttribute attribute = getPersonStatusAttribute(person);
        return attribute == null ? null : PersonStatus.valueOf(attribute.getValue());
    }

    private static PersonAttribute getPersonStatusAttribute(Person person) {
        if (person != null) {
            return person.getAttribute(ConfigConstants.PATIENT_STATUS_ATTRIBUTE_TYPE_NAME);
        }
        return null;
    }

    private PersonAttributeUtil() {
    }
}
