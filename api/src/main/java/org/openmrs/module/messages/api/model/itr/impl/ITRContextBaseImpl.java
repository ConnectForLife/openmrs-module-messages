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
