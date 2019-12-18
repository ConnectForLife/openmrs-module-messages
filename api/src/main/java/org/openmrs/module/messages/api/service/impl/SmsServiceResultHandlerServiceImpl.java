package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;

import java.util.HashMap;
import java.util.Map;

import static org.openmrs.module.messages.api.event.SmsEventParamConstants.CUSTOM_PARAMS;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.MESSAGE;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.MESSAGE_ID;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.RECIPIENTS;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.SERVICE_NAME;

public class SmsServiceResultHandlerServiceImpl extends AbstractServiceResultHandlerService {

    private static final String SMS_INITIATE_EVENT = "send_sms";

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
        params.put(RECIPIENTS, getPersonPhone(group.getActorId()));
        //TODO in CFLM-446: Specify message
        params.put(MESSAGE, "Not yet specified");
        params.put(SERVICE_NAME, group.getServiceName());

        if (result.getAdditionalParams() != null) {
            params.put(CUSTOM_PARAMS, result.getAdditionalParams());
        }

        return new MessagesEvent(SMS_INITIATE_EVENT, params);
    }
}
