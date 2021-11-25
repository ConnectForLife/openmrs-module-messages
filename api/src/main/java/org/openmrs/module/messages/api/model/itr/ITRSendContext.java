package org.openmrs.module.messages.api.model.itr;

import java.util.List;

/**
 * The ITRSendContext Class.
 * <p>The wrapper for all data needed to send a message.
 *
 * @see org.openmrs.module.messages.api.model.itr.impl.ITRSendContextBuilder
 */
public interface ITRSendContext extends ITRContext {
  /**
   * @return the name of provider config to use when sending the message, never null
   */
  String getProviderConfigName();

  /**
   * @return the list of recipient phone numbers, never null
   */
  List<String> getRecipientsPhoneNumbers();
}
