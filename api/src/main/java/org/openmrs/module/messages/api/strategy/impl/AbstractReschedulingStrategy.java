/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy.impl;

import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.api.util.DateUtil;

public abstract class AbstractReschedulingStrategy implements ReschedulingStrategy {

    private final Log logger = LogFactory.getLog(getClass());

    private ConfigService configService;

    private MessagesDeliveryService deliveryService;

    @Override
    public void execute(ScheduledService service) {
        if (!shouldReschedule(service)) {
            return;
        }

        List<ScheduledService> servicesToExecute = extractServiceListToExecute(service);

        deliveryService.scheduleDelivery(new ScheduledExecutionContext(
                servicesToExecute,
                getRescheduleDate(),
                service.getPatientTemplate().getActor()
        ));
    }

    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    public void setDeliveryService(MessagesDeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    protected boolean shouldReschedule(ScheduledService service) {
        boolean shouldReschedule = true;
        if (service.getStatus() != ServiceStatus.FAILED) {
            logger.debug(String.format(
                    "ScheduledService %d will be not rescheduled due to no failed status: %s",
                    service.getId(),
                    service.getStatus()));
            shouldReschedule = false;
        } else if (service.getNumberOfAttempts() >= getMaxNumberOfAttempts()) {
            logger.info(String.format(
                    "ScheduledService %d will be not rescheduled due to exceeding the max number of attempts: %d/%d",
                    service.getId(),
                    service.getNumberOfAttempts(),
                    getMaxNumberOfAttempts()));
            shouldReschedule = false;
        }
        return shouldReschedule;
    }

    protected abstract List<ScheduledService> extractServiceListToExecute(ScheduledService service);

    private Date getRescheduleDate() {
        return DateUtil.getDatePlusSeconds(
                getTimeIntervalToNextReschedule());
    }

    private int getMaxNumberOfAttempts() {
        return configService.getMaxNumberOfRescheduling();
    }

    private int getTimeIntervalToNextReschedule() {
        return configService.getTimeIntervalToNextReschedule();
    }
}
