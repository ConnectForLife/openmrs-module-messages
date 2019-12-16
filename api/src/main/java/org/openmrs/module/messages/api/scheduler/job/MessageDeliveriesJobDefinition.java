package org.openmrs.module.messages.api.scheduler.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultGroupHelper;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientStatus;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.Date;
import java.util.List;

import static org.openmrs.module.messages.api.util.PersonAttributeUtil.getPersonStatus;

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

            int groupingPeriod = getConfigService().getGroupingPeriodInSeconds();
            List<ServiceResultList> groupedResults = ServiceResultGroupHelper
                .groupByActorIdAndExecutionDate(results, groupingPeriod);

            for (ServiceResultList group : groupedResults) {
                JobDefinition definition = new ServiceGroupDeliveryJobDefinition(group);
                Date startDate = getGroupResultsStartDate(group.getResults());
                if (PatientStatus.ACTIVE.equals(getPersonStatus(getPersonService().getPerson(group.getActorId())))) {
                    getSchedulerService().createNewTask(definition, startDate, JobRepeatInterval.NEVER);
                }
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

    private MessagesSchedulerService getSchedulerService() {
        return Context.getRegisteredComponent(
            MessagesConstants.SCHEDULER_SERVICE, MessagesSchedulerService.class);
    }

    private ConfigService getConfigService() {
        return Context.getRegisteredComponent(
            MessagesConstants.CONFIG_SERVICE, ConfigService.class);
    }

    private PersonService getPersonService() {
        return Context.getRegisteredComponent(
                MessagesConstants.PERSON_SERVICE, PersonService.class);
    }

    private Date getGroupResultsStartDate(List<ServiceResult> results) {
        return ServiceResultGroupHelper.getEarliestDate(results);
    }
}
