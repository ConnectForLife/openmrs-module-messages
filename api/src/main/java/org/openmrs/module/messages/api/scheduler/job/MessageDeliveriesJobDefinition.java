/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.scheduler.job;

import java.util.List;
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
import org.openmrs.module.messages.api.mappers.ScheduledGroupMapper;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;
import org.openmrs.module.messages.api.service.MessagesDeliveryService;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.util.DateUtil;

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
            ScheduledServiceGroup group = convertAndSave(groupedResult);
            getDeliveryService().schedulerDelivery(new ScheduledServicesExecutionContext(
                    group.getScheduledServices(),
                    groupedResult.getExecutionDate(),
                    group.getActor()
            ));
        } else {
            LOGGER.warn("Status of a person with id=" + person.getId() + " is not active, " +
                    "so no service execution events will be sent");
        }
    }

    private ScheduledServiceGroup convertAndSave(GroupedServiceResultList groupedResult) {
        ScheduledServiceGroup group = getGroupMapper().fromDto(groupedResult);
        return getGroupService().saveOrUpdate(group);
    }

    private MessagingService getMessagingService() {
        return Context.getRegisteredComponent(
                MessagesConstants.MESSAGING_SERVICE, MessagingService.class);
    }

    private MessagesDeliveryService getDeliveryService() {
        return Context.getRegisteredComponent(
                MessagesConstants.DELIVERY_SERVICE, MessagesDeliveryService.class);
    }

    private ScheduledGroupMapper getGroupMapper() {
        return Context.getRegisteredComponent(
                MessagesConstants.SCHEDULED_GROUP_MAPPER, ScheduledGroupMapper.class);
    }

    private MessagingGroupService getGroupService() {
        return Context.getRegisteredComponent(
                MessagesConstants.MESSAGING_GROUP_SERVICE, MessagingGroupService.class);
    }

    private PersonService getPersonService() {
        return Context.getRegisteredComponent(
                MessagesConstants.PERSON_SERVICE, PersonService.class);
    }
}
