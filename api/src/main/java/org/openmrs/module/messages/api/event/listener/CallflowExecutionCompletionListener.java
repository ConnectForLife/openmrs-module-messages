/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import com.antkorwin.xsync.XSync;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.event.AbstractMessagesEventListener;
import org.openmrs.module.messages.api.event.CallFlowParamConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.util.Properties;

public class CallflowExecutionCompletionListener extends AbstractMessagesEventListener {

  private static final Log LOGGER = LogFactory.getLog(CallflowExecutionCompletionListener.class);
  private static final String CALLFLOWS_CALL_STATUS_SUBJECT = "callflows-call-status";
  private static final String PARAM_CALL_ID = "callId";
  private static final String UNKNOWN_CALL_ID = "unknown";
  private static final String PARAM_STATUS = "status";
  private static final String CHANNEL_TYPE = "Call";

  @Override
  public String getSubject() {
    return CALLFLOWS_CALL_STATUS_SUBJECT;
  }

  @Override
  protected void handleEvent(Properties properties) {
    if (isMessagesCall(properties)) {
      LOGGER.debug(String.format("Handling event: %s with properties %s", getSubject(), properties.toString()));

      final XSync<Integer> scheduledServiceGroupXSync = (XSync<Integer>) getScheduledServiceGroupXSync();

      final int groupId = getGroupId(properties);
      scheduledServiceGroupXSync.execute(groupId, () -> this.performOnEventForScheduledGroup(groupId, properties));
    } else {
      if (LOGGER.isTraceEnabled()) {
        LOGGER.trace(
            String.format("Skipped handling event: %s with properties %s " + "because this call is not from messages",
                getSubject(), properties.toString()));
      }
    }
  }

  private void performOnEventForScheduledGroup(int groupId, Properties properties) {
    String status = getStatus(properties);
    if (!getConfigService().getStatusesEndingCallflow().contains(status)) {
      LOGGER.trace(String.format("Skipped handling event with the status %s - it is not the end of a callflow: %s", status,
          properties.toString()));
      return;
    }

    String executionId = getExecutionId(properties);

    getComponent("messages.messagesExecutionService", MessagesExecutionService.class).executionCompleted(groupId,
        executionId, CHANNEL_TYPE);
  }

  private XSync<?> getScheduledServiceGroupXSync() {
    return Context.getRegisteredComponent("messages.scheduledServiceGroupXSync", XSync.class);
  }

  private String getStatus(Properties properties) {
    String result = properties.getString(PARAM_STATUS);
    if (result == null) {
      throw new MessagesRuntimeException("Status cannot be null - it must be passed by callflow");
    }
    return result;
  }

  private int getGroupId(Properties properties) {
    Properties params = properties.getNestedProperties(CallFlowParamConstants.ADDITIONAL_PARAMS);
    Integer result = params != null ? params.getInt(CallFlowParamConstants.REF_KEY) : null;
    if (result == null) {
      throw new MessagesRuntimeException(
          String.format("The event param %s.%s cannot be null", CallFlowParamConstants.ADDITIONAL_PARAMS,
              CallFlowParamConstants.REF_KEY));
    }
    return result;
  }

  private String getExecutionId(Properties properties) {
    String callId = properties.getString(PARAM_CALL_ID);
    if (StringUtils.equals(callId, UNKNOWN_CALL_ID)) {
      callId = null;
    }
    return callId;
  }

  private boolean isMessagesCall(Properties properties) {
    Properties params = properties.getNestedProperties(CallFlowParamConstants.ADDITIONAL_PARAMS);
    return params.get(CallFlowParamConstants.MESSAGES) != null;
  }

  private ConfigService getConfigService() {
    return getComponent("messages.configService", ConfigService.class);
  }
}
