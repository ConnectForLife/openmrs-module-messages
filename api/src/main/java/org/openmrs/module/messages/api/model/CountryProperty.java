package org.openmrs.module.messages.api.model;

import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.Concept;
import org.openmrs.module.messages.api.constants.MessagesConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The entity to save configuration as key-value pairs, where key is split into a name of the
 * property and a reference to Concept of a relevant country. This is similar to the OpenMRS Global
 * Property, but with an addition of country reference. It is meant to be used to create
 * country-based configuration.
 */
@Entity(name = "messages.CountryProperty")
@Table(name = "messages_country_property")
public class CountryProperty extends BaseOpenmrsMetadata {
  private static final long serialVersionUID = 7121389677014252086L;

  public static final String ID = "id";
  public static final String UUID = "uuid";
  public static final String NAME = "name";
  public static final String DESCRIPTION = "description";
  public static final String COUNTRY = "country";
  public static final String VALUE = "value";
  public static final String RETIRED = "retired";

  @Id
  @GeneratedValue
  @Column(name = "country_property_id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "country_concept_id")
  private Concept country;

  @Column(
      name = "country_property_value",
      nullable = false,
      columnDefinition = "text",
      length = MessagesConstants.MYSQL_TEXT_DATATYPE_LENGTH)
  private String value;

  public CountryProperty() {
    super();
  }

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  public Concept getCountry() {
    return country;
  }

  public void setCountry(Concept country) {
    this.country = country;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
