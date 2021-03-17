/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.builder.ServiceResultListBuilder;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.BestContactTimeHelper;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents a list of execution for a service/message.
 */
public class ServiceResultList implements Serializable {

    private static final long serialVersionUID = 6075952817494895177L;
    private static final Log LOGGER = LogFactory.getLog(ServiceResultList.class);

    private Integer patientId;
    private Integer actorId;
    private String actorType;
    private Integer serviceId;
    private String serviceName;
    private Date startDate;
    private Date endDate;
    private List<ServiceResult> results;

    public static ServiceResultList withEmptyResults(ServiceResultList other) {
        ServiceResultList result = new ServiceResultList();

        result.patientId = other.getPatientId();
        result.actorId = other.getActorId();
        result.actorType = other.getActorType();
        result.serviceId = other.getServiceId();
        result.serviceName = other.getServiceName();
        result.startDate = other.getStartDate();
        result.endDate = other.getEndDate();
        result.results = new ArrayList<>();

        return result;
    }

    public static List<ServiceResultList> createList(@NotNull List<Map<String, Object>> rowList, Template template,
                                                     Range<Date> dateTimeRange) {
        List<ServiceResultList> serviceResultsLists = new ArrayList<>();
        for (Map<String, Object> row : rowList) {
            ServiceResult serviceResult = ServiceResult.parse(row);
            PatientTemplate patientTemplate = getRelatedPatientTemplate(serviceResult, template);
            if (patientTemplate != null) {
                serviceResult.setPatientTemplateId(patientTemplate.getId());
                serviceResult.setChannelType(getTemplateFieldValue(patientTemplate,
                        MessagesConstants.CHANNEL_TYPE_PARAM_NAME));
                serviceResult.setExecutionDate(adjustExecutionDateToBestContactTime(serviceResult, patientTemplate));

                ServiceResultList serviceResultList = new ServiceResultListBuilder()
                        .withPatientId(serviceResult.getPatientId())
                        .withActorId(serviceResult.getActorId())
                        .withActorType(patientTemplate.getActorTypeAsString())
                        .withServiceId(patientTemplate.getServiceId())
                        .withServiceName(template.getName())
                        .withStartDate(dateTimeRange.getStart())
                        .withEndDate(dateTimeRange.getEnd())
                        .withResults(Arrays.asList(serviceResult))
                        .build();

                serviceResultsLists.add(serviceResultList);
            } else if (LOGGER.isTraceEnabled()) {
                LOGGER.trace(String.format("Patient template for patient id: %d and service type: %s " +
                            "does not exist or is deactivated", serviceResult.getPatientId(), template.getName()));
            }
        }

        return serviceResultsLists;
    }

    public static ServiceResultList createList(@NotNull List<Map<String, Object>> rowList, PatientTemplate patientTemplate,
                                               @NotNull Range<Date> dateRange) {
        ServiceResultList resultList = new ServiceResultList();

        List<ServiceResult> results = ServiceResult.parseList(rowList, patientTemplate);
        resultList.setResults(results);

        resultList.setPatientId(patientTemplate.getPatient().getPatientId());
        resultList.setActorId(patientTemplate.getActor().getPersonId());
        resultList.setActorType(patientTemplate.getActorTypeAsString());
        // TODO: at this moment is is always set to 0 - it should not be in ServiceResultList
        resultList.setServiceId(patientTemplate.getServiceId());

        resultList.setStartDate(dateRange.getStart());
        resultList.setEndDate(dateRange.getEnd());

        // TODO: most probably it does point to incorrect value - it should not be in ServiceResultList
        resultList.setServiceName(patientTemplate.getTemplate().getName());

        return resultList;
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

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
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

    private static Date adjustExecutionDateToBestContactTime(ServiceResult serviceResult, PatientTemplate patientTemplate) {
        Patient patient = Context.getPatientService().getPatient(serviceResult.getPatientId());
        String patientBestContactTime = BestContactTimeHelper.getBestContactTime(patient, patientTemplate.getActorType());
        Date executionDateWithProperBestContactTime = getDateWithUpdatedTime(serviceResult.getExecutionDate(),
                patientBestContactTime, MessagesConstants.HOURS_MINUTES_SEPARATOR);

        return ServiceResult.adjustTimezoneIfFuturePlannedEvent(executionDateWithProperBestContactTime,
                serviceResult.getServiceStatus());
    }

    private static PatientTemplate getRelatedPatientTemplate(ServiceResult serviceResult, Template template) {
        PatientTemplateService patientTemplateService = Context.getService(PatientTemplateService.class);
        PatientTemplateCriteria patientTemplateCriteria = PatientTemplateCriteria.forPatientAndActorAndTemplate(
                serviceResult.getPatientId(), serviceResult.getActorId(), template.getId());
        PatientTemplate patientTemplate = patientTemplateService.findOneByCriteria(patientTemplateCriteria);

        return patientTemplate != null && !patientTemplate.isDeactivated() ? patientTemplate : null;
    }

    private static Date getDateWithUpdatedTime(Date date, String time, String timeSeparator) {
        String[] splitTimeBySeparator = time.split(timeSeparator);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(splitTimeBySeparator[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(splitTimeBySeparator[1]));

        return calendar.getTime();
    }

    private static String getTemplateFieldValue(PatientTemplate patientTemplate, String templateFieldName) {
        String fieldValue = null;
        for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
            if (StringUtils.equals(templateFieldValue.getTemplateField().getName(), templateFieldName)) {
                fieldValue = templateFieldValue.getValue();
                break;
            }
        }
        return fieldValue;
    }
}
