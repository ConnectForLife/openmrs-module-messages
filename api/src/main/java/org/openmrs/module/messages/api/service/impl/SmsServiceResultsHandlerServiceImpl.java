package org.openmrs.module.messages.api.service.impl;

import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.messages.api.event.SmsEventParamConstants.CUSTOM_PARAMS;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.MESSAGE;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.MESSAGE_ID;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.RECIPIENTS;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.SERVICE_NAME;

public class SmsServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {

    private static final String SMS_INITIATE_EVENT = "send_sms";

    @Override
    public void handle(List<ServiceResult> results, GroupedServiceResultList group) {
        for (ServiceResult result : results) {
            triggerEvent(result, group);
        }
    }

    private void triggerEvent(ServiceResult result, GroupedServiceResultList group) {
        MessagesEvent messagesEvent = buildMessage(result, group);
        sendEventMessage(messagesEvent);
    }

    private MessagesEvent buildMessage(ServiceResult result, GroupedServiceResultList group) {
        Map<String, Object> params = new HashMap<>();
        params.put(MESSAGE_ID, result.getMessageId());
        params.put(RECIPIENTS, getPersonPhone(group.getActorId()));
        //TODO in CFLM-446: Specify message
        params.put(MESSAGE, "Not yet specified");
        params.put(SERVICE_NAME, group.getGroup().getServiceName());

        if (result.getAdditionalParams() != null) {
            params.put(CUSTOM_PARAMS, result.getAdditionalParams());
        }

        return new MessagesEvent(SMS_INITIATE_EVENT, params);
    }
}
