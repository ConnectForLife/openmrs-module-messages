package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.OpenmrsObjectUtil;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

public class ScheduledExecutionContextBuilder extends AbstractBuilder<ScheduledExecutionContext> {
    public static final int ACTOR_ID = 2;
    public static final String ACTOR_TYPE = "Caregiver";
    public static final int GROUP_ID = 3;

    private static final int SERVICE_ID = 1;

    private List<Integer> serviceIdsToExecute;
    private String channelType;
    private ZonedDateTime executionDate;
    private int actorId;
    private String actorType;
    private int groupId;

    public ScheduledExecutionContextBuilder() {
        this.serviceIdsToExecute = Collections.singletonList(SERVICE_ID);
        this.executionDate = DateUtil.now();
        this.actorId = ACTOR_ID;
        this.actorType = ACTOR_TYPE;
        this.groupId = GROUP_ID;
    }

    @Override
    public ScheduledExecutionContext build() {
        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContext();
        scheduledExecutionContext.setServiceIdsToExecute(serviceIdsToExecute);
        scheduledExecutionContext.setChannelType(channelType);
        scheduledExecutionContext.setExecutionDate(executionDate.toInstant());
        scheduledExecutionContext.setActorId(actorId);
        scheduledExecutionContext.setActorType(actorType);
        scheduledExecutionContext.setGroupId(groupId);
        return scheduledExecutionContext;
    }

    @Override
    public ScheduledExecutionContext buildAsNew() {
        return build();
    }

    public ScheduledExecutionContextBuilder withScheduledServices(List<ScheduledService> scheduledServices) {
        this.serviceIdsToExecute = OpenmrsObjectUtil.getIds(scheduledServices);
        return this;
    }

    public ScheduledExecutionContextBuilder withChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public ScheduledExecutionContextBuilder withExecutionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
        return this;
    }

    public ScheduledExecutionContextBuilder withActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public ScheduledExecutionContextBuilder withActorType(String actorType) {
        this.actorType = actorType;
        return this;
    }

    public ScheduledExecutionContextBuilder withGroupId(int groupId) {
        this.groupId = groupId;
        return this;
    }
}
