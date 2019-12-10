package org.openmrs.module.messages.api.scheduler.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class MessageDeliveriesJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(MessageDeliveriesJobDefinition.class);
    private static final String TASK_NAME = "Message Deliveries Job Task";

    @Override
    public void execute() {
        LOGGER.info(getTaskName() + " started");
        try {
            List<ServiceResultList> results = getMessagingService().retrieveAllServiceExecutions(DateUtil.now(),
                    DateUtil.getDatePlusSeconds(getTaskDefinition().getRepeatInterval()));
            List<ServiceResultList> groupedResults = new ArrayList<>();
            for (ServiceResultList result : results) {
                //TODO: group by actorId and executionDate batches (as a part of CFLM-184)
                groupedResults.add(result);
            }
            for (ServiceResultList group : groupedResults) {
                //TODO: create job definition for group and schedule it (as a part of CFLM-184)
                new ServiceGroupDeliveryJobDefinition();
            }
        } catch (ExecutionException e) {
            LOGGER.error("Failed to execute task: " + getTaskName());
            throw new MessagesRuntimeException("Failed to execute task: " + getTaskName(), e);
        }
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

    private MessagingService getMessagingService() {
        return Context.getRegisteredComponent(
                MessagesConstants.MESSAGING_SERVICE, MessagingService.class);
    }
}
