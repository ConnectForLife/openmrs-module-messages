package org.openmrs.module.messages.api.service;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.dto.CountryPropertyValueDTO;
import org.openmrs.module.messages.api.model.CountryProperty;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The CountryPropertyService API.
 *
 * <p>The Country Properties are meant to be used to store country-based configuration, similarly to
 * the OpenMRS Global Properties. The Country Properties without country (null value) are to be use
 * as default values.
 *
 * <p>The service instance is available from OpenMRS Context.
 */
public interface CountryPropertyService {
  /**
   * @param uuid the UUID of Country Property, not null
   * @return the Optional with Country Property with the {@code uuid}
   */
  Optional<CountryProperty> getCountryPropertyByUuid(String uuid);

  /**
   * @param country the Concept representing a country
   * @param name the property name, not null
   * @return the Optional with Country Property with {@code country} and {@code name}, if there is no value for
   * {@code country} then a value for 'null' country is returned
   */
  Optional<CountryProperty> getCountryProperty(Concept country, String name);

  /**
   * Saves {@code property} as-is.
   *
   * @param property the property to save
   * @return the saved property, never null
   */
  CountryProperty saveCountryProperty(CountryProperty property);

  /**
   * Retires the {@code property} with a {@code reason}.
   *
   * @param property the property to retire, not null
   * @param reason the reason of retirement
   * @return the retired property, never null
   */
  CountryProperty retireCountryProperty(CountryProperty property, String reason);

  /**
   * Purge the {@code property} from database.
   *
   * @param property the property to purge from database, not null
   */
  void purgeCountryProperty(CountryProperty property);

  /**
   * @param includeRetired whether the result should include retired Country Properties
   * @param firstResult the index of the first result to return (offset), not null
   * @param maxResults the maximum number of results, not null
   * @return the List of Country Properties, never null
   */
  List<CountryProperty> getAllCountryProperties(
      boolean includeRetired, Integer firstResult, Integer maxResults);

  /**
   * @param namePrefix the prefix which returned property names must start with, not null
   * @param includeRetired whether the result should include retired Country Properties
   * @param firstResult the index of the first result to return (offset), not null
   * @param maxResults the maximum number of results, not null
   * @return the List of Country Properties, never null
   */
  List<CountryProperty> getAllCountryProperties(
      String namePrefix, boolean includeRetired, Integer firstResult, Integer maxResults);

  /**
   * @param includeRetired whether the result should include retired Country Properties
   * @return the total count of Country Properties
   */
  long getCountOfCountryProperties(boolean includeRetired);

  /**
   * @param namePrefix the prefix which counted property names must start with, not null
   * @param includeRetired whether the result should include retired Country Properties
   * @return the total count of Country Properties
   */
  long getCountOfCountryProperties(String namePrefix, boolean includeRetired);

  /**
   * @param country the Concept representing a country
   * @param name the property name, not null
   * @return the Optional with Country Property value, if there is no value for {@code country}
   *     then a value for 'null' country is returned
   */
  Optional<String> getCountryPropertyValue(Concept country, String name);

  /**
   * Sets a new {@code value} for Country Property with {@code country} and {@code name}.
   *
   * <p>The method creates new Country Property or updates existing as needed.
   *
   * <p>The {@code null} value indicates that an existing Country Property must be retired.
   *
   * @param country the Concept representing a country
   * @param name the property name, not null
   * @param value the new value to set
   */
  void setCountryPropertyValue(Concept country, String name, String value);

  /**
   * Sets a new value for Country Property.
   *
   * <p>The method creates new Country Property or updates existing as needed.
   *
   * <p>The {@code null} value indicates that an existing Country Property must be retired.
   *
   * <p>IN case of multiple DTOs, the {@code setCountryPropertyValues} method which accepts
   * collection is more efficient then calling this method multiple times.
   *
   * @param value the DTO with Country Property data, not null
   * @see #setCountryPropertyValue(Concept, String, String)
   * @see #setCountryPropertyValues(Collection)
   */
  void setCountryPropertyValue(CountryPropertyValueDTO value);

  /**
   * Sets a new values for Country Properties.
   *
   * <p>The method creates new Country Property or updates existing as needed.
   *
   * <p>The {@code null} value indicates that an existing Country Property must be retired.
   *
   * @param values the DTOs with Country Property data, not null
   * @see #setCountryPropertyValue(Concept, String, String)
   */
  void setCountryPropertyValues(Collection<CountryPropertyValueDTO> values);
}
