package org.openmrs.module.messages.api.service;

import java.util.List;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServicesExecutionContext;

public interface ServiceResultsHandlerService {
    void handle(List<ScheduledService> services, ScheduledServicesExecutionContext executionContext);
}
