/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class PersonStatusConfigDTO implements Serializable {

    private static final long serialVersionUID = 271349603627302913L;

    private String name;

    private String backgroundColor;

    private String textColor;

    public String getName() {
        return name;
    }

    public PersonStatusConfigDTO setName(String name) {
        this.name = name;
        return this;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public PersonStatusConfigDTO setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public String getTextColor() {
        return textColor;
    }

    public PersonStatusConfigDTO setTextColor(String textColor) {
        this.textColor = textColor;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
