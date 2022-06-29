/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.validator;

import org.openmrs.Concept;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;
import java.util.Optional;

import static org.openmrs.module.messages.api.util.ValidationUtil.resolveErrorCode;
import static org.springframework.validation.ValidationUtils.rejectIfEmpty;

@Handler(supports = CountryProperty.class)
public class CountryPropertyValidator implements Validator {
  @Override
  public boolean supports(Class<?> aClass) {
    return CountryProperty.class.isAssignableFrom(aClass);
  }

  @Override
  public void validate(Object target, Errors errors) {
    final CountryProperty countryProperty = (CountryProperty) target;

    rejectIfEmpty(errors, "name", "CountryProperty.error.name.required");
    rejectIfEmpty(errors, "value", "CountryProperty.error.value.required");

    if (isNew(countryProperty)) {
      validateUniqueness(countryProperty, errors);
    }
  }

  private boolean isNew(CountryProperty countryProperty) {
    return countryProperty.getId() == null;
  }

  private void validateUniqueness(CountryProperty countryProperty, Errors errors) {
    final Optional<CountryProperty> existingProperty =
        Context.getService(CountryPropertyService.class)
            .getCountryProperty(countryProperty.getCountry(), countryProperty.getName());

    if (existingProperty.isPresent()
        && Objects.equals(existingProperty.get().getCountry(), countryProperty.getCountry())) {
      // The error code must be resolved here, the OpenMRS doesn't add arguments to the message
      errors.reject(
          resolveErrorCode(
              "CountryProperty.error.notUnique",
              countryProperty.getName(),
              getCountryNameForErrorMessage(countryProperty.getCountry())));
    }
  }

  private String getCountryNameForErrorMessage(Concept countryConcept) {
    return countryConcept == null ? "<null>" : countryConcept.getName().getName();
  }
}
