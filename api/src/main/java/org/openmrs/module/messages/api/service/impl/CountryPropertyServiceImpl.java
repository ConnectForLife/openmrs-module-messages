package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.CountryPropertyDAO;
import org.openmrs.module.messages.api.dto.CountryPropertyValueDTO;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.validator.ValidateUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CountryPropertyServiceImpl implements CountryPropertyService {
  private static final String NULL_VALUE_RETIRE_REASON = "Set value to null.";
  private final CountryPropertyDAO countryPropertyDAO;

  public CountryPropertyServiceImpl(CountryPropertyDAO countryPropertyDAO) {
    this.countryPropertyDAO = countryPropertyDAO;
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CountryProperty> getCountryPropertyByUuid(String uuid) {
    return Optional.of(countryPropertyDAO.getByUuid(uuid));
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<CountryProperty> getCountryProperty(Concept country, String name) {
    return countryPropertyDAO.getCountryProperty(country, name);
  }

  @Override
  @Transactional
  public CountryProperty saveCountryProperty(CountryProperty property) {
    ValidateUtil.validate(property);
    return countryPropertyDAO.saveOrUpdate(property);
  }

  @Override
  @Transactional
  public CountryProperty retireCountryProperty(CountryProperty property, String reason) {
    ValidateUtil.validate(property);
    property.setRetired(Boolean.TRUE);
    property.setRetireReason(reason);
    return countryPropertyDAO.saveOrUpdate(property);
  }

  @Override
  @Transactional
  public void purgeCountryProperty(CountryProperty property) {
    countryPropertyDAO.delete(property);
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<String> getCountryPropertyValue(Concept country, String name) {
    return getCountryProperty(country, name).map(CountryProperty::getValue);
  }

  @Override
  @Transactional
  public void setCountryPropertyValue(Concept country, String name, String value) {
    final Optional<CountryProperty> currentCountryProperty =
        countryPropertyDAO.getCountryProperty(country, name);

    if (value != null) {
      final CountryProperty countryProperty =
          currentCountryProperty.orElseGet(() -> createCountryProperty(country, name));
      countryProperty.setValue(value);
      saveCountryProperty(countryProperty);
    } else {
      currentCountryProperty.ifPresent(
          countryProperty -> retireCountryProperty(countryProperty, NULL_VALUE_RETIRE_REASON));
    }
  }

  @Override
  @Transactional
  public void setCountryPropertyValue(CountryPropertyValueDTO value) {
    final Concept country =
        StringUtils.isBlank(value.getCountry())
            ? null
            : Context.getConceptService().getConceptByName(value.getCountry());
    setCountryPropertyValue(country, value.getName(), value.getValue());
  }

  @Override
  @Transactional
  public void setCountryPropertyValues(Collection<CountryPropertyValueDTO> values) {
    final ConceptService conceptService = Context.getConceptService();
    final Map<String, Concept> countryCache = new HashMap<>();

    for (CountryPropertyValueDTO value : values) {
      final Concept country =
          StringUtils.isBlank(value.getCountry())
              ? null
              : countryCache.computeIfAbsent(value.getCountry(), conceptService::getConceptByName);

      setCountryPropertyValue(country, value.getName(), value.getValue());
    }
  }

  @Override
  @Transactional(readOnly = true)
  public List<CountryProperty> getAllCountryProperties(
      boolean includeRetired, Integer firstResult, Integer maxResults) {
    return countryPropertyDAO.getAll(includeRetired, firstResult, maxResults);
  }

  @Override
  @Transactional(readOnly = true)
  public List<CountryProperty> getAllCountryProperties(
      String namePrefix, boolean includeRetired, Integer firstResult, Integer maxResults) {
    return countryPropertyDAO.getAll(namePrefix, includeRetired, firstResult, maxResults);
  }

  @Override
  @Transactional(readOnly = true)
  public long getCountOfCountryProperties(boolean includeRetired) {
    return countryPropertyDAO.getAllCount(includeRetired);
  }

  @Override
  @Transactional(readOnly = true)
  public long getCountOfCountryProperties(String namePrefix, boolean includeRetired) {
    return countryPropertyDAO.getAllCount(namePrefix, includeRetired);
  }

  private CountryProperty createCountryProperty(Concept country, String name) {
    final CountryProperty newCountryProperty = new CountryProperty();
    newCountryProperty.setName(name);
    newCountryProperty.setCountry(country);
    return newCountryProperty;
  }
}
