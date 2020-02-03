/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import java.util.Date;
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

public class MessagesExecutionServiceImpl implements MessagesExecutionService {
    private static final Log LOGGER = LogFactory.getLog(MessagesExecutionServiceImpl.class);

    private ConfigService configService;
    private MessagingService messagingService;
    private MessagingGroupService messagingGroupService;

    @Override
    public void executionCompleted(Integer groupId, String executionId, String channelType) {
        LOGGER.trace(String.format(
                "Handling messages executing completion for groupId=%d, executionId=%s, channelType=%s",
                groupId, executionId, channelType));

        Date completionDate = DateUtil.now();
        ScheduledServiceGroup group = HibernateUtil.getNotNull(groupId, messagingGroupService);

        registerFailedAttemptIfNotDelivered(executionId, channelType, completionDate, group);
        // TODO: currently groups can represent different channels and it is difficult to manage its statuses,
        //  however in the future group will represent exactly one channel,
        //  so statuses for the whole group will be set as it should be
        group.setStatus(isGroupDelivered(group) ? ServiceStatus.DELIVERED : ServiceStatus.FAILED);
        group = messagingGroupService.saveOrUpdate(group);

        configService.getReschedulingStrategy(channelType)
            .execute(group, channelType);
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

    private void registerFailedAttemptIfNotDelivered(String executionId, String channelType, Date date,
                                                     ScheduledServiceGroup group) {
        for (ScheduledService ss : group.getScheduledServicesByChannel(channelType)) {
            if (!ss.getStatus().equals(ServiceStatus.DELIVERED)) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace(String.format(
                            "Registering failed attempt for not delivered ScheduledService %d (%s)",
                            ss.getId(),
                            ss.getPatientTemplate().getTemplate().getName()));
                }
                messagingService.registerAttempt(ss, ServiceStatus.FAILED, date, executionId);
            }
        }
    }

    private boolean isGroupDelivered(ScheduledServiceGroup group) {
        boolean result = true;
        for (ScheduledService ss : group.getScheduledServices()) {
            if (!ServiceStatus.DELIVERED.equals(ss.getStatus())) {
                result = false;
                break;
            }
        }
        return result;
    }
}
