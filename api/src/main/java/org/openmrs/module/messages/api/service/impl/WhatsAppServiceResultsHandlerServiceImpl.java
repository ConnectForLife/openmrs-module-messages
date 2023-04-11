/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

/**
 * Implements methods related to the handling of WhatsApp service results
 */
public class WhatsAppServiceResultsHandlerServiceImpl extends AbstractTextMessageServiceResultsHandlerService {

  private static final String WHATSAPP_CHANNEL_TYPE = "WhatsApp";

  public WhatsAppServiceResultsHandlerServiceImpl() {
    super(WHATSAPP_CHANNEL_TYPE);
  }

  @Override
  protected String getConfigName(ScheduledExecutionContext executionContext) {
    return executionContext.getChannelConfiguration()
      .getOrDefault(CONFIG_KEY,
        Context.getAdministrationService()
          .getGlobalProperty(ConfigConstants.WHATSAPP_CONFIG_GP_KEY));
  }
}
