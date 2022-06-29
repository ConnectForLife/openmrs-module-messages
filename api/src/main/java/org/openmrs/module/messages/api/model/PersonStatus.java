/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.openmrs.Person;

import static org.openmrs.module.messages.api.util.PersonAttributeUtil.getPersonStatus;

public enum PersonStatus {
    NO_CONSENT("person.status.no_consent.title"),
    ACTIVATED("person.status.activated.title"),
    DEACTIVATED("person.status.deactivated.title"),
    MISSING_VALUE("person.status.missing.title");

    private String titleKey;

    PersonStatus(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public static boolean isActive(Person person) {
        return ACTIVATED == getPersonStatus(person);
    }
}
