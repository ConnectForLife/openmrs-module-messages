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
