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

public final class PersonAttributeUtil {

    public static PersonAttribute getBestContactTimeAttribute(Person person) {
        return person.getAttribute(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
    }

    public static PersonStatus getPersonStatus(Person person) {
        PersonAttribute attribute = getPersonStatusAttribute(person);
        if (attribute == null) {
            return null;
        }
        return PersonStatus.valueOf(attribute.getValue());
    }

    public static PersonAttribute getPersonStatusAttribute(Person person) {
        if (person != null) {
            return person.getAttribute(ConfigConstants.PATIENT_STATUS_ATTRIBUTE_TYPE_NAME);
        }
        return null;
    }

    public static String getValue(PersonAttribute attribute) {
        String result;
        try {
            PersonStatus status = PersonStatus.valueOf(attribute.getValue());
            result = status.getTitleKey();
        } catch (IllegalArgumentException ex) {
            result = attribute.getValue();
        }
        return result;
    }

    private PersonAttributeUtil() {
    }
}
