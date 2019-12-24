package org.openmrs.module.messages.api.scheduler.job;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ChannelType;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.service.ServiceResultsHandlerService;
import org.openmrs.module.messages.api.util.MapperUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServiceGroupDeliveryJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(ServiceGroupDeliveryJobDefinition.class);
    private static final String TASK_NAME_PREFIX = "Group";
    private static final String GROUP_ENTITY = "GROUP_ENTITY";

    private final Gson gson = MapperUtil.getGson();

    private GroupedServiceResultList groupedServiceResults;

    public ServiceGroupDeliveryJobDefinition() {
        // initiated by scheduler
    }

    public ServiceGroupDeliveryJobDefinition(GroupedServiceResultList groupedServiceResults) {
        this.groupedServiceResults = groupedServiceResults;
    }

    @Override
    public void execute() {
        // Firstly, we need to initialize object fields basing on the saved properties
        groupedServiceResults = gson.fromJson(taskDefinition.getProperties().get(GROUP_ENTITY),
                GroupedServiceResultList.class);
        LOGGER.info(String.format("Started task with id %s", taskDefinition.getId()));
        handleGroupedResults();
    }

    @Override
    public String getTaskName() {
        return String.format("%s:%s-%s",
            TASK_NAME_PREFIX,
            this.groupedServiceResults.getActorId(),
                this.groupedServiceResults.getExecutionDate());
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
        return Collections.singletonMap(GROUP_ENTITY, gson.toJson(groupedServiceResults));
    }

    private void handleGroupedResults() {
        List<ServiceResult> smsList = new ArrayList<>();
        List<ServiceResult> calls = new ArrayList<>();

        for (ServiceResult result : groupedServiceResults.getGroup().getResults()) {
            if (result.getMessageId() == null) {
                throw new MessagesRuntimeException("Message id must be specified");
            }
            if (ChannelType.CALL.equals(result.getChannelType())) {
                calls.add(result);
            } else if (ChannelType.SMS.equals(result.getChannelType())) {
                smsList.add(result);
            } else {
                throw new MessagesRuntimeException(
                    String.format("Unsupported channel: %s", result.getChannelType()));
            }
        }

        getCallFlowsServiceResultHandlerService().handle(calls, groupedServiceResults);
        getSmsServiceResultHandlerService().handle(smsList, groupedServiceResults);
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
}
