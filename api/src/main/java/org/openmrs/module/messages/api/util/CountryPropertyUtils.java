package org.openmrs.module.messages.api.util;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;

import java.util.Optional;

/** The utilities related to Country Properties. */
public final class CountryPropertyUtils {
  private CountryPropertyUtils() {}

  /**
   * Creates new default Country Property only if there is no default property yet.
   *
   * <p>A default Country Property is a property where with country equal to null - that indicates
   * the value should be taken if there is no value defined for specific country.
   *
   * @param name the name of property, not null
   * @param value the value, not null
   * @param description the description
   */
  public static void createDefaultCountrySettingIfNotExists(
      String name, String value, String description) {
    final CountryPropertyService propertyService = Context.getService(CountryPropertyService.class);
    final Optional<String> existingProperty = propertyService.getCountryPropertyValue(null, name);

    if (!existingProperty.isPresent()) {
      final CountryProperty newCountryProperty = new CountryProperty();
      newCountryProperty.setName(name);
      newCountryProperty.setDescription(description);
      newCountryProperty.setValue(value);
      propertyService.saveCountryProperty(newCountryProperty);
    }
  }

  /**
   * Sets default Country Property value.
   *
   * <p>If there is no default Country Property, a new property is created.
   *
   * <p>A default Country Property is a property where with country equal to null - that indicates *
   * the value should be taken if there is no value defined for specific country.
   *
   * @param name the property name, not null
   * @param value the value, not null
   */
  public static void setDefaultCountrySetting(String name, String value) {
    final CountryPropertyService propertyService = Context.getService(CountryPropertyService.class);
    final Optional<CountryProperty> existingProperty =
        propertyService.getCountryProperty(null, name);

    if (existingProperty.isPresent()) {
      existingProperty.get().setValue(value);
    } else {
      createDefaultCountrySettingIfNotExists(name, value, null);
    }
  }
}
