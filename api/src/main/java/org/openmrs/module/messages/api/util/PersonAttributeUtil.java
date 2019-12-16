package org.openmrs.module.messages.api.util;

import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.module.messages.api.model.PatientStatus;

public final class PersonAttributeUtil {

    public static PersonAttribute getBestContactTimeAttribute(Person person) {
        return person.getAttribute(ConfigConstants.PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME);
    }

    public static PatientStatus getPersonStatus(Person person) {
        PersonAttribute attribute = getPersonStatusAttribute(person);
        if (attribute == null) {
            return null;
        }
        return PatientStatus.valueOf(getValue(attribute));
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
            PatientStatus status = PatientStatus.valueOf(attribute.getValue());
            result = status.getTitleKey();
        } catch (IllegalArgumentException ex) {
            result = attribute.getValue();
        }
        return result;
    }

    private PersonAttributeUtil() {
    }
}
