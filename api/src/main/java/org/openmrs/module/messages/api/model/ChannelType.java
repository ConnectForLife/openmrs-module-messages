package org.openmrs.module.messages.api.model;

public enum ChannelType {
    CALL("Call"),
    SMS("SMS"),
    DEACTIVATED("Deactivate service");

    private String name;

    ChannelType(String value) {
        this.name = value;
    }

    public String getName() {
        return name;
    }

    public static ChannelType fromName(String name) {
        for (ChannelType type : ChannelType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Channel type with name '%s' is invalid.",
            name));
    }
}

