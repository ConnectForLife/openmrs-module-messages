package org.openmrs.module.messages.api.scheduler.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ServiceGroupDeliveryJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(ServiceGroupDeliveryJobDefinition.class);
    private static final String TASK_NAME_PREFIX = "Service Group Delivery for group: ";

    @Override
    public void execute() {
        LOGGER.info(getTaskName() + " started");
        //TODO: Add logic (as a part of CFLM-184)
    }

    @Override
    public String getTaskName() {
        //TODO: Add actorId and executionDime to be unique
        return TASK_NAME_PREFIX;
    }

    @Override
    public boolean shouldStartAtFirstCreation() {
        return false;
    }

    @Override
    public Class getTaskClass() {
        return ServiceGroupDeliveryJobDefinition.class;
    }
}
