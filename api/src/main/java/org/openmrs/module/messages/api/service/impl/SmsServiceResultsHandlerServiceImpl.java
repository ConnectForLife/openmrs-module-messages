/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import static org.openmrs.module.messages.api.event.SmsEventParamConstants.CUSTOM_PARAMS;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.MESSAGE;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.MESSAGE_ID;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.RECIPIENTS;
import static org.openmrs.module.messages.api.event.SmsEventParamConstants.SERVICE_NAME;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

public class SmsServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {

    private static final String SMS_INITIATE_EVENT = "send_sms";

    @Override
    public void handle(List<ScheduledService> smsServices, ScheduledExecutionContext executionContext) {
        for (ScheduledService service : smsServices) {
            triggerEvent(service, executionContext);
        }
    }

    private void triggerEvent(ScheduledService smsService, ScheduledExecutionContext executionContext) {
        MessagesEvent messagesEvent = buildMessage(smsService, executionContext);
        sendEventMessage(messagesEvent);
    }

    private MessagesEvent buildMessage(ScheduledService smsService, ScheduledExecutionContext executionContext) {
        Map<String, Object> params = new HashMap<>();
        params.put(MESSAGE_ID, smsService.getId());
        params.put(RECIPIENTS, getPersonPhone(executionContext.getActorId()));
        //TODO in CFLM-446: Specify message
        params.put(MESSAGE, "Not yet specified");
        params.put(SERVICE_NAME, smsService.getTemplateName());

        params.put(CUSTOM_PARAMS, smsService.getParameters());

        return new MessagesEvent(SMS_INITIATE_EVENT, params);
    }
}
