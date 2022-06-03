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

import org.openmrs.module.messages.api.model.itr.ITRContext;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

class ITRContextBaseImpl implements ITRContext {
  private final Locale locale;
  private final Map<String, Object> parameters;

  ITRContextBaseImpl(Locale locale, Map<String, Object> parameters) {
    this.locale = locale;
    this.parameters = parameters;
  }

  @Override
  public Locale getLocale() {
    return locale;
  }

  @Override
  public Map<String, Object> getParameters() {
    return parameters;
  }

  @Override
  public Map<String, Object> toContextParameters() {
    final Map<String, Object> contextParameters = new HashMap<>(getParameters());
    contextParameters.put("locale", getLocale());
    return contextParameters;
  }
}
