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

  private static final long serialVersionUID = 6075952817494895177L;
  private static final Log LOGGER = LogFactory.getLog(ServiceResultList.class);
  private static final int CLEARING_CACHE_STEP = 1000;
  private static final String BEST_CONTACT_TIME_ALIAS = "bestContactTime";

  private String channelType;
  private Integer patientId;
  private Integer actorId;
  private String actorType;
  private Integer serviceId;
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
    result.serviceId = other.getServiceId();
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
    List<ServiceResultList> serviceResultsLists = new ArrayList<>();
    for (int i = 0; i < rowList.size(); i++) {
      ServiceResult serviceResult = ServiceResult.parse(rowList.get(i));

      String bestContactTime = getBestContactTimeFromQueryResult(serviceResult);
      setBestContactTimeFromQueryResultIfExists(serviceResult, bestContactTime);

      ServiceResultListBuilder serviceResultListBuilder = new ServiceResultListBuilder();

      PatientTemplate patientTemplate = getRelatedPatientTemplate(serviceResult, template);
      if (patientTemplate != null) {
        setChannelTypeFromPatientTemplate(serviceResult, patientTemplate);
        setBestContactTimeFromPatientTemplate(bestContactTime, serviceResult, patientTemplate);
        serviceResult.setPatientTemplateId(patientTemplate.getId());

        serviceResultListBuilder.withActorType(patientTemplate.getActorTypeAsString());
        serviceResultListBuilder.withServiceId(patientTemplate.getServiceId());
      } else {
        LOGGER.trace(
            String.format(
                "Patient template for patient id: %d and service type: %s "
                    + "does not exist or is deactivated",
                serviceResult.getPatientId(), template.getName()));
      }

      serviceResultListBuilder
          .withPatientId(serviceResult.getPatientId())
          .withActorId(serviceResult.getActorId())
          .withChannelType(serviceResult.getChannelType())
          .withServiceName(template.getName())
          .withStartDate(dateTimeRange.getStart())
          .withEndDate(dateTimeRange.getEnd())
          .withResults(Collections.singletonList(serviceResult));

      serviceResultsLists.add(serviceResultListBuilder.build());

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
    // TODO: at this moment is is always set to 0 - it should not be in ServiceResultList
    resultList.setServiceId(patientTemplate.getServiceId());
    resultList.setStartDate(dateRange.getStart());
    resultList.setEndDate(dateRange.getEnd());
    // TODO: most probably it does point to incorrect value - it should not be in ServiceResultList
    resultList.setServiceName(patientTemplate.getTemplate().getName());

    return resultList;
  }

  /**
   * Adjusts execution date of {@code serviceResult} in following way: the Local Date part is
   * retained, the Local Time part is taken from {@code patientTemplate} configuration and the
   * default user time zone is used.
   *
   * @param serviceResult the service result to adjust its execution date, not null
   * @param patientTemplate the source of local time configuration, not null
   * @return the adjusted date, never null
   */
  private static ZonedDateTime adjustExecutionDateToBestContactTime(
      ServiceResult serviceResult, PatientTemplate patientTemplate) {
    final Person person = Context.getPersonService().getPerson(serviceResult.getActorId());
    final String patientBestContactTime =
        BestContactTimeHelper.getBestContactTime(
            person,
            patientTemplate.getActorType() != null
                ? patientTemplate.getActorType().getRelationshipType()
                : null);

    return getDateWithLocalTimeAndDefaultUserTimeZone(
        serviceResult.getExecutionDate(), patientBestContactTime);
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
      ZonedDateTime date, String localTime) {
    final String[] splitTimeBySeparator =
        localTime.split(MessagesConstants.HOURS_MINUTES_SEPARATOR);
    final int hourOfDay = Integer.parseInt(splitTimeBySeparator[0]);
    final int minute = Integer.parseInt(splitTimeBySeparator[1]);

    return ZonedDateTime.of(
        date.toLocalDate(), LocalTime.of(hourOfDay, minute), DateUtil.getDefaultUserTimeZone());
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

  private static String getBestContactTimeFromQueryResult(ServiceResult serviceResult) {
    Object bestContactTimeParam = serviceResult.getAdditionalParams().get(BEST_CONTACT_TIME_ALIAS);
    if (bestContactTimeParam != null) {
      return bestContactTimeParam.toString();
    }
    return null;
  }

  private static void setBestContactTimeFromQueryResultIfExists(
      ServiceResult serviceResult, String bestContactTime) {
    if (bestContactTime != null) {
      serviceResult.setExecutionDate(
          getDateWithLocalTimeAndDefaultUserTimeZone(
              serviceResult.getExecutionDate(), bestContactTime));
    }
  }

  private static void setChannelTypeFromPatientTemplate(
      ServiceResult serviceResult, PatientTemplate patientTemplate) {
    if (serviceResult.getChannelType() == null) {
      serviceResult.setChannelType(getChannelType(patientTemplate));
    }
  }

  private static void setBestContactTimeFromPatientTemplate(
      String bestContactTime, ServiceResult serviceResult, PatientTemplate patientTemplate) {
    if (bestContactTime == null) {
      serviceResult.setExecutionDate(
          adjustExecutionDateToBestContactTime(serviceResult, patientTemplate));
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

  public Integer getServiceId() {
    return serviceId;
  }

  public void setServiceId(Integer serviceId) {
    this.serviceId = serviceId;
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
