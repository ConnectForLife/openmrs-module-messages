package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openmrs.Concept;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.messages.api.dao.CountryPropertyDAO;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.impl.CountryPropertyServiceImpl;
import org.openmrs.test.BaseContextMockTest;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CountryPropertyServiceImplTest extends BaseContextMockTest {

  @Mock protected AdministrationService administrationServiceMock;

  @Before
  public void setup() {
    contextMockHelper.setService(AdministrationService.class, administrationServiceMock);
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
    final CountryPropertyDAO daoMock = mock(CountryPropertyDAO.class);
    final CountryProperty countryPropertyTest = Mockito.spy(createTestCountryProperty(null));
    final String reason = "This is retire reason.";

    final CountryPropertyServiceImpl service = new CountryPropertyServiceImpl(daoMock);

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
    final CountryPropertyDAO daoMock = mock(CountryPropertyDAO.class);
    final Concept country = mock(Concept.class);
    final String propertyName = "test.property.name";
    final String propertyValue = "This is new value.";
    final CountryPropertyServiceImpl service = new CountryPropertyServiceImpl(daoMock);

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
    final CountryPropertyDAO daoMock = mock(CountryPropertyDAO.class);
    final CountryProperty countryPropertyTest =
        Mockito.spy(createTestCountryProperty(mock(Concept.class)));
    final String propertyValue = "This is new value.";
    final CountryPropertyServiceImpl service = new CountryPropertyServiceImpl(daoMock);

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

  private CountryProperty createTestCountryProperty(Concept country) {
    final CountryProperty countryProperty = new CountryProperty();
    countryProperty.setId(12);
    countryProperty.setName("CountryPropertyServiceImplTest.prop");
    countryProperty.setCountry(country);
    return countryProperty;
  }
}
