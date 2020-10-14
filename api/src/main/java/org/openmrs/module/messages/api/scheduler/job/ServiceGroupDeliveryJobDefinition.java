/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.scheduler.job;

import com.google.gson.Gson;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.ServiceResultsHandlerService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.JsonUtil;
import org.openmrs.module.messages.domain.criteria.ScheduledServiceCriteria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceGroupDeliveryJobDefinition extends JobDefinition {

    public static final String EXECUTION_CONTEXT = "EXECUTION_CONTEXT";

    private static final Log LOGGER = LogFactory.getLog(ServiceGroupDeliveryJobDefinition.class);
    private static final String SHORT_DATE_FORMAT = "yyyyMMddHHmmz";

    private final Gson gson = JsonUtil.getGson();

    private ScheduledExecutionContext executionContext;

    public ServiceGroupDeliveryJobDefinition() {
        // initiated by scheduler
    }

    public ServiceGroupDeliveryJobDefinition(ScheduledExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    @Override
    public void execute() {
        // Firstly, we need to initialize object fields basing on the saved properties
        executionContext = gson.fromJson(taskDefinition.getProperties().get(EXECUTION_CONTEXT),
                ScheduledExecutionContext.class);
        LOGGER.info(String.format("Started task with id %s", taskDefinition.getId()));
        handleGroupedResults();
    }

    @Override
    public String getTaskName() {
        return String.format("a:%dp:%dd:%s",
            this.executionContext.getActorId(),
            this.executionContext.getPatientId(),
            DateUtil.convertDate(this.executionContext.getExecutionDate(), SHORT_DATE_FORMAT));
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
        return wrapByMap(EXECUTION_CONTEXT, gson.toJson(executionContext));
    }

    private void handleGroupedResults() {
        Map<String, List<ScheduledService>> groupedServicesByChannel = groupByChannelType();

        for (String channelType : groupedServicesByChannel.keySet()) {
            ServiceResultsHandlerService handler = getConfigService().getResultsHandlerOrDefault(channelType);
            handler.handle(groupedServicesByChannel.get(channelType), executionContext);
        }
    }

    private Map<String, List<ScheduledService>> groupByChannelType() {
        Map<String, List<ScheduledService>> groupedServicesByChannel = new HashMap<>();
        for (ScheduledService service : getScheduledServices()) {
            if (MessagesConstants.DEACTIVATED_SERVICE.equalsIgnoreCase(service.getChannelType())) {
                LOGGER.info(String.format("Skipped task handling with id %s due to channel deactivation",
                        taskDefinition.getId()));
            } else {
                String channelType = service.getChannelType();
                String[] splittedChannelTypes;
                if (channelType.contains(",")) {
                    splittedChannelTypes = channelType.split(",");
                    for (String channel : splittedChannelTypes) {
                        if (!groupedServicesByChannel.containsKey(channel)) {
                            groupedServicesByChannel.put(channel, new ArrayList<>());
                        }
                        groupedServicesByChannel.get(channel).add(service);
                    }
                } else {
                    if (!groupedServicesByChannel.containsKey(channelType)) {
                        groupedServicesByChannel.put(channelType, new ArrayList<>());
                    }
                    groupedServicesByChannel.get(channelType).add(service);
                }
            }
        }
        return groupedServicesByChannel;
    }

    private List<ScheduledService> getScheduledServices() {
        return getMessagingService().findAllByCriteria(
                ScheduledServiceCriteria.forIds(executionContext.getServiceIdsToExecute())
        );
    }

    private MessagingService getMessagingService() {
        return Context.getRegisteredComponent(
                MessagesConstants.MESSAGING_SERVICE,
                MessagingService.class);
    }

    private Map<String, String> wrapByMap(String key, String value) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    private ConfigService getConfigService() {
        return Context.getRegisteredComponent(
                MessagesConstants.CONFIG_SERVICE, ConfigService.class);
    }
}
