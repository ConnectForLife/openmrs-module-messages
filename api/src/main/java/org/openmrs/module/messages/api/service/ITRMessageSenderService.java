package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.itr.ITRSendContext;

/**
 * The ITRMessageSenderService class.
 * <p>The ITRMessageSender service allows sending ITR messages programmatically.
 */
public interface ITRMessageSenderService {
  /**
   * Sends the message with {@code uuid}. Uses {@code contextParameters} to resolve any message parameters.
   *
   * @param uuid the UUID of a message to sent, not null
   * @param sendContext the context, not null
   */
  void sendMessage(String uuid, ITRSendContext sendContext);
}
