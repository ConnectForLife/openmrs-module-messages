package org.openmrs.module.messages.api.util;

public enum EndDateType {
    NO_DATE("NO_DATE"),
    AFTER_TIMES("AFTER_TIMES"),
    DATE_PICKER("DATE_PICKER");

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
