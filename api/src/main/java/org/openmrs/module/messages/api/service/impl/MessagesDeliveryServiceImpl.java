package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;

import java.util.Date;

public class MessagesDeliveryServiceImpl extends BaseOpenmrsService implements MessagesDeliveryService {

    private MessagesSchedulerService schedulerService;

    public void setSchedulerService(MessagesSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @Override
    public void schedulerDelivery(GroupedServiceResultList groupedResult) {
        Date startDate = groupedResult.getExecutionDate();
        JobDefinition definition = new ServiceGroupDeliveryJobDefinition(groupedResult);
        schedulerService.createNewTask(definition, startDate, JobRepeatInterval.NEVER);
    }
}
