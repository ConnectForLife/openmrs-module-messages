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

import org.openmrs.module.messages.api.model.itr.ITRContext;
import org.openmrs.module.messages.api.model.itr.ITRMessage;

import java.util.Map;

/**
 * The ITRConverterService Class.
 * <p>The ITRConverter service allows to convert the ITRMessage to different representations fit for API of other
 * modules.
 */
public interface ITRConverterService {
  Map<String, Object> convertToSmsEvent(ITRMessage message, ITRContext context);
}
