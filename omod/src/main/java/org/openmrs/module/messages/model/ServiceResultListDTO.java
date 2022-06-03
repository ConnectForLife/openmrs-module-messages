/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.model;

import org.openmrs.module.messages.api.execution.ServiceResultList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The DTO for {@link ServiceResultList}.
 */
public class ServiceResultListDTO {
    private String channelType;
    private Integer patientId;
    private Integer actorId;
    private String actorType;
    private String serviceName;
    private Long startDate;
    private Long endDate;
    private List<ServiceResultDTO> results;

    public ServiceResultListDTO() {
    }

    public ServiceResultListDTO(ServiceResultList serviceResultList) {
        this.channelType = serviceResultList.getChannelType();
        this.patientId = serviceResultList.getPatientId();
        this.actorId = serviceResultList.getActorId();
        this.actorType = serviceResultList.getActorType();
        this.serviceName = serviceResultList.getServiceName();
        this.startDate = serviceResultList.getStartDate().toInstant().toEpochMilli();
        this.endDate = serviceResultList.getEndDate().toInstant().toEpochMilli();
        this.results = Collections.unmodifiableList(
                serviceResultList.getResults().stream().map(ServiceResultDTO::new).collect(Collectors.toList()));
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getActorId() {
        return actorId;
    }

    public void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Long getStartDate() {
        return startDate;
    }

    public void setStartDate(Long startDate) {
        this.startDate = startDate;
    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public List<ServiceResultDTO> getResults() {
        return results;
    }

    public void setResults(List<ServiceResultDTO> results) {
        this.results = results;
    }
}
