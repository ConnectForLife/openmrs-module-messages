package org.openmrs.module.messages.api.builder;

import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;

import java.time.ZonedDateTime;
import java.util.List;

public class ServiceResultListBuilder implements Builder<ServiceResultList> {

    private String channelType;
    private Integer patientId;
    private Integer actorId;
    private String actorType;
    private String serviceName;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private List<ServiceResult> results;

    @Override
    public ServiceResultList build() {
        ServiceResultList serviceResultList = new ServiceResultList();
        serviceResultList.setChannelType(channelType);
        serviceResultList.setPatientId(patientId);
        serviceResultList.setActorId(actorId);
        serviceResultList.setActorType(actorType);
        serviceResultList.setServiceName(serviceName);
        serviceResultList.setStartDate(startDate);
        serviceResultList.setEndDate(endDate);
        serviceResultList.setResults(results);
        return serviceResultList;
    }

    public ServiceResultListBuilder withChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public ServiceResultListBuilder withPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public ServiceResultListBuilder withActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public ServiceResultListBuilder withActorType(String actorType) {
        this.actorType = actorType;
        return this;
    }

    public ServiceResultListBuilder withServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ServiceResultListBuilder withStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
        return this;
    }

    public ServiceResultListBuilder withEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
        return this;
    }

    public ServiceResultListBuilder withResults(List<ServiceResult> results) {
        this.results = results;
        return this;
    }

}
