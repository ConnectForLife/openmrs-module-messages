/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
