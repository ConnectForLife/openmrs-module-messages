package org.openmrs.module.messages.api.util;

import org.openmrs.Person;
import org.openmrs.PersonAttribute;

public final class PersonAttributeUtil {

    public static PersonAttribute getBestContactTimeAttribute(Person person) {
        return person.getAttribute(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
    }

    private PersonAttributeUtil() {
    }
}
