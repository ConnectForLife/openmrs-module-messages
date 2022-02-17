package org.openmrs.module.messages.api.service;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.dto.CountryPropertyValueDTO;
import org.openmrs.module.messages.api.model.CountryProperty;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface CountryPropertyService {
  CountryProperty getCountryPropertyByUuid(String uuid);

  Optional<CountryProperty> getCountryProperty(Concept country, String name);

  CountryProperty saveCountryProperty(CountryProperty property);

  CountryProperty retireCountryProperty(CountryProperty property, String reason);

  void purgeCountryProperty(CountryProperty property);

  List<CountryProperty> getAllCountryProperties(
      boolean includeRetired, Integer firstResult, Integer maxResults);

  List<CountryProperty> getAllCountryProperties(
      String namePrefix, boolean includeRetired, Integer firstResult, Integer maxResults);

  long getCountOfCountryProperties(boolean includeRetired);

  long getCountOfCountryProperties(String namePrefix, boolean includeRetired);

  Optional<String> getCountryPropertyValue(Concept country, String name);

  void setCountryPropertyValue(Concept country, String name, String value);

  void setCountryPropertyValue(CountryPropertyValueDTO value);

  void setCountryPropertyValues(Collection<CountryPropertyValueDTO> values);
}
