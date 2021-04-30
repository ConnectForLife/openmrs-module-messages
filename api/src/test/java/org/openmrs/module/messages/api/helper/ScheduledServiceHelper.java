package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.TestConstants;

public final class ScheduledServiceHelper {
    
    private ScheduledServiceHelper() {
    }
    
    public static ScheduledService createTestInstance() {
        ScheduledService scheduledService = new ScheduledService();
        scheduledService.setService(TestConstants.TEST_SERVICE);
        scheduledService.setStatus(ServiceStatus.DELIVERED);
        scheduledService.setLastServiceExecution(TestConstants.TEST_SERVICE_EXECUTION_ID);
        
        return scheduledService;
    }
}
