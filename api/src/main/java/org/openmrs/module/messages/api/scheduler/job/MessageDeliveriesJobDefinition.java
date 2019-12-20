package org.openmrs.module.messages.api.scheduler.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResultGroupHelper;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.List;

public class MessageDeliveriesJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(MessageDeliveriesJobDefinition.class);
    private static final String TASK_NAME = "Message Deliveries Job Task";

    @Override
    public void execute() {
        LOGGER.info(getTaskName() + " started");
        try {
            List<ServiceResultList> results =
                getMessagingService().retrieveAllServiceExecutions(DateUtil.now(),
                DateUtil.getDatePlusSeconds(getTaskDefinition().getRepeatInterval()));

            List<GroupedServiceResultList> groupedResults = ServiceResultGroupHelper
                .groupByActorAndExecutionDate(results);

            for (GroupedServiceResultList groupedResult : groupedResults) {
                scheduleTaskForActivePerson(groupedResult);
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

    private void scheduleTaskForActivePerson(GroupedServiceResultList groupedResult) {
        Person person = getPersonService().getPerson(groupedResult.getActorId());
        if (PersonStatus.isActive(person)) {
            getDeliveryService().schedulerDelivery(groupedResult);
        } else {
            LOGGER.warn("Status of a person with id=" + person.getId() + " is not active, " +
                    "so no service execution events will be sent");
        }
    }

    private MessagingService getMessagingService() {
        return Context.getRegisteredComponent(
            MessagesConstants.MESSAGING_SERVICE, MessagingService.class);
    }

    private MessagesDeliveryService getDeliveryService() {
        return Context.getRegisteredComponent(
            MessagesConstants.DELIVERY_SERVICE, MessagesDeliveryService.class);
    }

    private PersonService getPersonService() {
        return Context.getRegisteredComponent(
                MessagesConstants.PERSON_SERVICE, PersonService.class);
    }
}
