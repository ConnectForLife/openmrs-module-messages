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
