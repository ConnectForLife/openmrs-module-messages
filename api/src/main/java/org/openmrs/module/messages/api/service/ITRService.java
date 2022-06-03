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

import org.openmrs.module.messages.api.model.itr.ITRMessage;

import java.util.Optional;

/**
 * The ITRService class.
 * <p>The service handles Interactive Text Response system.
 */
public interface ITRService {
  /**
   * Finds a response message which fits the provided {@code inputText}.
   * <p>The method searches for an ITR Message Concept which Answer Regex match the provided text.
   * <p>If there are multiple matches, the first is returned and a WARN-level log output is printed.
   *
   * @param inputText the text to find a response for, not null
   * @return the ITR Message optional, never null
   */
  Optional<ITRMessage> findResponse(String inputText);

  /**
   * Gets the default response.
   */
  ITRMessage getDefaultResponse();

  /**
   * Gets message by UUID.
   *
   * @param uuid the message UUID, not null
   * @return the ITRMessage, never null
   * @throws org.openmrs.api.APIException if there is no message for given UUID
   */
  ITRMessage getMessageByUuid(String uuid);
}
