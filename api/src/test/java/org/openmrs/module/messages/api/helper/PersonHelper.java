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

import org.openmrs.Person;
import org.openmrs.PersonName;
import org.openmrs.api.context.Context;

public final class PersonHelper {
    
    private PersonHelper() {
    }
    
    public static Person createTestInstance() {
        Person person = new Person();
        person.setGender("M");
        person.setBirthdateEstimated(true);
        person.setDead(false);
        person.setDeathdateEstimated(false);
        
        PersonName name = new PersonName();
        name.setGivenName("test name");
        name.setFamilyName("test family");
        person.addName(name);
        
        return Context.getPersonService().savePerson(person);
    }
}
