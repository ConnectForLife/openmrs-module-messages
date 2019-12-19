package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResult;

import java.util.List;

public interface ServiceResultsHandlerService {
    void handle(List<ServiceResult> result, GroupedServiceResultList group);
}
