package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Person;
import org.openmrs.PersonAddress;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class PersonAddressUtilTest {
  @Mock private ConceptService conceptService;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getConceptService()).thenReturn(conceptService);
  }

  @Test
  public void shouldGetPersonCountryWhenPersonAddressIsNotBlank() {
    Person person = createPersonWithAddress(createTestPersonAddress());

    PersonAddressUtil.getPersonCountry(person);

    verify(conceptService).getConceptByName("testCountry");
  }

  @Test
  public void shouldGetPersonCountryWhenPersonAddressIsBlank() {
    Person person = createPersonWithAddress(null);

    PersonAddressUtil.getPersonCountry(person);

    verify(conceptService, times(0)).getConceptByName(anyString());
  }

  private Person createPersonWithAddress(PersonAddress personAddress) {
    Person person = new Person(100);
    if (personAddress != null) {
      person.addAddress(personAddress);
    }

    return person;
  }

  private PersonAddress createTestPersonAddress() {
    PersonAddress personAddress = new PersonAddress();
    personAddress.setCountry("testCountry");
    personAddress.setCountyDistrict("testCountyDistrict");
    personAddress.setCityVillage("testCity");
    return personAddress;
  }
}
