package org.openmrs.module.messages.api.service.impl;

import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_CONFIG;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_FLOW;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ADDITIONAL_PARAMS;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.CONFIG;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.FLOW_NAME;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.MESSAGE_ID;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.PHONE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.SERVICE_NAME;

import java.util.HashMap;
import java.util.Map;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;

public class CallFlowServiceResultHandlerServiceImpl extends AbstractServiceResultHandlerService {
    private static final String CALL_FLOW_INITIATE_CALL_EVENT = "callflows-call-initiate";

    @Override
    public void handle(ServiceResult result, ServiceResultList group) {
        triggerEvent(result, group);
    }

    private void triggerEvent(ServiceResult result, ServiceResultList group) {
        MessagesEvent messagesEvent = buildMessage(result, group);
        sendEventMessage(messagesEvent);
    }

    private MessagesEvent buildMessage(ServiceResult result, ServiceResultList group) {
        Map<String, Object> params = new HashMap<>();
        params.put(MESSAGE_ID, result.getMessageId());
        params.put(CONFIG, CALLFLOWS_DEFAULT_CONFIG);
        params.put(FLOW_NAME, CALLFLOWS_DEFAULT_FLOW);

        Map<String, Object> additionalParams = new HashMap<>();
        additionalParams.put(PHONE, getPatientPhone(group.getPatientId()));
        additionalParams.put(SERVICE_NAME, group.getServiceName());

        if (result.getAdditionalParams() != null) {
            additionalParams.putAll(result.getAdditionalParams());
        }
        params.put(ADDITIONAL_PARAMS, additionalParams);

        return new MessagesEvent(CALL_FLOW_INITIATE_CALL_EVENT, params);
    }
}
