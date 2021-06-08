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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class SmsServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {

    /**
     * The name of configuration property with Message template value.
     */
    public static final String SMS_CHANNEL_CONF_TEMPLATE_VALUE = "templateValue";

    public static final String MESSAGE_KEY = "message";

    private static final Log LOGGER = LogFactory.getLog(SmsServiceResultsHandlerServiceImpl.class);
    private static final String DEFAULT_MESSAGE = "Not yet specified";
    private static final String CHANNEL_TYPE = "SMS";

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
                LOGGER.error(String.format("During handling the `%s` service the following exception noticed `%s`",
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
        final Map<String, String> templateMap = executeTemplate(smsService, executionContext);
        final String message = templateMap.getOrDefault(MESSAGE_KEY, DEFAULT_MESSAGE);

        final Map<String, String> smsServiceParameters = smsService.getParameters();
        for (Map.Entry<String, String> entry : templateMap.entrySet()) {
            final String key = entry.getKey();
            if (!key.equals(MESSAGE_KEY) && !smsServiceParameters.containsKey(key)) {
                smsServiceParameters.put(key, entry.getValue());
            }
        }

        final Map<String, Object> params = new HashMap<>();
        params.put(SmsEventParamConstants.MESSAGE_ID, smsService.getId());
        params.put(SmsEventParamConstants.RECIPIENTS,
                Collections.singletonList(getPersonPhone(executionContext.getActorId())));
        params.put(SmsEventParamConstants.MESSAGE, message);
        params.put(SmsEventParamConstants.CUSTOM_PARAMS, smsServiceParameters);
        return new MessagesEvent(SMS_INITIATE_EVENT, params);
    }

    private Map<String, String> executeTemplate(ScheduledService smsService, ScheduledExecutionContext executionContext) {
        final Map<String, String> templateParsingParameters = buildServiceParams(smsService);
        final String parsedTemplate;

        if (executionContext.getChannelConfiguration().containsKey(SMS_CHANNEL_CONF_TEMPLATE_VALUE)) {
            final NotificationTemplate template = new NotificationTemplate();
            template.setTemplateName(smsService.getPatientTemplate().getTemplate().getName());
            template.setValue(executionContext.getChannelConfiguration().get(SMS_CHANNEL_CONF_TEMPLATE_VALUE));

            parsedTemplate = notificationTemplateService.parseTemplate(smsService.getPatientTemplate(), template,
                    templateParsingParameters);
        } else {
            parsedTemplate =
                    notificationTemplateService.parseTemplate(smsService.getPatientTemplate(), templateParsingParameters);
        }

        return StringUtils.isNotBlank(parsedTemplate) ? JsonUtil.toMap(parsedTemplate, JsonUtil.STRING_TO_STRING_MAP) :
                Collections.emptyMap();
    }

    private Map<String, String> buildServiceParams(ScheduledService smsService) {
        final Map<String, String> serviceParams = new HashMap<>(smsService.getParameters());
        serviceParams.put(SmsEventParamConstants.MESSAGE_ID, smsService.getId().toString());
        final ScheduledServiceGroup group = smsService.getGroup();
        if (null != group && null != group.getId()) {
            serviceParams.put(SmsEventParamConstants.MESSAGE_GROUP_ID, smsService.getGroup().getId().toString());
        }
        return serviceParams;
    }
}
