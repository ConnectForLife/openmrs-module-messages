package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;

public final class PersonUtil {

    public static String getPersonFullName(Person person) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(person.getGivenName())) {
            sb.append(person.getGivenName()).append(" ");
        }
        if (StringUtils.isNotBlank(person.getMiddleName())) {
            sb.append(person.getMiddleName()).append(" ");
        }
        if (StringUtils.isNotBlank(person.getFamilyName())) {
            sb.append(person.getFamilyName());
        }

        return sb.toString().trim();
    }

    private PersonUtil() {
    }
}
