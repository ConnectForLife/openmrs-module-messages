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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.NotificationTemplate;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.NotificationTemplateService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.JsonUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.messages.api.constants.MessagesConstants.SMS_INITIATE_EVENT;

/**
 * Implements methods related to the handling of sms service results
 */
public class SmsServiceResultsHandlerServiceImpl extends AbstractTextMessageServiceResultsHandlerService {
  
  private static final String SMS_CHANNEL_TYPE = "SMS";

  @Override
  public void handle(List<ScheduledService> smsServices, ScheduledExecutionContext executionContext) {
    handleServices(smsServices, executionContext);
    if (CollectionUtils.isNotEmpty(smsServices)) {
      int groupId = executionContext.getGroupId();
      messagesExecutionService.executionCompleted(groupId, null, SMS_CHANNEL_TYPE);
    }
  }

  @Override
  protected String getConfigName(ScheduledExecutionContext executionContext) {
    return executionContext.getChannelConfiguration()
      .getOrDefault(CONFIG_KEY,
        Context.getAdministrationService()
          .getGlobalProperty(ConfigConstants.SMS_CONFIG, ConfigConstants.SMS_CONFIG_DEFAULT_VALUE));
  }
}
