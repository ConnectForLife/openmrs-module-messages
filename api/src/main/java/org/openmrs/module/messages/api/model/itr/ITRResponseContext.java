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
