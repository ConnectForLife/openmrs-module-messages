/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model.types;

/**
 * The enum which represents possible ScheduledService's statuses.
 * @see org.openmrs.module.messages.api.model.ScheduledService
 */
public enum ServiceStatus {
    /**
     * DELIVERY describes ScheduledServices which are already persisted in DB and the system got registerAttempt
     * response with success.
     */
    DELIVERED,

    /**
     * FAILED describes ScheduledServices which are already persisted in DB and the system got registerAttempt
     * response with no success.
     */
    FAILED,

    /**
     * FUTURE describes ScheduledServices which are not persisted in DB - it is part of
     * {@link org.openmrs.module.messages.api.execution.ExecutionEngine} results with not existence
     * as ScheduledService in DB.
     */
    FUTURE,

    /**
     * PENDING describes ScheduledServices which are already persisted in DB, but the system got no registerAttempt
     * response for it - it means that it must be scheduled or already processed by service.
     */
    PENDING
}
