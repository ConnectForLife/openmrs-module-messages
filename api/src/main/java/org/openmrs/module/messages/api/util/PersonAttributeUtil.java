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
        return PersonStatus.valueOf(getValue(attribute));
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
