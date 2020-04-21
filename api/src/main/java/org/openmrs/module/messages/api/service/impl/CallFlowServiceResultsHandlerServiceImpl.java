/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.model.Message;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_CONFIG;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_FLOW;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALL_FLOW_INITIATE_CALL_EVENT;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ACTOR_ID;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ACTOR_TYPE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ADDITIONAL_PARAMS;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.CONFIG;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.FLOW_NAME;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.MESSAGES;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.PHONE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.REF_KEY;

/**
 * Implements methods related to the handling of call service results
 */
public class CallFlowServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {

    private static final Log LOG = LogFactory.getLog(CallFlowServiceResultsHandlerServiceImpl.class);

    @Override
    public void handle(List<ScheduledService> callServices, ScheduledExecutionContext executionContext) {
        triggerEvent(callServices, executionContext);
    }

    private void triggerEvent(List<ScheduledService> callServices, ScheduledExecutionContext executionContext) {
        if (callServices.isEmpty()) {
            LOG.info(String.format("Handling of callflow for actor: %s, executionDate: %s has been skipped because "
                    + "of empty services list", executionContext.getActorId(), executionContext.getExecutionDate()));
        } else {
            MessagesEvent messagesEvent = buildMessage(callServices, executionContext);
            sendEventMessage(messagesEvent);
        }
    }

    private MessagesEvent buildMessage(List<ScheduledService> callServices,
                                       ScheduledExecutionContext executionContext) {
        Map<String, Object> params = new HashMap<>();
        params.put(CONFIG, CALLFLOWS_DEFAULT_CONFIG);
        params.put(FLOW_NAME, CALLFLOWS_DEFAULT_FLOW);

        String personPhone = getPersonPhone(executionContext.getActorId());

        Map<String, Object> additionalParams = new HashMap<>();
        List<Message> messages = getMessages(callServices);
        additionalParams.put(MESSAGES, toPrimitivesList(messages));
        additionalParams.put(PHONE, personPhone);
        additionalParams.put(ACTOR_ID, Integer.toString(executionContext.getActorId()));
        additionalParams.put(REF_KEY, Integer.toString(executionContext.getGroupId()));
        additionalParams.put(ACTOR_TYPE, executionContext.getActorType());

        params.put(ADDITIONAL_PARAMS, additionalParams);

        return new MessagesEvent(CALL_FLOW_INITIATE_CALL_EVENT, params);
    }

    private List<Message> getMessages(List<ScheduledService> callServices) {
        List<Message> messages = new ArrayList<>();
        for (ScheduledService service : callServices) {
            String serviceName = service.getPatientTemplate().getTemplate().getName();
            messages.add(new Message(
                    serviceName,
                    service.getId(),
                    service.getParameters()));
        }
        return messages;
    }

    private List<Map<String, Object>> toPrimitivesList(List<Message> messages) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (Message message : messages) {
            result.add(message.toPrimitivesMap());
        }
        return result;
    }
}
