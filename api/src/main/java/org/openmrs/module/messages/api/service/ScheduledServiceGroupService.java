/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.scheduler.SchedulerService;

import java.util.Date;

/**
 * The ScheduledServiceGroupService Class.
 * <p>
 * This service provides general-purpose functionalities related to {@link ScheduledServiceGroup}.
 * </p>
 */
public interface ScheduledServiceGroupService {
    ServiceStatus INITIAL_SCHEDULED_SERVICE_STATUS = ServiceStatus.PENDING;

    /**
     * Create new instance of {@link ScheduledServiceGroup} with single {@link ScheduledService} for the given
     * {@code deliveryTime}, {@code channelType} and {@code patientTemplate}.
     * <p>
     * The {@link SchedulerService} and {@link ScheduledServiceGroup} are created with the
     * {@link #INITIAL_SCHEDULED_SERVICE_STATUS} set.
     * </p>
     *
     * @param deliveryTime    the deliver time to schedule the group for, not null
     * @param channelType     the channel type to sue when delivering the service, not null
     * @param patientTemplate the Patient Template to use, not null
     * @return the new instance of ScheduledServiceGroup, never null
     */
    ScheduledServiceGroup createSingletonGroup(final Date deliveryTime, final String channelType,
                                               final PatientTemplate patientTemplate);
}
