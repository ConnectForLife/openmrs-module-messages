package org.openmrs.module.messages.validator;

import org.openmrs.Concept;
import org.openmrs.annotation.Handler;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
    final Optional<String> existingProperty =
        Context.getService(CountryPropertyService.class)
            .getCountryPropertyValue(countryProperty.getCountry(), countryProperty.getName());

    if (existingProperty.isPresent()) {
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
