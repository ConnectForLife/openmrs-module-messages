package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CountryPropertyValueDTOTest {

    @Test
    public void shouldBuildTestCountryPropertyValueDTO() {
        CountryPropertyValueDTO actual = buildTestCountryPropertyValueDTO();

        assertNotNull(actual);
        assertEquals("Spain", actual.getCountry());
        assertEquals("messages.bestContactTimeDefault", actual.getName());
        assertEquals("13:00", actual.getValue());
    }

    private CountryPropertyValueDTO buildTestCountryPropertyValueDTO() {
        CountryPropertyValueDTO countryPropertyValueDTO = new CountryPropertyValueDTO();
        countryPropertyValueDTO.setCountry("Spain");
        countryPropertyValueDTO.setName("messages.bestContactTimeDefault");
        countryPropertyValueDTO.setValue("13:00");
        return countryPropertyValueDTO;
    }
}
