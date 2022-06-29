/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import java.util.Date;

import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.TestConstants;

public final class DeliveryAttemptBuilder extends AbstractBuilder<DeliveryAttempt> {

    private Integer id;
    private ScheduledService scheduledService;
    private Date timestamp;
    private ServiceStatus status;
    private int attemptNumber;
    private String serviceExecution;

    public DeliveryAttemptBuilder() {
        super();
        id = getInstanceNumber();
        timestamp = new Date();
        status = ServiceStatus.DELIVERED;
        attemptNumber = 1;
        serviceExecution = TestConstants.TEST_SERVICE_EXECUTION_ID;
    }

    @Override
    public DeliveryAttempt build() {
        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setId(id);
        deliveryAttempt.setScheduledService(scheduledService);
        deliveryAttempt.setTimestamp(timestamp);
        deliveryAttempt.setStatus(status);
        deliveryAttempt.setAttemptNumber(attemptNumber);
        deliveryAttempt.setServiceExecution(serviceExecution);

        return deliveryAttempt;
    }

    @Override
    public DeliveryAttempt buildAsNew() {
        return withId(null).build();
    }

    public DeliveryAttemptBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public DeliveryAttemptBuilder withScheduledService(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
        return this;
    }

    public DeliveryAttemptBuilder withTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public DeliveryAttemptBuilder withStatus(ServiceStatus status) {
        this.status = status;
        return this;
    }

    public DeliveryAttemptBuilder withAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
        return this;
    }

    public DeliveryAttemptBuilder withServiceExecution(String serviceExecution) {
        this.serviceExecution = serviceExecution;
        return this;
    }
}
