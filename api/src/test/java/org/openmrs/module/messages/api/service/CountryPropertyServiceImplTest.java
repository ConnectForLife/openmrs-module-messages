/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.Concept;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.module.messages.api.dao.CountryPropertyDAO;
import org.openmrs.module.messages.api.dto.CountryPropertyValueDTO;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.impl.CountryPropertyServiceImpl;
import org.openmrs.test.BaseContextMockTest;

import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CountryPropertyServiceImplTest extends BaseContextMockTest {

  @Mock protected AdministrationService administrationServiceMock;

  @Mock protected ConceptService conceptServiceMock;

  private final CountryPropertyDAO daoMock = mock(CountryPropertyDAO.class);

  private final CountryPropertyServiceImpl service = new CountryPropertyServiceImpl(daoMock);

  @Before
  public void setup() {
    contextMockHelper.setService(AdministrationService.class, administrationServiceMock);
    contextMockHelper.setService(ConceptService.class, conceptServiceMock);
  }

  @Test
  public void getCountryProperty_shouldReturnValueForSpecificCountry() {
    final CountryPropertyDAO daoMock = mock(CountryPropertyDAO.class);
    final Concept specificCountry = mock(Concept.class);
    final CountryProperty defaultCountryProperty = Mockito.spy(createTestCountryProperty(null));
    final CountryProperty specificCountryProperty = Mockito.spy(createTestCountryProperty(specificCountry));

    when(daoMock.getCountryProperty(null, defaultCountryProperty.getName()))
        .thenReturn(Optional.of(defaultCountryProperty));
    when(daoMock.getCountryProperty(specificCountry, specificCountryProperty.getName()))
        .thenReturn(Optional.of(specificCountryProperty));

    final CountryPropertyServiceImpl service = new CountryPropertyServiceImpl(daoMock);

    final Optional<CountryProperty> result = service.getCountryProperty(specificCountry,
        defaultCountryProperty.getName());

    assertTrue(result.isPresent());
    assertEquals(specificCountryProperty.getUuid(), result.get().getUuid());
  }

  @Test
  public void getCountryProperty_shouldReturnDefaultValue() {
    final CountryPropertyDAO daoMock = mock(CountryPropertyDAO.class);
    final Concept specificCountry = mock(Concept.class);
    final Concept otherCountry = mock(Concept.class);
    final CountryProperty defaultCountryProperty = Mockito.spy(createTestCountryProperty(null));
    final CountryProperty specificCountryProperty = Mockito.spy(createTestCountryProperty(specificCountry));

    when(daoMock.getCountryProperty(null, defaultCountryProperty.getName()))
        .thenReturn(Optional.of(defaultCountryProperty));
    when(daoMock.getCountryProperty(specificCountry, specificCountryProperty.getName()))
        .thenReturn(Optional.of(specificCountryProperty));
    when(daoMock.getCountryProperty(otherCountry, specificCountryProperty.getName()))
        .thenReturn(Optional.empty());

    final CountryPropertyServiceImpl service = new CountryPropertyServiceImpl(daoMock);

    final Optional<CountryProperty> result = service.getCountryProperty(otherCountry,
        defaultCountryProperty.getName());

    assertTrue(result.isPresent());
    assertEquals(defaultCountryProperty.getUuid(), result.get().getUuid());
  }

  @Test
  public void retireCountryProperty_shouldSetReason() {
    final CountryProperty countryPropertyTest = Mockito.spy(createTestCountryProperty(null));
    final String reason = "This is retire reason.";

    service.retireCountryProperty(countryPropertyTest, reason);

    verify(countryPropertyTest).setRetired(true);
    verify(countryPropertyTest).setRetireReason(reason);

    final ArgumentCaptor<CountryProperty> savedPropertyCapture =
        ArgumentCaptor.forClass(CountryProperty.class);
    verify(daoMock).saveOrUpdate(savedPropertyCapture.capture());
    assertEquals(countryPropertyTest.getId(), savedPropertyCapture.getValue().getId());
  }

  @Test
  public void setCountryPropertyValue_shouldCreateNewPropertyIfNoneExists() {
    final Concept country = mock(Concept.class);
    final String propertyName = "test.property.name";
    final String propertyValue = "This is new value.";

    when(daoMock.getCountryProperty(country, propertyName)).thenReturn(Optional.empty());

    service.setCountryPropertyValue(country, propertyName, propertyValue);

    final ArgumentCaptor<CountryProperty> savedPropertyCapture =
        ArgumentCaptor.forClass(CountryProperty.class);
    verify(daoMock).saveOrUpdate(savedPropertyCapture.capture());

    final CountryProperty capturedProperty = savedPropertyCapture.getValue();
    assertEquals(propertyName, capturedProperty.getName());
    assertEquals(country, capturedProperty.getCountry());
    assertEquals(propertyValue, capturedProperty.getValue());
  }

  @Test
  public void setCountryPropertyValue_shouldUpdateExistingIfAnyExists() {
    final CountryProperty countryPropertyTest =
        Mockito.spy(createTestCountryProperty(mock(Concept.class)));
    final String propertyValue = "This is new value.";

    when(daoMock.getCountryProperty(
            countryPropertyTest.getCountry(), countryPropertyTest.getName()))
        .thenReturn(Optional.of(countryPropertyTest));

    service.setCountryPropertyValue(
        countryPropertyTest.getCountry(), countryPropertyTest.getName(), propertyValue);

    final ArgumentCaptor<CountryProperty> savedPropertyCapture =
        ArgumentCaptor.forClass(CountryProperty.class);
    verify(daoMock).saveOrUpdate(savedPropertyCapture.capture());

    final CountryProperty capturedProperty = savedPropertyCapture.getValue();
    assertEquals(countryPropertyTest.getName(), capturedProperty.getName());
    assertEquals(countryPropertyTest.getCountry(), capturedProperty.getCountry());
    assertEquals(propertyValue, capturedProperty.getValue());
    assertEquals(countryPropertyTest.getUuid(), capturedProperty.getUuid());
  }

  @Test
  public void shouldGetCountryPropertyByUuid() {
    String testUuid = "aaaa-bbbb-cccc-dddd";
    when(daoMock.getByUuid(testUuid)).thenReturn(createTestCountryProperty(new Concept()));

    service.getCountryPropertyByUuid(testUuid);

    verify(daoMock).getByUuid(testUuid);
  }

  @Test
  public void shouldGetCountryPropertyByCountryAndName() {
    when(daoMock.getCountryProperty(any(Concept.class), anyString()))
            .thenReturn(Optional.of(new CountryProperty()));

    service.getCountryProperty(any(Concept.class), anyString());

    verify(daoMock).getCountryProperty(any(Concept.class), anyString());
  }

  @Test
  public void shouldSaveCountryProperty() {
    service.saveCountryProperty(new CountryProperty());

    verify(daoMock).saveOrUpdate(any(CountryProperty.class));
  }

  @Test
  public void shouldRetireCountryProperty() {
    service.retireCountryProperty(new CountryProperty(), "test reason");

    verify(daoMock).saveOrUpdate(any(CountryProperty.class));
  }

  @Test
  public void shouldPurgeCountryProperty() {
    service.purgeCountryProperty(new CountryProperty());

    verify(daoMock).delete(any(CountryProperty.class));
  }

  @Test
  public void shouldGetCountryPropertyValue() {
    CountryProperty countryProperty = createTestCountryProperty(new Concept());
    countryProperty.setValue("testValue");
    when(daoMock.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.of(countryProperty));

    Optional<String> actual = service.getCountryPropertyValue(any(Concept.class), anyString());

    assertTrue(actual.isPresent());
    assertEquals("testValue", actual.get());
  }

  @Test
  public void shouldSetCountryPropertyValueByCountryAndNameAndValueWhenValueAndCountryPropertyAreNotBlank() {;
    when(daoMock.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.of(createTestCountryProperty(new Concept())));

    service.setCountryPropertyValue(new Concept(), "testName", "testValue");

    verify(daoMock).saveOrUpdate(any(CountryProperty.class));
  }

  @Test
  public void shouldSetCountryPropertyValueByCountryAndNameAndValueWhenCountryPropertyIsBlankAndValueIsNotBlank() {
    when(daoMock.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.empty());

    service.setCountryPropertyValue(new Concept(), "testName", "testValue");

    verify(daoMock).saveOrUpdate(any(CountryProperty.class));
  }

  @Test
  public void shouldSetCountryPropertyValueByCountryAndNameAndValueWhenValueIsBlank() {
    when(daoMock.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.of(createTestCountryProperty(new Concept())));

    service.setCountryPropertyValue(new Concept(), "testName", null);

    verify(daoMock).saveOrUpdate(any(CountryProperty.class));
  }

  @Test
  public void shouldSetCountryPropertyValueByCountryPropertyValueDTO() {
    when(daoMock.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.of(createTestCountryProperty(new Concept())));

    CountryPropertyValueDTO countryPropertyValueDTO = createTestCountryPropertyValueDTO();

    service.setCountryPropertyValue(countryPropertyValueDTO);

    verify(conceptServiceMock).getConceptByName("Spain");
  }

  @Test
  public void shouldSetCountryPropertyValuesByCountryPropertyValueDTOs() {
    when(daoMock.getCountryProperty(any(Concept.class), anyString())).thenReturn(Optional.of(createTestCountryProperty(new Concept())));

    CountryPropertyValueDTO countryPropertyValueDTO = createTestCountryPropertyValueDTO();

    service.setCountryPropertyValues(Collections.singleton(countryPropertyValueDTO));

    verify(conceptServiceMock).getConceptByName("Spain");
  }

  @Test
  public void shouldGetAllNonVoidedCountryProperties() {
    service.getAllCountryProperties(false, 1, 100);

    verify(daoMock).getAll(false, 1, 100);
  }

  @Test
  public void shouldGetAllNonVoidedCountryPropertiesByPrefix() {
    service.getAllCountryProperties("test", false, 1, 100);

    verify(daoMock).getAll("test", false, 1, 100);
  }

  @Test
  public void getCountOfAllNonVoidedCountryProperties() {
    service.getCountOfCountryProperties(false);

    verify(daoMock).getAllCount(false);
  }

  @Test
  public void getCountOfAllNonVoidedCountryPropertiesByPrefix() {
    service.getCountOfCountryProperties("test", false);

    verify(daoMock).getAllCount("test", false);
  }

  private CountryProperty createTestCountryProperty(Concept country) {
    final CountryProperty countryProperty = new CountryProperty();
    countryProperty.setId(12);
    countryProperty.setName("CountryPropertyServiceImplTest.prop");
    countryProperty.setCountry(country);
    return countryProperty;
  }

  private CountryPropertyValueDTO createTestCountryPropertyValueDTO() {
    CountryPropertyValueDTO countryPropertyValueDTO = new CountryPropertyValueDTO();
    countryPropertyValueDTO.setCountry("Spain");
    countryPropertyValueDTO.setName("defaultBestContactTime");
    countryPropertyValueDTO.setValue("11:00");
    return countryPropertyValueDTO;
  }
}
