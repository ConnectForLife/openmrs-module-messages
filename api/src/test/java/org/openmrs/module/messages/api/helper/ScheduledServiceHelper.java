package org.openmrs.module.messages.api.helper;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.TestConstants;

public final class ScheduledServiceHelper {
    
    private ScheduledServiceHelper() {
    }
    
    public static ScheduledService createTestInstance() {
        ScheduledService scheduledService = new ScheduledService();
        scheduledService.setService(Context.getConceptService().getConcept(TestConstants.TEST_CONCEPT_ID));
        scheduledService.setChannelType(Context.getConceptService().getConcept(TestConstants.TEST_CONCEPT_ID));
        scheduledService.setStatus(ServiceStatus.DELIVERED);
        scheduledService.setLastServiceExecution(TestConstants.TEST_SERVICE_EXECUTION_ID);
        
        return scheduledService;
    }
}
