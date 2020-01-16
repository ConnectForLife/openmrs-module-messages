/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
        // TODO: CFLM-528 Replace enum validation with GP or Concepts validation.
        for (ChannelType type : ChannelType.values()) {
            if (StringUtils.equalsIgnoreCase(type.name, name)) {
                return type;
            }
        }
        throw new IllegalArgumentException(String.format("Channel type with name '%s' is invalid.",
                name));
    }

    public boolean nameEquals(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}

