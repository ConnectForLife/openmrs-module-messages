/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

/**
 * Models the messaging controller parameters
 */
public class MessagingParams extends PageableParams {

    /**
     * The id of patient to search for
     */
    private Integer personId;

    private boolean isPatient = true;

    public MessagingParams setPersonId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public Integer getPersonId() {
        return personId;
    }

    public boolean isPatient() {
        return isPatient;
    }

    public MessagingParams setIsPatient(Boolean isPatient) {
        if (isPatient != null) {
            this.isPatient = isPatient;
        }
        return this;
    }

    public PatientTemplateCriteria getCriteria() {
        if (isPatient) {
            return PatientTemplateCriteria.forPatientId(personId);
        }
        return PatientTemplateCriteria.forActorId(personId);
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
