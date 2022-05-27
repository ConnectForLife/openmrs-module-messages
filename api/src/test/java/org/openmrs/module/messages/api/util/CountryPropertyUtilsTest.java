package org.openmrs.module.messages.api.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class CountryPropertyUtilsTest {

    @Mock
    private CountryPropertyService countryPropertyService;

    @Before
    public void setUp() {
        mockStatic(Context.class);

        when(Context.getService(CountryPropertyService.class)).thenReturn(countryPropertyService);
    }

    @Test
    public void shouldCreateDefaultCountrySettingIfNotExists() {
        when(countryPropertyService.getCountryPropertyValue(any(), any())).thenReturn(Optional.empty());

        CountryPropertyUtils.createDefaultCountrySettingIfNotExists("testPropName", "testPropValue", "testPropDescription");

        verify(countryPropertyService).saveCountryProperty(any(CountryProperty.class));
    }

    @Test
    public void shouldSetDefaultValueForExistingCountrySetting() {
        CountryProperty testCountryProperty = new CountryProperty();
        when(countryPropertyService.getCountryProperty(any(), anyString())).thenReturn(Optional.of(testCountryProperty));

        CountryPropertyUtils.setDefaultCountrySetting("testPropName", "testPropValue");

        assertEquals("testPropValue", testCountryProperty.getValue());
    }
}
