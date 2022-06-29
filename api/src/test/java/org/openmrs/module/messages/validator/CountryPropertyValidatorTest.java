/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.validator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.messages.api.model.CountryProperty;
import org.openmrs.module.messages.api.service.CountryPropertyService;
import org.openmrs.test.BaseContextMockTest;
import org.springframework.validation.Errors;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CountryPropertyValidatorTest extends BaseContextMockTest {
  @Mock private CountryPropertyService countryPropertyServiceMock;
  @Mock private MessageSourceService messageSourceService;

  @Before
  public void setup() {
    contextMockHelper.setService(CountryPropertyService.class, countryPropertyServiceMock);
    when(countryPropertyServiceMock.getCountryProperty(any(), any())).thenReturn(empty());

    contextMockHelper.setService(MessageSourceService.class, messageSourceService);
    when(messageSourceService.getMessage(any())).then(invocation -> invocation.getArguments()[0]);
  }

  @Test
  public void shouldRejectEmptyName() {
    final CountryProperty countryProperty = new CountryProperty();
    countryProperty.setValue("some value");
    final Errors errorsMock = mock(Errors.class);

    new CountryPropertyValidator().validate(countryProperty, errorsMock);

    verify(errorsMock).rejectValue(eq(CountryProperty.NAME_PROP_NAME), any(), any(), any());
  }

  @Test
  public void shouldRejectEmptyValue() {
    final CountryProperty countryProperty = new CountryProperty();
    countryProperty.setName("some name");
    final Errors errorsMock = mock(Errors.class);

    new CountryPropertyValidator().validate(countryProperty, errorsMock);

    verify(errorsMock).rejectValue(eq(CountryProperty.VALUE_PROP_NAME), any(), any(), any());
  }

  @Test
  public void shouldRejectNotUniqueProperty() {
    final CountryProperty countryProperty = new CountryProperty();
    countryProperty.setName("some name");
    countryProperty.setValue("some value");
    final Errors errorsMock = mock(Errors.class);

    when(countryPropertyServiceMock.getCountryProperty(null, countryProperty.getName()))
        .thenReturn(of(new CountryProperty()));

    new CountryPropertyValidator().validate(countryProperty, errorsMock);

    verify(errorsMock).reject(anyString());
  }
}
