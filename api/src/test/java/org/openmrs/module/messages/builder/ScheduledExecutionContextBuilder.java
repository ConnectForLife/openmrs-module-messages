package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.util.OpenmrsObjectUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ScheduledExecutionContextBuilder extends AbstractBuilder<ScheduledExecutionContext> {
    public static final Date EXECUTION_DATE = new Date();
    public static final int ACTOR_ID = 2;
    public static final String ACTOR_TYPE = "Caregiver";
    public static final int GROUP_ID = 3;

    private static final int SERVICE_ID = 1;

    private List<Integer> serviceIdsToExecute;
    private Date executionDate;
    private int actorId;
    private String actorType;
    private int groupId;

    public ScheduledExecutionContextBuilder() {
        this.serviceIdsToExecute = Collections.singletonList(SERVICE_ID);
        this.executionDate = EXECUTION_DATE;
        this.actorId = ACTOR_ID;
        this.actorType = ACTOR_TYPE;
        this.groupId = GROUP_ID;
    }

    @Override
    public ScheduledExecutionContext build() {
        ScheduledExecutionContext scheduledExecutionContext = new ScheduledExecutionContext();
        scheduledExecutionContext.setServiceIdsToExecute(serviceIdsToExecute);
        scheduledExecutionContext.setExecutionDate(executionDate);
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

    public ScheduledExecutionContextBuilder withExecutionDate(Date executionDate) {
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
