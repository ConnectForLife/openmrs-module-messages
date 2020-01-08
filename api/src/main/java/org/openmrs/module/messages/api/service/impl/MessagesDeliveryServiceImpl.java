/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import javax.transaction.Transactional;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;

@Transactional
public class MessagesDeliveryServiceImpl extends BaseOpenmrsService implements MessagesDeliveryService {

    private MessagesSchedulerService schedulerService;

    @Override
    public void scheduleDelivery(ScheduledExecutionContext executionContext) {
        JobDefinition definition = new ServiceGroupDeliveryJobDefinition(executionContext);
        schedulerService.createNewTask(definition, executionContext.getExecutionDate(), JobRepeatInterval.NEVER);
    }

    public void setSchedulerService(MessagesSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }
}
