package org.openmrs.module.messages.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import javax.transaction.Transactional;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.service.MessagingService;

public class MessagingServiceImpl extends BaseOpenmrsDataService<ScheduledService> implements MessagingService {

    @Transactional
    public ScheduledService registerAttempt(int scheduledServiceId, ServiceStatus status, Date timestamp,
            String executionId) {
        ScheduledService scheduledServices = getById(scheduledServiceId);

        DeliveryAttempt deliveryAttempt = new DeliveryAttempt();
        deliveryAttempt.setServiceExecution(executionId);
        deliveryAttempt.setAttemptNumber(scheduledServices.getDeliveryAttempts().size() + 1);
        deliveryAttempt.setStatus(status);
        deliveryAttempt.setTimestamp(timestamp);
        deliveryAttempt.setScheduledService(scheduledServices);

        scheduledServices.setStatus(status);
        scheduledServices.setLastServiceExecution(executionId);

        ArrayList<DeliveryAttempt> deliveryAttempts = new ArrayList<>(scheduledServices.getDeliveryAttempts());
        deliveryAttempts.add(deliveryAttempt);
        scheduledServices.setDeliveryAttempts(deliveryAttempts);

        return saveOrUpdate(scheduledServices);
    }
}
