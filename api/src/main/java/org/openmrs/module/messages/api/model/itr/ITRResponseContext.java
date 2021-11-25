package org.openmrs.module.messages.api.model.itr;

/**
 * The ITRResponseContext Class.
 * <p>The wrapper for all data needed to determine an ITR response.
 *
 * @see org.openmrs.module.messages.api.model.itr.impl.ITRResponseContextBuilder
 */
public interface ITRResponseContext extends ITRContext {
  /**
   * @return the received text
   */
  String getReceivedText();
}
