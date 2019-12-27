package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;

public interface MessagesDeliveryService extends OpenmrsService {

    void scheduleDelivery(ScheduledServicesExecutionContext executionContext);
}
