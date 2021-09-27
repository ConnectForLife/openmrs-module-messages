package org.openmrs.module.messages.api.execution;

public class GroupedServiceResult {
    private final String serviceName;
    private final ServiceResult serviceResult;

    public GroupedServiceResult(String serviceName, ServiceResult serviceResult) {
        this.serviceName = serviceName;
        this.serviceResult = serviceResult;
    }

    public String getServiceName() {
        return serviceName;
    }

    public ServiceResult getServiceResult() {
        return serviceResult;
    }
}
