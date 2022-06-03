/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.openmrs.Concept;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.api.context.Context;

import java.util.Optional;

public class PersonAddressUtil {
  private PersonAddressUtil() {}

  public static Optional<Concept> getPersonCountry(Person person) {
    final PersonAddress personAddress = person.getPersonAddress();

    if (personAddress == null) {
      return Optional.empty();
    }

    final Concept countryConcept =
        Context.getConceptService().getConceptByName(personAddress.getCountry());

    return Optional.ofNullable(countryConcept);
  }
}
