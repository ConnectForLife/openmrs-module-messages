package org.openmrs.module.messages.api.model;

public enum PatientStatus {
    NO_CONSENT("patient.status.no_consent.title"),
    ACTIVE("patient.status.active.title"),
    DEACTIVATE("patient.status.deactivate.title");

    private String titleKey;

    PatientStatus(String titleKey) {
        this.titleKey = titleKey;
    }

    public String getTitleKey() {
        return titleKey;
    }
}
