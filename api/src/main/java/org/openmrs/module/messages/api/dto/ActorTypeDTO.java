/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;

public class ActorTypeDTO implements Serializable {

    private static final long serialVersionUID = -5236428829247063915L;

    private String uuid;

    private String display;

    public ActorTypeDTO(String uuid, String display) {
        this.uuid = uuid;
        this.display = display;
    }

    public String getUuid() {
        return uuid;
    }

    public ActorTypeDTO setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getDisplay() {
        return display;
    }

    public ActorTypeDTO setDisplay(String display) {
        this.display = display;
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
