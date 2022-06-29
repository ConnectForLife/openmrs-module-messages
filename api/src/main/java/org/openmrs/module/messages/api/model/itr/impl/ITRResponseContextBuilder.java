/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model.itr.impl;

import org.openmrs.module.messages.api.model.itr.ITRResponseContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The ITRResponseContextBuilder Class.
 * <p>The builder for ITRResponseContext.
 * <p>Warning: This class is sued from Velocity template which doesn't support generics,  therefore it was not possible to
 * use derive from ITRContextBuilder.
 */
public class ITRResponseContextBuilder {
  private Locale locale;
  private String receivedText;
  private Map<String, Object> parameters = new HashMap<>();

  public ITRResponseContext build() {
    return new ITRResponseContextImpl(locale, receivedText, parameters);
  }

  public ITRResponseContextBuilder withLocale(String languageTag) {
    this.locale = Locale.forLanguageTag(languageTag);
    return this;
  }

  public ITRResponseContextBuilder withLocale(Locale locale) {
    this.locale = locale;
    return this;
  }

  public ITRResponseContextBuilder withReceivedText(String receivedText) {
    this.receivedText = receivedText;
    return this;
  }

  public ITRResponseContextBuilder withParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return this;
  }

  public ITRResponseContextBuilder addParameter(String name, Object value) {
    this.parameters.put(name, value);
    return this;
  }
}
