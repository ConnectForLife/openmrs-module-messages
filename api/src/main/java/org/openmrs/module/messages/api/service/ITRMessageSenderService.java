/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
