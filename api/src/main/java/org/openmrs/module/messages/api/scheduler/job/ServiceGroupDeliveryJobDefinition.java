package org.openmrs.module.messages.api.scheduler.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.service.ServiceResultHandlerService;
import org.openmrs.module.messages.api.util.DateUtil;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServiceGroupDeliveryJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(ServiceGroupDeliveryJobDefinition.class);
    private static final String TASK_NAME_PREFIX = "Group";
    private static final String GROUP_ENTITY = "GROUP_ENTITY";

    private final Gson gson =
        new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();

    private ServiceResultList group;

    public ServiceGroupDeliveryJobDefinition() {
        // initiated by scheduler
    }

    public ServiceGroupDeliveryJobDefinition(ServiceResultList group) {
        this.group = group;
    }

    @Override
    public void execute() {
        // Firstly, we need to initialize object fields basing on the saved properties
        group = gson.fromJson(taskDefinition.getProperties().get(GROUP_ENTITY),
            ServiceResultList.class);
        LOGGER.info(String.format("Started task with id %s", taskDefinition.getId()));
        handleGroupedResults();
    }

    @Override
    public String getTaskName() {
        return String.format("%s:%s-%s",
            TASK_NAME_PREFIX,
            this.group.getActorId(),
            DateUtil.now().toInstant());
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
        return Collections.singletonMap(GROUP_ENTITY, gson.toJson(group));
    }

    private void handleGroupedResults() {
        List<ServiceResult> results = group.getResults();
        boolean isCallFlowHandledAlready = false;
        for (ServiceResult result : results) {
            if (result.getMessageId() == null) {
                throw new MessagesRuntimeException("Message id must be specified");
            }
            switch (result.getChannelType()) {
                case CALL:
                    if (!isCallFlowHandledAlready) {
                        getCallFlowsServiceResultHandlerService().handle(result, group);
                    }
                    isCallFlowHandledAlready = true;
                    break;
                case SMS:
                    getSmsServiceResultHandlerService().handle(result, group);
                    break;
                default:
                    throw new MessagesRuntimeException(
                        String.format("Unsupported channel: %s", result.getChannelType()));
            }
        }
    }

    private ServiceResultHandlerService getCallFlowsServiceResultHandlerService() {
        return Context.getRegisteredComponent(
            MessagesConstants.CALL_FLOW_SERVICE_RESULT_HANDLER_SERVICE,
            ServiceResultHandlerService.class);
    }

    private ServiceResultHandlerService getSmsServiceResultHandlerService() {
        return Context.getRegisteredComponent(
            MessagesConstants.SMS_SERVICE_RESULT_HANDLER_SERVICE,
            ServiceResultHandlerService.class);
    }
}
