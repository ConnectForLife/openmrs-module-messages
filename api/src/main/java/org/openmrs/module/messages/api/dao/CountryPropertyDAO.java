package org.openmrs.module.messages.api.dao;

import org.openmrs.Concept;
import org.openmrs.api.db.OpenmrsMetadataDAO;
import org.openmrs.module.messages.api.model.CountryProperty;

import java.util.List;
import java.util.Optional;

public interface CountryPropertyDAO extends OpenmrsMetadataDAO<CountryProperty> {
  Optional<CountryProperty> getCountryProperty(Concept country, String name);

  List<CountryProperty> getAll(String namePrefix, boolean includeRetired, Integer firstResult, Integer maxResults);

  int getAllCount(String namePrefix,boolean includeRetired);
}
