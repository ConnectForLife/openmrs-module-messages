package org.openmrs.module.messages.api.service.impl;

import com.google.gson.Gson;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.service.MessagingService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_CONFIG;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_FLOW;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ADDITIONAL_PARAMS;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.CONFIG;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.FLOW_NAME;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.PHONE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.SERVICES;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.SERVICE_NAME;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.SERVICE_OBJECTS;

public class CallFlowServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {
    private static final String CALL_FLOW_INITIATE_CALL_EVENT = "callflows-call-initiate";
    private static final String MESSAGING_SERVICE_BEAN_NAME = "messages.messagingService";

    @Override
    public void handle(List<ServiceResult> results, GroupedServiceResultList group) {
        triggerEvent(results, group);
    }

    private void triggerEvent(List<ServiceResult> results, GroupedServiceResultList group) {
        MessagesEvent messagesEvent = buildMessage(results, group);
        sendEventMessage(messagesEvent);
    }

    private MessagesEvent buildMessage(List<ServiceResult> results, GroupedServiceResultList group) {
        Map<String, Object> params = new HashMap<>();
        params.put(CONFIG, CALLFLOWS_DEFAULT_CONFIG);
        params.put(FLOW_NAME, CALLFLOWS_DEFAULT_FLOW);

        String personPhone = getPersonPhone(group.getActorId());

        Map<String, Object> additionalParams = new HashMap<>();
        List<Service> services = getServices(results);
        additionalParams.put(SERVICE_OBJECTS, new Gson().toJson(services));
        additionalParams.put(SERVICES, extractNameList(services));
        additionalParams.put(PHONE, personPhone);
        additionalParams.put(SERVICE_NAME, group.getGroup().getServiceName());

        params.put(ADDITIONAL_PARAMS, additionalParams);

        return new MessagesEvent(CALL_FLOW_INITIATE_CALL_EVENT, params);
    }

    private List<Service> getServices(List<ServiceResult> results) {
        List<Service> services = new ArrayList<>();
        MessagingService messagingService = getMessagingService();
        for (ServiceResult result : results) {
            ScheduledService scheduledService = messagingService.getById((Serializable) result.getMessageId());
            String serviceName = scheduledService.getPatientTemplate().getTemplate().getName();
            services.add(new Service(serviceName, result.getMessageId()));
        }
        return services;
    }

    private List<String> extractNameList(List<Service> services) {
        List<String> names = new ArrayList<>();
        for (Service service : services) {
            names.add(service.getName());
        }
        return names;
    }

    private MessagingService getMessagingService() {
        return Context.getRegisteredComponent(
                MESSAGING_SERVICE_BEAN_NAME,
                MessagingService.class);
    }

    private static final class Service implements Serializable {

        private static final long serialVersionUID = -8316289727827489159L;

        private final String name;
        private final Object messageId;

        private Service(String name, Object messageId) {
            this.name = name;
            this.messageId = messageId;
        }

        public String getName() {
            return name;
        }

        public Object getMessageId() {
            return messageId;
        }
    }
}
