package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;

public interface ServiceResultHandlerService {
    void handle(ServiceResult result, ServiceResultList group);
}
