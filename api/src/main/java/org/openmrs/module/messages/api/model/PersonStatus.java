package org.openmrs.module.messages.api.model;

import org.openmrs.Person;

import static org.openmrs.module.messages.api.util.PersonAttributeUtil.getPersonStatus;

public enum PersonStatus {
    NO_CONSENT("person.status.no_consent.title"),
    ACTIVE("person.status.active.title"),
    DEACTIVATE("person.status.deactivate.title"),
    MISSING_VALUE("person.status.missing.title");

    private String titleKey;

    PersonStatus(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getTitleKey() {
        return titleKey;
    }

    public static boolean isActive(Person person) {
        return ACTIVE.equals(getPersonStatus(person));
    }
}
