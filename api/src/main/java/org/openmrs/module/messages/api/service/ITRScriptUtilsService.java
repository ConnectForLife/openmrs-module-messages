package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.itr.ITRResponseContext;
import org.openmrs.module.messages.api.model.itr.impl.ITRResponseContextBuilder;

/**
 * The ITRScriptUtilsService Class.
 * <p>The service provides utilities for running ITR functionality from scripts.
 */
public interface ITRScriptUtilsService {

  ITRResponseContextBuilder getContextBuilder();

  /**
   * Finds proper response message and returns a Map with the message and optional properties.
   * <p>
   * <code>
   * {
   * "message": String,
   * "parameters": {
   * "key": Object|String|number
   * }
   * }
   * </code>
   *
   * @param responseContext the context used to determine the response, not null
   */
  String getITRResponseMessageJson(ITRResponseContext responseContext);
}
