package org.openmrs.module.messages.api.service.impl;

import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.ServiceGroupDeliveryJobDefinition;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.module.messages.api.service.MessagingGroupService;

import java.util.Date;

public class MessagesDeliveryServiceImpl extends BaseOpenmrsService implements MessagesDeliveryService {

    private MessagesSchedulerService schedulerService;
    private ScheduledGroupMapper groupMapper;
    private MessagingGroupService groupService;

    @Override
    public void schedulerDelivery(GroupedServiceResultList groupedResult) {
        Date startDate = groupedResult.getExecutionDate();
        JobDefinition definition = new ServiceGroupDeliveryJobDefinition(groupedResult);
        groupService.saveOrUpdate(groupMapper.fromDto(groupedResult));
        schedulerService.createNewTask(definition, startDate, JobRepeatInterval.NEVER);
    }

    public void setSchedulerService(MessagesSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    public void setGroupMapper(ScheduledGroupMapper groupMapper) {
        this.groupMapper = groupMapper;
    }

    public void setGroupService(MessagingGroupService groupService) {
        this.groupService = groupService;
    }
}
