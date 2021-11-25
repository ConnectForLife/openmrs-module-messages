package org.openmrs.module.messages.api.model.itr;

import java.util.Locale;
import java.util.Map;

/**
 * The base context of ITR Message functions.
 */
public interface ITRContext {
  /**
   * @return the requested locale of the message, never null
   */
  Locale getLocale();

  /**
   * @return the Map of additional parameters, never null
   */
  Map<String, Object> getParameters();

  /**
   * @return a Map which combines all named properties of ITRResponseContext and the parameters Map, never null
   */
  Map<String, Object> toContextParameters();
}
