package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;

public enum FrequencyType {
    DAILY("Daily"), WEEKLY("Weekly"), MONTHLY("Monthly");

    private String name;

    FrequencyType(String name) {
        this.name = name;
    }

    public static boolean isValidName(String name) {
        for (FrequencyType type : FrequencyType.values()) {
            if (StringUtils.equalsIgnoreCase(type.name, name)) {
                return true;
            }
        }
        return false;
    }

    public static FrequencyType fromName(String name) {
        for (FrequencyType type : FrequencyType.values()) {
            if (StringUtils.equalsIgnoreCase(type.name, name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Frequency type with name '%s' is " + "invalid.", name));
    }

    public String getName() {
        return name;
    }

    public boolean nameEquals(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
