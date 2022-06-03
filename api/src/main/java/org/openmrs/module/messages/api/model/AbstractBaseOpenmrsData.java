/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.BaseOpenmrsData;

public abstract class AbstractBaseOpenmrsData extends BaseOpenmrsData {
    
    private static final String ID_FIELD_NAME = "id";
    private static final long serialVersionUID = 8907890539048617187L;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        return EqualsBuilder.reflectionEquals(this, o, ID_FIELD_NAME);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, ID_FIELD_NAME);
    }
}
