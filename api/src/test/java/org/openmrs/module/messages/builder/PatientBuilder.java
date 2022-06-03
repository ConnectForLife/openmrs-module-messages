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

import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.Person;

public final class PatientBuilder extends AbstractBuilder<Patient> {

    private Integer id;

    private Person person;

    private PatientIdentifier identifier;

    public PatientBuilder() {
        super();
        id = getInstanceNumber();
        person = new PersonBuilder().build();
        identifier = new PatientIdentifierBuilder().build();
    }

    @Override
    public Patient build() {
        Patient patient = new Patient(person);
        patient.setId(id);
        patient.addIdentifier(identifier);
        return patient;
    }

    @Override
    public Patient buildAsNew() {
        return withId(null).build();
    }

    public PatientBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public PatientBuilder withPerson(Person person) {
        this.person = person;
        return this;
    }

    public PatientBuilder withIdentifier(PatientIdentifier identifier) {
        this.identifier = identifier;
        return this;
    }
}
