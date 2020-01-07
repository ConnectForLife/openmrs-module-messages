/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.PatientTemplate;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents a list of execution for a service/message.
 */
public class ServiceResultList implements Serializable {

    private static final long serialVersionUID = 6075952817494895177L;

    private Integer patientId;
    private Integer actorId;
    private Integer serviceId;
    private String serviceName;
    private Date startDate;
    private Date endDate;
    private List<ServiceResult> results;

    public static ServiceResultList withEmptyResults(ServiceResultList other) {
        ServiceResultList result = new ServiceResultList();

        result.patientId = other.getPatientId();
        result.actorId = other.getActorId();
        result.serviceId = other.getServiceId();
        result.serviceName = other.getServiceName();
        result.startDate = other.getStartDate();
        result.endDate = other.getEndDate();
        result.results = new ArrayList<>();

        return result;
    }

    public static ServiceResultList createList(@NotNull List<Map<String, Object>> rowList, PatientTemplate patientTemplate,
                                               @NotNull Range<Date> dateRange) {
        ServiceResultList resultList = new ServiceResultList();

        List<ServiceResult> results = ServiceResult.parseList(rowList, patientTemplate);
        resultList.setResults(results);

        resultList.setPatientId(patientTemplate.getPatient().getPatientId());
        resultList.setActorId(patientTemplate.getActor().getPersonId());
        // at this moment is is always set to 0 - it should not be in ServiceResultList
        resultList.setServiceId(patientTemplate.getServiceId());

        resultList.setStartDate(dateRange.getStart());
        resultList.setEndDate(dateRange.getEnd());

        // most probably it does point to incorrect value - it should not be in ServiceResultList
        resultList.setServiceName(patientTemplate.getTemplate().getName());

        return resultList;
    }

    public Integer getPatientId() {
        return patientId;
    }

    private void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getActorId() {
        return actorId;
    }

    private void setActorId(Integer actorId) {
        this.actorId = actorId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    private void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Date getStartDate() {
        return startDate;
    }

    private void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    private void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ServiceResult> getResults() {
        return results;
    }

    public ServiceResultList withResults(List<ServiceResult> results) {
        this.results = results;
        return this;
    }

    public void setResults(List<ServiceResult> results) {
        this.results = results;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
