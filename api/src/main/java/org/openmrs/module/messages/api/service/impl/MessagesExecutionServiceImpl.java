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
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.service.MessagesExecutionService;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.HibernateUtil;

import java.time.ZonedDateTime;

/**
 * Implements methods related to handling of completed messages executions
 */
public class MessagesExecutionServiceImpl implements MessagesExecutionService {
    private static final Log LOGGER = LogFactory.getLog(MessagesExecutionServiceImpl.class);

    private ConfigService configService;
    private MessagingService messagingService;
    private MessagingGroupService messagingGroupService;

    @Override
    public void executionCompleted(Integer groupId, String executionId, String channelType) {
        LOGGER.trace(String.format("Handling messages executing completion for groupId=%d, executionId=%s, channelType=%s",
                groupId, executionId, channelType));

        final ZonedDateTime completionDate = DateUtil.now();
        ScheduledServiceGroup group = HibernateUtil.getNotNull(groupId, messagingGroupService);

        registerFailedAttemptIfNotDelivered(executionId, completionDate, group);
        group.setStatus(isGroupDelivered(group) ? ServiceStatus.DELIVERED : ServiceStatus.FAILED);
        group = messagingGroupService.saveOrUpdate(group);

        configService.getReschedulingStrategy(channelType).execute(group);
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public void setMessagingService(MessagingService messagingService) {
        this.messagingService = messagingService;
    }

    public void setMessagingGroupService(MessagingGroupService messagingGroupService) {
        this.messagingGroupService = messagingGroupService;
    }

    private void registerFailedAttemptIfNotDelivered(String executionId, ZonedDateTime date, ScheduledServiceGroup group) {
        for (ScheduledService ss : group.getScheduledServices()) {
            if (!(ss.getStatus() == ServiceStatus.DELIVERED)) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(String.format("Registering failed attempt for not delivered ScheduledService %d (%s)",
                            ss.getId(), ss.getPatientTemplate().getTemplate().getName()));
                }
                messagingService.registerAttempt(ss, ServiceStatus.FAILED, DateUtil.toDate(date), executionId);
            }
        }
    }

    private boolean isGroupDelivered(ScheduledServiceGroup group) {
        boolean result = true;
        for (ScheduledService ss : group.getScheduledServices()) {
            if (!(ServiceStatus.DELIVERED == ss.getStatus())) {
                result = false;
                break;
            }
        }
        return result;
    }
}
