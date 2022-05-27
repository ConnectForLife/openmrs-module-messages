package org.openmrs.module.messages.web.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.module.messages.api.dto.CountryPropertyValueDTO;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.module.messages.web.model.CountryPropertyValueList;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;

import static org.mockito.Mockito.verify;

@RunWith(PowerMockRunner.class)
public class CountryPropertyControllerTest {

    @InjectMocks
    private CountryPropertyController controller = new CountryPropertyController();

    @Mock
    private CountryPropertyService countryPropertyService;

    @Test
    public void shouldSetValue() {
        CountryPropertyValueList countryPropertyValueList = new CountryPropertyValueList();
        countryPropertyValueList.setValues(Arrays.asList(new CountryPropertyValueDTO()));

        controller.setValue(countryPropertyValueList);

        verify(countryPropertyService).setCountryPropertyValues(countryPropertyValueList.getValues());
    }
}
