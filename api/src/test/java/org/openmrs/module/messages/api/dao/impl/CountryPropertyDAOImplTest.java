package org.openmrs.module.messages.api.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CountryPropertyDAOImplTest extends ContextSensitiveTest {
  private static final int COUNTRY_CONCEPT_ID = 1;
  private static final String COUNTRY_CONCEPT_UUID = "936372a1-1fa0-486f-8c3b-c6860ee00202";
  private static final String PROPERTY_NAME = "test.messages.prop";
  private static final String PROPERTY_WITH_COUNTRY_UUID = "1a9859c3-6e86-446c-9511-db9e0dc15c07";
  private static final String PROPERTY_WITHOUT_COUNTRY_UUID =
      "1a9859c3-6e86-446c-9511-db9e0dc15c08";

  @Autowired private CountryPropertyDAOImpl dao;

  @Before
  public void setUp() throws Exception {
    executeDataSet("datasets/CountryPropertyDAOImplTest.xml");
  }

  @Test
  public void getCountryProperty_shouldGetCorrectPropertyForNotNullCountry() {
    final Concept testCountry = Context.getConceptService().getConcept(COUNTRY_CONCEPT_ID);

    final Optional<CountryProperty> countryProperty =
        dao.getCountryProperty(testCountry, PROPERTY_NAME);

    assertTrue("The dao must return a value.", countryProperty.isPresent());
    assertEquals(PROPERTY_NAME, countryProperty.get().getName());
    assertEquals(PROPERTY_WITH_COUNTRY_UUID, countryProperty.get().getUuid());
    assertEquals(COUNTRY_CONCEPT_UUID, countryProperty.get().getCountry().getUuid());
  }

  @Test
  public void getCountryProperty_shouldGetCorrectPropertyForNullCountry() {
    final Optional<CountryProperty> countryProperty = dao.getCountryProperty(null, PROPERTY_NAME);

    assertTrue("The dao must return a value.", countryProperty.isPresent());
    assertEquals(PROPERTY_NAME, countryProperty.get().getName());
    assertEquals(PROPERTY_WITHOUT_COUNTRY_UUID, countryProperty.get().getUuid());
    assertNull("The country must be null.", countryProperty.get().getCountry());
  }
}
