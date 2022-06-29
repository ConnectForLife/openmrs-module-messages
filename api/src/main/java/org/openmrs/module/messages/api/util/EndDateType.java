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

public enum EndDateType {
    NO_DATE("NO_DATE"),
    AFTER_TIMES("AFTER_TIMES"),
    DATE_PICKER("DATE_PICKER"),
    OTHER("OTHER");

    private String name;

    EndDateType(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public static EndDateType fromName(String name) {
        for (EndDateType type : EndDateType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Channel type with name '%s' is invalid.",
                name));
    }
}
