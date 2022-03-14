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
