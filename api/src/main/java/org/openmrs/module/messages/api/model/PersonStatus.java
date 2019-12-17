package org.openmrs.module.messages.api.model;

public enum PersonStatus {
    NO_CONSENT("person.status.no_consent.title"),
    ACTIVE("person.status.active.title"),
    DEACTIVATE("person.status.deactivate.title");

    private String titleKey;

    PersonStatus(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getTitleKey() {
        return titleKey;
    }
}
