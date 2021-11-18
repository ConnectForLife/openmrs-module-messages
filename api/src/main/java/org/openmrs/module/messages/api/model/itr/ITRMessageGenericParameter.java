package org.openmrs.module.messages.api.model.itr;

import java.util.Map;

/**
 * The ITRMessageGenericParameter Class.
 * <p>The representation of a single generic parameter of an ITR message. The parameter value is resolved at send time.
 */
public interface ITRMessageGenericParameter {
  String getName();

  String getValue(Map<String, Object> contextParameters);
}
