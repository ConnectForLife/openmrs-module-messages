/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_CONFIG;
import static org.openmrs.module.messages.api.constants.MessagesConstants.CALLFLOWS_DEFAULT_FLOW;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.ADDITIONAL_PARAMS;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.CONFIG;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.FLOW_NAME;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.PHONE;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.SERVICES;
import static org.openmrs.module.messages.api.event.CallFlowParamConstants.SERVICE_OBJECTS;

import com.google.gson.Gson;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.event.MessagesEvent;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;

public class CallFlowServiceResultsHandlerServiceImpl extends AbstractServiceResultsHandlerService {
    private static final String CALL_FLOW_INITIATE_CALL_EVENT = "callflows-call-initiate";

    private static final Log LOG = LogFactory.getLog(CallFlowServiceResultsHandlerServiceImpl.class);

    @Override
    public void handle(List<ScheduledService> callServices, ScheduledServicesExecutionContext executionContext) {
        triggerEvent(callServices, executionContext);
    }

    private void triggerEvent(List<ScheduledService> callServices, ScheduledServicesExecutionContext executionContext) {
        if (callServices.isEmpty()) {
            LOG.info(String.format("Handling of callflow for actor: %s, executionDate: %s has been skipped because "
                    + "of empty services list", executionContext.getActorId(), executionContext.getExecutionDate()));
        } else {
            MessagesEvent messagesEvent = buildMessage(callServices, executionContext);
            sendEventMessage(messagesEvent);
        }
    }

    private MessagesEvent buildMessage(List<ScheduledService> callServices,
                                       ScheduledServicesExecutionContext executionContext) {
        Map<String, Object> params = new HashMap<>();
        params.put(CONFIG, CALLFLOWS_DEFAULT_CONFIG);
        params.put(FLOW_NAME, CALLFLOWS_DEFAULT_FLOW);

        String personPhone = getPersonPhone(executionContext.getActorId());

        Map<String, Object> additionalParams = new HashMap<>();
        List<Service> services = getServices(callServices);
        additionalParams.put(SERVICE_OBJECTS, new Gson().toJson(services));
        additionalParams.put(SERVICES, extractNameList(services));
        additionalParams.put(PHONE, personPhone);

        params.put(ADDITIONAL_PARAMS, additionalParams);

        return new MessagesEvent(CALL_FLOW_INITIATE_CALL_EVENT, params);
    }

    private List<Service> getServices(List<ScheduledService> callServices) {
        List<Service> services = new ArrayList<>();
        for (ScheduledService service : callServices) {
            String serviceName = service.getPatientTemplate().getTemplate().getName();
            services.add(new Service(
                    serviceName,
                    service.getId(),
                    service.getParameters()));
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

    private static final class Service implements Serializable {

        private static final long serialVersionUID = -8316289727827489159L;

        private final String name;
        private final int messageId;
        private final Map<String, String> params;

        private Service(String name, int messageId, Map<String, String> params) {
            this.name = name;
            this.messageId = messageId;
            this.params = params;
        }

        public String getName() {
            return name;
        }

        public int getMessageId() {
            return messageId;
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
