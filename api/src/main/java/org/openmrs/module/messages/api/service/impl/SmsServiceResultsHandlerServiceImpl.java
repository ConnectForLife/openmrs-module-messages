/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.event.SmsEventParamConstants;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.NotificationTemplateService;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements methods related to the handling of sms service results
 */
public class SmsServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {

    private static final Log LOGGER = LogFactory.getLog(SmsServiceResultsHandlerServiceImpl.class);

    private static final String SMS_INITIATE_EVENT = "send_sms";

    private static final String DEFAULT_MESSAGE = "Not yet specified";

    private static final String CHANNEL_TYPE = ChannelType.SMS.getName();

    private NotificationTemplateService notificationTemplateService;

    private MessagingService messagingService;

    private MessagesExecutionService messagesExecutionService;

    @Override
    public void handle(List<ScheduledService> smsServices, ScheduledExecutionContext executionContext) {
        for (ScheduledService service : smsServices) {
            try {
                this.triggerEvent(service, executionContext);
                this.messagingService.registerAttempt(service, ServiceStatus.DELIVERED, DateUtil.now(), null);
            } catch (Exception ex) {
                LOGGER.warn(String.format("During handling the `%s` service the following exception noticed `%s`",
                        service.getTemplateName(), ex.getMessage()));
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Error details: ", ex);
                }
                this.messagingService.registerAttempt(service, ServiceStatus.FAILED, DateUtil.now(), null);
            }
        }
        if (CollectionUtils.isNotEmpty(smsServices)) {
            int groupId = executionContext.getGroupId();
            this.messagesExecutionService.executionCompleted(groupId, null, CHANNEL_TYPE);
        }
    }

    public void setNotificationTemplateService(NotificationTemplateService notificationTemplateService) {
        this.notificationTemplateService = notificationTemplateService;
    }

    public void setMessagingService(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public void setMessagesExecutionService(MessagesExecutionService messagesExecutionService) {
        this.messagesExecutionService = messagesExecutionService;
    }

    private void triggerEvent(ScheduledService smsService, ScheduledExecutionContext executionContext) {
        MessagesEvent messagesEvent = buildMessage(smsService, executionContext);
        sendEventMessage(messagesEvent);
    }

    private MessagesEvent buildMessage(ScheduledService smsService, ScheduledExecutionContext executionContext) {
        Map<String, Object> params = new HashMap<>();
        params.put(SmsEventParamConstants.MESSAGE_ID, smsService.getId());
        params.put(SmsEventParamConstants.RECIPIENTS, Arrays.asList(getPersonPhone(executionContext.getActorId())));

        String message = notificationTemplateService.buildMessageForService(smsService.getPatientTemplate(),
                buildServiceParams(smsService));
        if (StringUtils.isBlank(message)) {
            message = DEFAULT_MESSAGE;
        }
        params.put(SmsEventParamConstants.MESSAGE, message);
        params.put(SmsEventParamConstants.CUSTOM_PARAMS, smsService.getParameters());
        return new MessagesEvent(SMS_INITIATE_EVENT, params);
    }

    private Map<String, String> buildServiceParams(ScheduledService smsService) {
        Map<String, String> serviceParams = new HashMap<>(smsService.getParameters());
        serviceParams.put(SmsEventParamConstants.MESSAGE_ID, smsService.getId().toString());
        ScheduledServiceGroup group = smsService.getGroup();
        if (null != group && null != group.getId()) {
            serviceParams.put(SmsEventParamConstants.MESSAGE_GROUP_ID, smsService.getGroup().getId().toString());
        }
        return serviceParams;
    }
}
