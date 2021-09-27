package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Person;

import java.util.StringJoiner;

public final class PersonUtil {

    public static String getPersonFullName(Person person) {
        StringJoiner sj = new StringJoiner(" ");
        if (StringUtils.isNotBlank(person.getGivenName())) {
            sj.add(person.getGivenName());
        }
        if (StringUtils.isNotBlank(person.getMiddleName())) {
            sj.add(person.getMiddleName());
        }
        if (StringUtils.isNotBlank(person.getFamilyName())) {
            sj.add(person.getFamilyName());
        }

        return sj.toString();
    }

    private PersonUtil() {
    }
}
