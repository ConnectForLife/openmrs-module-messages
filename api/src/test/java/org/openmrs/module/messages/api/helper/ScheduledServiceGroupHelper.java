package org.openmrs.module.messages.api.helper;

import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.DateUtil;

public final class ScheduledServiceGroupHelper {
    
    private ScheduledServiceGroupHelper() {
    }
    
    public static ScheduledServiceGroup createTestInstance() {
        ScheduledServiceGroup scheduledServiceGroup = new ScheduledServiceGroup();
        scheduledServiceGroup.setMsgSendTime(DateUtil.now());
        scheduledServiceGroup.setPatient(PatientHelper.createTestInstance());
        scheduledServiceGroup.setActor(PatientHelper.createTestInstance().getPerson());
        scheduledServiceGroup.setStatus(ServiceStatus.DELIVERED);
        scheduledServiceGroup.setChannelType("Call");
        
        return scheduledServiceGroup;
    }
    
}
