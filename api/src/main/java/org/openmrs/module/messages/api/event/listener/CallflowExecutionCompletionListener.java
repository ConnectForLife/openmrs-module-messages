/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.event.AbstractMessagesEventListener;
import org.openmrs.module.messages.api.event.CallFlowParamConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.util.Properties;

public class CallflowExecutionCompletionListener extends AbstractMessagesEventListener {

    private static final Log LOGGER = LogFactory.getLog(CallflowExecutionCompletionListener.class);

    public static final String CALLFLOWS_CALL_STATUS = "callflows-call-status";

    public static final String PARAM_CALL_ID = "callId";
    public static final String UNKNOWN_CALL_ID = "unknown";

    private static final String CHANNEL_TYPE = ChannelType.CALL.getName();

    @Override
    public String getSubject() {
        return CALLFLOWS_CALL_STATUS;
    }

    @Override
    protected void handleEvent(Properties properties) {
        LOGGER.trace(String.format("Handling event: %s with properties %s", getSubject(), properties.toString()));

        int groupId = getGroupId(properties);
        String executionId = getExecutionId(properties);

        getComponent("messages.messagesExecutionService", MessagesExecutionService.class)
            .executionCompleted(groupId, executionId, CHANNEL_TYPE);
    }

    private int getGroupId(Properties properties) {
        Properties params = properties.getNestedProperties(CallFlowParamConstants.ADDITIONAL_PARAMS);
        Integer result = params != null ? params.getInt(CallFlowParamConstants.REF_KEY) : null;
        if (result == null) {
            throw new MessagesRuntimeException(String.format(
                    "The event param %s.%s cannot be null",
                    CallFlowParamConstants.ADDITIONAL_PARAMS,
                    CallFlowParamConstants.REF_KEY
            ));
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
}
