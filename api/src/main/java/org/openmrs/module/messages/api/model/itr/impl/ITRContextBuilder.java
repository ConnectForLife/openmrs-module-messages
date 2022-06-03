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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ITRContextBuilder<T extends ITRContextBuilder> {
  protected Locale locale;
  protected Map<String, Object> parameters = new HashMap<>();

  public T withLocale(String languageTag) {
    this.locale = Locale.forLanguageTag(languageTag);
    return (T) this;
  }

  public T withLocale(Locale locale) {
    this.locale = locale;
    return (T) this;
  }

  public T withParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
    return (T) this;
  }

  public T addParameter(String name, Object value) {
    this.parameters.put(name, value);
    return (T) this;
  }
}
