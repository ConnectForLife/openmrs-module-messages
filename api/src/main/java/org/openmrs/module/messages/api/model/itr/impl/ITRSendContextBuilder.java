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

import java.util.ArrayList;
import java.util.List;

/**
 * The ITRSendContextBuilder Class.
 * <p>The builder for {@link ITRSendContext}.
 */
public class ITRSendContextBuilder extends ITRContextBuilder<ITRSendContextBuilder> {
  private String providerConfigName;
  private List<String> recipientsPhoneNumbers = new ArrayList<>();

  ITRSendContext build() {
    return new ITRSendContextImpl(locale, providerConfigName, recipientsPhoneNumbers, parameters);
  }

  public ITRSendContextBuilder withProviderConfigName(String providerConfigName) {
    this.providerConfigName = providerConfigName;
    return this;
  }

  public ITRSendContextBuilder withRecipientsPhoneNumbers(List<String> recipientsPhoneNumbers) {
    this.recipientsPhoneNumbers = recipientsPhoneNumbers;
    return this;
  }

  public ITRSendContextBuilder addRecipientPhoneNumber(String phoneNumber) {
    this.recipientsPhoneNumbers.add(phoneNumber);
    return this;
  }
}
