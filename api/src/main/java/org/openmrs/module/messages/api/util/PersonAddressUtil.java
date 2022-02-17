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
