package org.openmrs.module.messages.api.service;

import java.util.Date;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

public interface MessagingService extends OpenmrsDataService<ScheduledService> {

    /**
     * The API to be called by other modules, such as callflows and sms, in order to update the delivery status of a
     * given service scheduled.
     *
     * @param scheduledServiceId is the id of the ScheduledService for which the status should be updated
     * @param status is a new value of a service delivery which should be set (eg. delivered, failed)
     * @param timestamp describes the time when the update took place
     * @param executionId is the id coming in from the module that is doing the messaging (eg. from the sms module)
     * @return the updated scheduled service
     */
    ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp, String executionId);
}
