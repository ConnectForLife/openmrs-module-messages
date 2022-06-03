/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Location;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;

public final class PatientIdentifierBuilder extends AbstractBuilder<PatientIdentifier> {

    private Integer id;

    private Location location;

    private PatientIdentifierType identifierType;

    private String value;

    public PatientIdentifierBuilder() {
        super();
        id = getInstanceNumber();
        value = "no-unique-identifier";
    }

    @Override
    public PatientIdentifier build() {
        PatientIdentifier patientIdentifier = new PatientIdentifier();
        patientIdentifier.setId(id);
        patientIdentifier.setLocation(location);
        patientIdentifier.setIdentifierType(identifierType);
        patientIdentifier.setIdentifier(value);
        return patientIdentifier;
    }

    @Override
    public PatientIdentifier buildAsNew() {
        return withId(null).build();
    }

    public PatientIdentifierBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PatientIdentifierBuilder withLocation(Location location) {
        this.location = location;
        return this;
    }

    public PatientIdentifierBuilder withIdentifierType(PatientIdentifierType identifierType) {
        this.identifierType = identifierType;
        return this;
    }

    public PatientIdentifierBuilder withValue(String value) {
        this.value = value;
        return this;
    }
}
