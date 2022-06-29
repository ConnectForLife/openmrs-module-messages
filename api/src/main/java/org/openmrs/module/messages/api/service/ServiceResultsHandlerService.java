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

import java.util.List;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

/**
 * Provides methods to handle results after service query execution
 */
public interface ServiceResultsHandlerService {
    /**
     * Handles results after service query execution e.g. trigger event
     *
     * @param services list of scheduled services e.g. smsServices or callServices
     * @param executionContext scheduled service context which contains all necessary data to schedule an event
     */
    void handle(List<ScheduledService> services, ScheduledExecutionContext executionContext);
}
