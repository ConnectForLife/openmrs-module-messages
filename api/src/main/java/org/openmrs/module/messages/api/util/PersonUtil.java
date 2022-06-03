/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
