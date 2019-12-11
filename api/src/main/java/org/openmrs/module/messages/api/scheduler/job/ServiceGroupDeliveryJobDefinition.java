package org.openmrs.module.messages.api.scheduler.job;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.service.MessagesEventService;
import org.openmrs.module.messages.api.util.DateUtil;

import java.text.DateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ServiceGroupDeliveryJobDefinition extends JobDefinition {

    private static final Log LOGGER = LogFactory.getLog(ServiceGroupDeliveryJobDefinition.class);
    private static final String TASK_NAME_PREFIX = "Group";
    private static final String GROUP_ENTITY = "GROUP_ENTITY";
    private static final int CALL_FLOW_TYPE = 0; //TODO: CFLM-184: Extract to const
    private static final int SMS_TYPE = 1; //TODO: CFLM-184: Extract to const

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
        LOGGER.info(getTaskName() + " started");
        group = gson.fromJson(taskDefinition.getProperties().get(GROUP_ENTITY),
            ServiceResultList.class);
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
        for (ServiceResult result : results) {
            switch (result.getChannelId()) {
                case CALL_FLOW_TYPE:
                    triggerCallFlowEvent(result);
                    break;
                case SMS_TYPE:
                    triggerSmsEvent(result);
                    break;
                default:
                    throw new MessagesRuntimeException(
                        String.format("Unsupported channel id: %d", result.getChannelId()));
            }
        }
    }

    private void triggerCallFlowEvent(ServiceResult result) {
        LOGGER.debug(String.format("%s: Callflow event triggered", getTaskName()));
        //TODO: CFLM-184: Trigger call flow event
        getEventService().sendEventMessage(new MessagesEvent(null, null));
        LOGGER.debug(result);
    }

    private void triggerSmsEvent(ServiceResult result) {
        LOGGER.debug(String.format("%s: Sms event triggered", getTaskName()));
        //TODO: CFLM-184: Trigger sms event
        getEventService().sendEventMessage(new MessagesEvent(null, null));
        LOGGER.debug(result);
    }

    private MessagesEventService getEventService() {
        return Context.getRegisteredComponent(
                MessagesConstants.CONFIG_SERVICE, MessagesEventService.class);
    }
}
