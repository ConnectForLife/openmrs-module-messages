package org.openmrs.module.messages.api.model;

import org.apache.commons.lang.StringUtils;

public enum ChannelType {
    CALL("Call"),
    SMS("SMS"),
    DEACTIVATED("Deactivate service");

    private String name;

    ChannelType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ChannelType fromName(String name) {
        for (ChannelType type : ChannelType.values()) {
            if (StringUtils.equalsIgnoreCase(type.name, name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Channel type with name '%s' is invalid.",
            name));
    }
}

