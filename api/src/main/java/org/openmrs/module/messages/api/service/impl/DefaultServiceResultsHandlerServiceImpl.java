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
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;

import java.util.List;

/**
 * Implements default methods related to the handling of service results
 */
public class DefaultServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {

    private static final Log LOGGER = LogFactory.getLog(DefaultServiceResultsHandlerServiceImpl.class);

    @Override
    public void handle(List<ScheduledService> services, ScheduledExecutionContext executionContext) {
        if (LOGGER.isTraceEnabled()) {
            for (ScheduledService service : services) {
                LOGGER.trace(String.format("The %s service was skipped and won't be processed " +
                        "because missing concrete handler for %s channel type.", service.getTemplateName(),
                        service.getChannelType()));
            }
        }
    }
}
