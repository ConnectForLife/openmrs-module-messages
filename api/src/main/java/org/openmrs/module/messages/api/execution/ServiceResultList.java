/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.builder.ServiceResultListBuilder;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.MessagingGroupService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.util.BestContactTimeHelper;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.validator.BestContactTimeValidatorUtils;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/** Represents a list of execution for a service/message. */
public class ServiceResultList implements Serializable {

  private static final long serialVersionUID = -6883448088210184888L;
  private static final Log LOGGER = LogFactory.getLog(ServiceResultList.class);
  private static final int CLEARING_CACHE_STEP = 1000;

  private String channelType;
  private Integer patientId;
  private Integer actorId;
  private String actorType;
  private String serviceName;
  private ZonedDateTime startDate;
  private ZonedDateTime endDate;
  private List<ServiceResult> results;

  public static ServiceResultList withEmptyResults(ServiceResultList other) {
    ServiceResultList result = new ServiceResultList();

    result.channelType = other.getChannelType();
    result.patientId = other.getPatientId();
    result.actorId = other.getActorId();
    result.actorType = other.getActorType();
    result.serviceName = other.getServiceName();
    result.startDate = other.getStartDate();
    result.endDate = other.getEndDate();
    result.results = new ArrayList<>();

    return result;
  }

  public static List<ServiceResultList> createList(
      @NotNull List<Map<String, Object>> rowList,
      Template template,
      Range<ZonedDateTime> dateTimeRange) {
    List<ServiceResultList> serviceResultsLists = new ArrayList<>(rowList.size());
    ServiceResultListBuilder serviceResultListBuilder = new ServiceResultListBuilder();

    for (int i = 0; i < rowList.size(); i++) {
      ServiceResult serviceResult = ServiceResult.parse(rowList.get(i));
      setExecutionDateWithBestContactTimeFromQueryResult(serviceResult);

      serviceResultListBuilder.withActorType(MessagesConstants.PATIENT_DEFAULT_ACTOR_TYPE);

      PatientTemplate patientTemplate = getRelatedPatientTemplate(serviceResult, template);
      if (patientTemplate != null) {
        Person person = patientTemplate.getActor();
        String patientBestContactTime =
            BestContactTimeHelper.getBestContactTime(
                person,
                patientTemplate.getActorType() != null
                    ? patientTemplate.getActorType().getRelationshipType()
                    : null);
        if (!BestContactTimeValidatorUtils.isValidTime(patientBestContactTime)) {
          LOGGER.warn(
              String.format(
                  "Best contact time for patient with id: %d is invalid. Scheduling events for this patient will be skipped",
                  person.getPersonId()));
          continue;
        }

        setChannelTypeFromPatientTemplate(serviceResult, patientTemplate);
        setExecutionDateWithBestContactTimeFromPatientTemplate(
            serviceResult, patientBestContactTime);
        serviceResult.setPatientTemplateId(patientTemplate.getId());

        serviceResultListBuilder.withActorType(patientTemplate.getActorTypeAsString());
      } else {
        LOGGER.trace(
            String.format(
                "Patient template for patient id: %d and service type: %s "
                    + "does not exist or is deactivated",
                serviceResult.getPatientId(), template.getName()));
      }

      // Collect result only if execution date fits the dateTimeRange
      if (dateTimeRange.contains(serviceResult.getExecutionDate(), ZonedDateTime::compareTo)) {
        serviceResultListBuilder
            .withPatientId(serviceResult.getPatientId())
            .withActorId(serviceResult.getActorId())
            .withChannelType(serviceResult.getChannelType())
            .withServiceName(template.getName())
            .withStartDate(dateTimeRange.getStart())
            .withEndDate(dateTimeRange.getEnd())
            .withResults(Collections.singletonList(serviceResult));

        serviceResultsLists.add(serviceResultListBuilder.build());
      }

      // We clear session cache memory periodically because of performance issues
      // Each ServiceResultList object created is stored in memory and in case of large number
      // of elements in the rowList at some point there is no memory for storing new objects
      if (i % CLEARING_CACHE_STEP == 0) {
        getGroupService().flushAndClearSessionCache();
      }
    }

    return serviceResultsLists;
  }

