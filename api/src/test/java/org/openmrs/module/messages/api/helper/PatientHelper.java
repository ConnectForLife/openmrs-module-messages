/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.helper;

import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.util.TestConstants;

import java.util.UUID;

public final class PatientHelper {
    
    private PatientHelper() {
    }
    
    public static Patient createTestInstance() {
        PersonName name = new PersonName();
        name.setGivenName("test name");
        name.setFamilyName("test family");
        Patient patient = new Patient();
        patient.addName(name);
        patient.setGender("M");
        
        PatientIdentifier identifier = new PatientIdentifier();
        identifier.setIdentifier(UUID.randomUUID().toString());
        identifier.setLocation(Context.getLocationService().getLocation(TestConstants.TEST_LOCATION_ID));
        identifier.setIdentifierType(Context.getPatientService()
                .getPatientIdentifierType(TestConstants.TEST_PATIENT_IDENTIFIER_ID));
        patient.addIdentifier(identifier);
        
        return Context.getPatientService().savePatient(patient);
    }
}
