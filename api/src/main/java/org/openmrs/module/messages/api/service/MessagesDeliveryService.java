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

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

/**
 * Provides methods related to messages delivery management
 * <p>
 * The service automatically schedules proper message delivery tasks during system startup.
 * </p>
 */
public interface MessagesDeliveryService extends OpenmrsService {
    /**
     * Schedules a new event delivery
     *
     * @param executionContext scheduled service context which contains all necessary data to schedule an event
     */
    void scheduleDelivery(ScheduledExecutionContext executionContext);
}
