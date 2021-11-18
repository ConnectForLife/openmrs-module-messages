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
