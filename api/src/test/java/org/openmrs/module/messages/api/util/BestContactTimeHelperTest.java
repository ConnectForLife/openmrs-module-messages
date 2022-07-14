package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.DefaultContactTimeDTO;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class BestContactTimeHelperTest {

  @Mock private CountryPropertyService countryPropertyService;

  @Before
  public void setUp() {
    mockStatic(Context.class);
    when(Context.getService(CountryPropertyService.class)).thenReturn(countryPropertyService);
  }

  @Test
  public void shouldSetDefaultContactTimes() {
    List<DefaultContactTimeDTO> dtos = createDefaultContactTimeDTOs();

    BestContactTimeHelper.setDefaultContactTimes(dtos);

    verify(countryPropertyService).setCountryPropertyValue(any(), anyString(), anyString());
  }

  @Test
  public void shouldGetBestContactTimes() {
    when(countryPropertyService.getCountryPropertyValue(null, "message.bestContactTime.default"))
        .thenReturn(Optional.empty());

    List<DefaultContactTimeDTO> actual = BestContactTimeHelper.getDefaultContactTimes();

    assertNotNull(actual);
    assertEquals(2, actual.size());
    assertEquals("global", actual.get(0).getActor());
    assertEquals("acec590b-825e-45d2-876a-0028f174903d", actual.get(1).getActor());
  }

  @Test
  public void shouldGetDefaultContactTimesWhenPassedJsonIsInvalid() {
    String invalidBestContactTimeJsonString =
        "{\n"
            + "  \""
            + "global"
            + "\" \""
            + "10:00"
            + "\"\n"
            + "  \"acec590b-825e-45d2-876a-0028f174903d\": \"10:00\"\n"
            + "}";

    when(countryPropertyService.getCountryPropertyValue(null, "message.bestContactTime.default"))
        .thenReturn(Optional.of(invalidBestContactTimeJsonString));

    List<DefaultContactTimeDTO> actual = BestContactTimeHelper.getDefaultContactTimes();

    assertNotNull(actual);
    assertEquals(2, actual.size());
    assertEquals("global", actual.get(0).getActor());
    assertEquals("acec590b-825e-45d2-876a-0028f174903d", actual.get(1).getActor());
  }

  @Test
  public void shouldGetBestContactTime() {
    Person person = createTestPerson(createPersonAttribute("Best contact time", "10:30"));

    String actual = BestContactTimeHelper.getBestContactTime(person);

    assertEquals("10:30", actual);
  }

  @Test
  public void shouldReturnNullValueWhenBestContactTimeIsEmpty() {
    Person person = createTestPerson(createPersonAttribute("Best contact time", ""));

    String actual = BestContactTimeHelper.getBestContactTime(person);

    assertNull(actual);
  }

  @Test
  public void shouldReturnNullValueWhenBestContactIsNotDefinedAtAll() {
    Person person = createTestPerson(null);

    String actual = BestContactTimeHelper.getBestContactTime(person);

    assertNull(actual);
  }

  private List<DefaultContactTimeDTO> createDefaultContactTimeDTOs() {
    return Arrays.asList(
        new DefaultContactTimeDTO("patient", "10:00"), new DefaultContactTimeDTO("actor", "11:00"));
  }

  private Person createTestPerson(PersonAttribute personAttribute) {
    Person person = new Person();
    if (personAttribute != null) {
      person.addAttribute(personAttribute);
    }
    return person;
  }

  private PersonAttribute createPersonAttribute(String personAttributeTypeName, String value) {
    PersonAttribute personAttribute = new PersonAttribute();
    personAttribute.setAttributeType(createPersonAttributeType(personAttributeTypeName));
    personAttribute.setValue(value);
    return personAttribute;
  }

  private PersonAttributeType createPersonAttributeType(String name) {
    PersonAttributeType personAttributeType = new PersonAttributeType();
    personAttributeType.setName(name);
    return personAttributeType;
  }
}
