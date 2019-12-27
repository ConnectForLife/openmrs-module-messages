package org.openmrs.module.messages.api.service.impl;

import javax.transaction.Transactional;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;

public class MessagesDeliveryServiceImpl extends BaseOpenmrsService implements MessagesDeliveryService {

    private MessagesSchedulerService schedulerService;

    @Override
    @Transactional
    public void schedulerDelivery(ScheduledServicesExecutionContext executionContext) {
        JobDefinition definition = new ServiceGroupDeliveryJobDefinition(executionContext);
        schedulerService.createNewTask(definition, executionContext.getExecutionDate(), JobRepeatInterval.NEVER);
    }

    public void setSchedulerService(MessagesSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }
}