  public static ServiceResultList createList(
      @NotNull List<Map<String, Object>> rowList,
      PatientTemplate patientTemplate,
      @NotNull Range<ZonedDateTime> dateRange) {
    final List<ServiceResult> results = ServiceResult.parseList(rowList, patientTemplate);

    final ServiceResultList resultList = new ServiceResultList();
    resultList.setResults(results);
    resultList.setChannelType(getChannelType(patientTemplate));
    resultList.setPatientId(patientTemplate.getPatient().getPatientId());
    resultList.setActorId(patientTemplate.getActor().getPersonId());
    resultList.setActorType(patientTemplate.getActorTypeAsString());
    resultList.setStartDate(dateRange.getStart());
    resultList.setEndDate(dateRange.getEnd());
    resultList.setServiceName(patientTemplate.getTemplate().getName());

    return resultList;
  }

  private static ZonedDateTime adjustExecutionDateToBestContactTime(
      ServiceResult serviceResult, String patientBestContactTime) {

    return getDateWithLocalTimeAndDefaultUserTimeZone(
        serviceResult.getExecutionDate(), patientBestContactTime, serviceResult.getActorId());
  }

  private static PatientTemplate getRelatedPatientTemplate(
      ServiceResult serviceResult, Template template) {
    PatientTemplateService patientTemplateService =
        Context.getService(PatientTemplateService.class);
    PatientTemplateCriteria patientTemplateCriteria =
        PatientTemplateCriteria.forPatientAndActorAndTemplate(
            serviceResult.getPatientId(), serviceResult.getActorId(), template.getId());
    PatientTemplate patientTemplate =
        patientTemplateService.findOneByCriteria(patientTemplateCriteria);

    return patientTemplate != null && !patientTemplate.isDeactivated() ? patientTemplate : null;
  }

  private static ZonedDateTime getDateWithLocalTimeAndDefaultUserTimeZone(
      ZonedDateTime executionDate, String localTime, Integer personId) {
    final String[] splitTimeBySeparator =
        localTime.split(MessagesConstants.HOURS_MINUTES_SEPARATOR);
    final int hourOfDay = Integer.parseInt(splitTimeBySeparator[0]);
    final int minute = Integer.parseInt(splitTimeBySeparator[1]);
    final Person actor = Context.getPersonService().getPerson(personId);

    return ZonedDateTime.of(
        executionDate.toLocalDate(),
        LocalTime.of(hourOfDay, minute),
        DateUtil.getPersonTimeZone(actor));
  }

  private static String getChannelType(PatientTemplate patientTemplate) {
    String fieldValue = null;
    for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
      if (StringUtils.equals(
          templateFieldValue.getTemplateField().getName(),
          MessagesConstants.CHANNEL_TYPE_PARAM_NAME)) {
        fieldValue = templateFieldValue.getValue();
        break;
      }
    }
    return fieldValue;
  }

  private static MessagingGroupService getGroupService() {
    return Context.getRegisteredComponent(
        MessagesConstants.MESSAGING_GROUP_SERVICE, MessagingGroupService.class);
  }

  private static void setExecutionDateWithBestContactTimeFromQueryResult(
      ServiceResult serviceResult) {
    String bestContactTime = serviceResult.getBestContactTime();
    if (bestContactTime != null) {
      serviceResult.setExecutionDate(
          getDateWithLocalTimeAndDefaultUserTimeZone(
              serviceResult.getExecutionDate(), bestContactTime, serviceResult.getActorId()));
    }
  }

  private static void setChannelTypeFromPatientTemplate(
      ServiceResult serviceResult, PatientTemplate patientTemplate) {
    if (serviceResult.getChannelType() == null) {
      serviceResult.setChannelType(getChannelType(patientTemplate));
    }
  }

  private static void setExecutionDateWithBestContactTimeFromPatientTemplate(
      ServiceResult serviceResult, String patientBestContactTime) {
    if (serviceResult.getBestContactTime() == null) {
      serviceResult.setExecutionDate(
          adjustExecutionDateToBestContactTime(serviceResult, patientBestContactTime));
    }
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

  public ZonedDateTime getStartDate() {
    return startDate;
  }

  public void setStartDate(ZonedDateTime startDate) {
    this.startDate = startDate;
  }

  public ZonedDateTime getEndDate() {
    return endDate;
  }

  public void setEndDate(ZonedDateTime endDate) {
    this.endDate = endDate;
  }

  public List<ServiceResult> getResults() {
    return results;
  }

  public void setResults(List<ServiceResult> results) {
    this.results = results;
  }

  public ServiceResultList withResults(List<ServiceResult> results) {
    this.results = results;
    return this;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }
}
