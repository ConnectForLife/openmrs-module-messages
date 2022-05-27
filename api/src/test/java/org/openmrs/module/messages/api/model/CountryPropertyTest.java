package org.openmrs.module.messages.api.model;

import org.junit.Test;
import org.openmrs.Concept;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CountryPropertyTest {

    private final Concept country = new Concept(100);

    @Test
    public void shouldCreateCountryPropertyObjectProperly() {
        CountryProperty actual = createCountryProperty();

        assertNotNull(actual);
        assertEquals(Integer.valueOf(5), actual.getId());
        assertEquals(country, actual.getCountry());
        assertEquals("messages.smsConfig", actual.getName());
        assertEquals("defaultSms", actual.getValue());
    }

    private CountryProperty createCountryProperty() {
        CountryProperty countryProperty = new CountryProperty();
        countryProperty.setId(5);
        countryProperty.setCountry(country);
        countryProperty.setName("messages.smsConfig");
        countryProperty.setValue("defaultSms");
        return countryProperty;
    }
}
