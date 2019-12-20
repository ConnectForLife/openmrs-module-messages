package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;

public interface MessagesDeliveryService extends OpenmrsService {

    void schedulerDelivery(GroupedServiceResultList groupedResult);
}
