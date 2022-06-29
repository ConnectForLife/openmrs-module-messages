/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

/**
 * Represents a person status config DTO
 */
public class PersonStatusConfigDTO extends BaseDTO {

    private static final long serialVersionUID = 3777566870351735347L;

    private String name;

    private String style;

    public String getName() {
        return name;
    }

    public PersonStatusConfigDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public PersonStatusConfigDTO setStyle(String style) {
        this.style = style;
        return this;
    }
}
