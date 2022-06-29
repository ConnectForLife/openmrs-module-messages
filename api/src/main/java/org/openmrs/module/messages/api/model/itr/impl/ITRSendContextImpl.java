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

import org.openmrs.module.messages.api.model.itr.ITRSendContext;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Collections.unmodifiableList;

/**
 * The immutable implementation of ITRSendContext.
 */
class ITRSendContextImpl extends ITRContextBaseImpl implements ITRSendContext {
  private final String providerConfigName;
  private final List<String> recipientsPhoneNumbers;

  ITRSendContextImpl(Locale locale, String providerConfigName, List<String> recipientsPhoneNumbers,
                     Map<String, Object> parameters) {
    super(locale, parameters);
    this.providerConfigName = providerConfigName;
    this.recipientsPhoneNumbers = unmodifiableList(recipientsPhoneNumbers);
  }

  @Override
  public String getProviderConfigName() {
    return providerConfigName;
  }

  @Override
  public List<String> getRecipientsPhoneNumbers() {
    return recipientsPhoneNumbers;
  }

  @Override
  public Map<String, Object> toContextParameters() {
    final Map<String, Object> contextParameters = super.toContextParameters();
    contextParameters.put("providerConfigName", providerConfigName);
    contextParameters.put("recipientsPhoneNumbers", recipientsPhoneNumbers);
    return contextParameters;
  }
}
