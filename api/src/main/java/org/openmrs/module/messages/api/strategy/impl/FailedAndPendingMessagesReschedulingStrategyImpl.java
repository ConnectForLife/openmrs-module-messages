/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy.impl;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

public class FailedAndPendingMessagesReschedulingStrategyImpl extends AbstractReschedulingStrategy {

    @Override
    protected List<ScheduledService> extractServiceListToExecute(ScheduledServiceGroup group, String channelType) {
        return extractFailedAndPendingServicesForChannelType(group, channelType);
    }

    private List<ScheduledService> extractFailedAndPendingServicesForChannelType(ScheduledServiceGroup group,
                                                                                 String channelType) {
        ArrayList<ScheduledService> services = new ArrayList<>();
        for (ScheduledService service : group.getScheduledServicesByChannel(channelType)) {
            if (hasFailedOrPendingStatus(service)) {
                services.add(service);
            }
        }
        return services;
    }

    private boolean hasFailedOrPendingStatus(ScheduledService service) {
        return ServiceStatus.FAILED.equals(service.getStatus())
                || ServiceStatus.PENDING.equals(service.getStatus());
    }
}
