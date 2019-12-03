package org.openmrs.module.messages.api.scheduler.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MessageDeliveriesJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(MessageDeliveriesJobDefinition.class);
    private static final String TASK_NAME = "Message Deliveries Job Task";

    @Override
    public void execute() {
        LOGGER.info("Message Deliveries Job started");
        //TODO: Add logic (as a part of CFLM-184)
    }

    @Override
    public String getTaskName() {
        return TASK_NAME;
    }

    @Override
    public boolean shouldStartAtFirstCreation() {
        return true;
    }

    @Override
    public Class getTaskClass() {
        return MessageDeliveriesJobDefinition.class;
    }
}
