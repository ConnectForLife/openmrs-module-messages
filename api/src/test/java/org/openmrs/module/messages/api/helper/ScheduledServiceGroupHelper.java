package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.Date;

public final class ScheduledServiceGroupHelper {
    
    private ScheduledServiceGroupHelper() {
    }
    
    public static ScheduledServiceGroup createTestInstance() {
        ScheduledServiceGroup scheduledServiceGroup = new ScheduledServiceGroup();
        scheduledServiceGroup.setMsgSendTime(new Date());
        scheduledServiceGroup.setPatient(PatientHelper.createTestInstance());
        scheduledServiceGroup.setStatus(ServiceStatus.DELIVERED);
        
        return scheduledServiceGroup;
    }
    
}
