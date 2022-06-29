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

import java.util.Locale;
import java.util.Map;

/**
 * The immutable implementation of ITRResponseContext.
 */
class ITRResponseContextImpl extends ITRContextBaseImpl implements ITRResponseContext {
  private final String receivedText;

  ITRResponseContextImpl(Locale locale, String receivedText, Map<String, Object> parameters) {
    super(locale, parameters);
    this.receivedText = receivedText;
  }

  @Override
  public String getReceivedText() {
    return receivedText;
  }

  @Override
  public Map<String, Object> toContextParameters() {
    final Map<String, Object> contextParameters = super.toContextParameters();
    contextParameters.put("receivedText", getReceivedText());
    return contextParameters;
  }
}
