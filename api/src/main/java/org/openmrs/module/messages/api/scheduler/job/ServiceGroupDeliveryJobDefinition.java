package org.openmrs.module.messages.api.scheduler.job;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.ServiceResultsHandlerService;
import org.openmrs.module.messages.api.util.MapperUtil;
import org.openmrs.module.messages.domain.criteria.ScheduledServiceCriteria;

public class ServiceGroupDeliveryJobDefinition extends JobDefinition {

    public static final String EXECUTION_CONTEXT = "EXECUTION_CONTEXT";
    
    private static final Log LOGGER = LogFactory.getLog(ServiceGroupDeliveryJobDefinition.class);
    private static final String TASK_NAME_PREFIX = "Group";

    private final Gson gson = MapperUtil.getGson();

    private ScheduledServicesExecutionContext executionContext;

    public ServiceGroupDeliveryJobDefinition() {
        // initiated by scheduler
    }

    public ServiceGroupDeliveryJobDefinition(ScheduledServicesExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public void execute() {
        // Firstly, we need to initialize object fields basing on the saved properties
        executionContext = gson.fromJson(taskDefinition.getProperties().get(EXECUTION_CONTEXT),
                ScheduledServicesExecutionContext.class);
        LOGGER.info(String.format("Started task with id %s", taskDefinition.getId()));
        handleGroupedResults();
    }

    @Override
    public String getTaskName() {
        return String.format("%s:%s-%s",
            TASK_NAME_PREFIX,
            this.executionContext.getActorId(),
            this.executionContext.getExecutionDate());
    }

    @Override
    public boolean shouldStartAtFirstCreation() {
        return false;
    }

    @Override
    public Class getTaskClass() {
        return ServiceGroupDeliveryJobDefinition.class;
    }

    @Override
    public Map<String, String> getProperties() {
        return Collections.singletonMap(EXECUTION_CONTEXT, gson.toJson(executionContext));
    }

    private void handleGroupedResults() {

        List<ScheduledService> smsList = new ArrayList<>();
        List<ScheduledService> calls = new ArrayList<>();

        for (ScheduledService service : getScheduledServices()) {
            if (ChannelType.CALL.getName().equalsIgnoreCase(service.getChannelType())) {
                calls.add(service);
            } else if (ChannelType.SMS.getName().equalsIgnoreCase(service.getChannelType())) {
                smsList.add(service);
            } else if (ChannelType.DEACTIVATED.getName().equalsIgnoreCase(service.getChannelType())) {
                LOGGER.info(String.format("Skipped task handling with id %s due to channel " +
                        "deactivation", taskDefinition.getId()));
            } else {
                throw new MessagesRuntimeException(
                    String.format("Unsupported channel: %s", service.getChannelType()));
            }
        }

        getCallFlowsServiceResultHandlerService().handle(calls, executionContext);
        getSmsServiceResultHandlerService().handle(smsList, executionContext);
    }

    private List<ScheduledService> getScheduledServices() {
        return getMessagingService().findAllByCriteria(
                ScheduledServiceCriteria.forIds(executionContext.getServiceIdsToExecute())
        );
    }

    private ServiceResultsHandlerService getCallFlowsServiceResultHandlerService() {
        return Context.getRegisteredComponent(
            MessagesConstants.CALL_FLOW_SERVICE_RESULT_HANDLER_SERVICE,
            ServiceResultsHandlerService.class);
    }

    private ServiceResultsHandlerService getSmsServiceResultHandlerService() {
        return Context.getRegisteredComponent(
            MessagesConstants.SMS_SERVICE_RESULT_HANDLER_SERVICE,
            ServiceResultsHandlerService.class);
    }

    private MessagingService getMessagingService() {
        return Context.getRegisteredComponent(
                MessagesConstants.MESSAGING_SERVICE,
                MessagingService.class);
    }
}
