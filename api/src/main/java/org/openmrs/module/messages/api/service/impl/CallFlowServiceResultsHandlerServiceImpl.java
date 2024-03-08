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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.event.CallFlowParamConstants;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.model.Message;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.messages.api.constants.MessagesConstants.CALL_FLOW_INITIATE_CALL_EVENT;

/**
 * Implements methods related to the handling of call service results
 */
public class CallFlowServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {
    /**
     * The name of Call-channel configuration property with Call Flow name.
     */
    public static final String CALL_CHANNEL_CONF_FLOW_NAME = "callFlow";
    /**
     * The name of Call-channel configuration property with Call config.
     */

    private static final Log LOG = LogFactory.getLog(CallFlowServiceResultsHandlerServiceImpl.class);

    @Override
    public void handle(List<ScheduledService> callServices, ScheduledExecutionContext executionContext) {
        triggerEvent(callServices, executionContext);
    }

    private void triggerEvent(List<ScheduledService> callServices, ScheduledExecutionContext executionContext) {
        if (callServices.isEmpty()) {
            LOG.info(String.format("Handling of callflow for actor: %s, executionDate: %s has been skipped because " +
                    "of empty services list", executionContext.getActorId(), executionContext.getExecutionDate()));
        } else {
            MessagesEvent messagesEvent = buildMessage(callServices, executionContext);
            sendEventMessage(messagesEvent);
        }
    }

    private MessagesEvent buildMessage(List<ScheduledService> callServices, ScheduledExecutionContext executionContext) {
        Map<String, Object> params = new HashMap<>();
        params.put(CallFlowParamConstants.CONFIG_KEY, getCallConfig(executionContext));
        params.put(CallFlowParamConstants.FLOW_NAME, getCallFlow(executionContext));

        String personPhone = getPersonPhone(executionContext.getActorId());

        Map<String, Object> additionalParams = new HashMap<>();
        List<Message> messages = getMessages(callServices);
        additionalParams.put(CallFlowParamConstants.MESSAGES, toPrimitivesList(messages));
        additionalParams.put(CallFlowParamConstants.PHONE, personPhone);
        additionalParams.put(CallFlowParamConstants.ACTOR_ID, Integer.toString(executionContext.getActorId()));
        additionalParams.put(CallFlowParamConstants.REF_KEY, Integer.toString(executionContext.getGroupId()));
        additionalParams.put(CallFlowParamConstants.ACTOR_TYPE, executionContext.getActorType());

        params.put(CallFlowParamConstants.ADDITIONAL_PARAMS, additionalParams);

        return new MessagesEvent(CALL_FLOW_INITIATE_CALL_EVENT, params);
    }

    private List<Message> getMessages(List<ScheduledService> callServices) {
        List<Message> messages = new ArrayList<>(callServices.size());
        for (ScheduledService service : callServices) {
            String serviceName = service.getPatientTemplate().getTemplate().getName();
            messages.add(new Message(serviceName, service.getId(), service.getParameters()));
        }
        return messages;
    }

    private List<Map<String, Object>> toPrimitivesList(List<Message> messages) {
        List<Map<String, Object>> result = new ArrayList<>(messages.size());
        for (Message message : messages) {
            result.add(message.toPrimitivesMap());
        }
        return result;
    }

    private Object getCallConfig(ScheduledExecutionContext executionContext) {
        return executionContext.getChannelConfiguration()
                .getOrDefault(CallFlowParamConstants.CONFIG_KEY,
                        Context.getAdministrationService()
                        .getGlobalProperty(ConfigConstants.CALL_CONFIG, ConfigConstants.CALL_CONFIG_DEFAULT_VALUE));
    }

    private String getCallFlow(ScheduledExecutionContext executionContext) {
        return executionContext.getChannelConfiguration()
                .getOrDefault(CALL_CHANNEL_CONF_FLOW_NAME,
                        Context.getAdministrationService()
                                .getGlobalProperty(ConfigConstants.CALL_DEFAULT_FLOW,
                                        ConfigConstants.CALL_DEFAULT_FLOW_DEFAULT_VALUE));
    }
}
