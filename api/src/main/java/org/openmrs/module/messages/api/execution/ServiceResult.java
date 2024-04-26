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

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Person;
import org.openmrs.module.messages.api.dto.DTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.DateUtil;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/** Represents a single execution for a service/message. */
public class ServiceResult implements Serializable, DTO {
  public static final String EXEC_DATE_ALIAS = "EXECUTION_DATE";
  public static final String MSG_ID_ALIAS = "MESSAGE_ID";
  public static final String CHANNEL_NAME_ALIAS = "CHANNEL_ID";
  public static final String STATUS_COL_ALIAS = "STATUS_ID";
  public static final String PATIENT_ID_ALIAS = "PATIENT_ID";
  public static final String ACTOR_ID_ALIAS = "ACTOR_ID";
  public static final String BEST_CONTACT_TIME_ALIAS = "BEST_CONTACT_TIME";
  public static final int MIN_COL_NUM = 3;
  private static final long serialVersionUID = -6530545764742463034L;
  private ZonedDateTime executionDate;
  private transient Object messageId;
  private String channelType;
  private Integer patientId;
  private Integer actorId;
  private ServiceStatus serviceStatus = ServiceStatus.FUTURE;
  private transient Map<String, Object> additionalParams = new HashMap<>();
  private String bestContactTime;
  private Integer patientTemplateId;

  public ServiceResult() {}

  public ServiceResult(
      ZonedDateTime executionDate,
      Object messageId,
      String channelType,
      Integer patientId,
      Integer actorId,
      ServiceStatus serviceStatus,
      Map<String, Object> additionalParams,
      String bestContactTime) {
    if (executionDate == null) {
      throw new IllegalArgumentException("Execution date is mandatory");
    }
    if (messageId == null) {
      throw new IllegalArgumentException("Message ID (external execution id) is required");
    }

    this.executionDate = executionDate;
    this.messageId = messageId;
    this.channelType = channelType;
    this.patientId = patientId;
    this.actorId = actorId;
    this.serviceStatus = serviceStatus;
    this.additionalParams = additionalParams == null ? new HashMap<>() : additionalParams;
    this.bestContactTime = bestContactTime;
  }

  @SuppressWarnings("PMD.CyclomaticComplexity")
  public static ServiceResult parse(Map<String, Object> row) {
    if (row.size() < MIN_COL_NUM) {
      throw new IllegalStateException("Invalid number of columns in result row: " + row.size());
    }

    ZonedDateTime date = null;
    Object msgId = null;
    String channelType = null;
    Integer patientId = null;
    Integer actorId = null;
    ServiceStatus status = ServiceStatus.FUTURE;
    Map<String, Object> params = new HashMap<>();
    String bestContactTime = null;

    for (Map.Entry<String, Object> entry : row.entrySet()) {
      // Skip null values
      if (entry.getValue() == null) {
        continue;
      }

      if (EXEC_DATE_ALIAS.equals(entry.getKey())) {
        date = DateUtil.convertOpenMRSDatabaseDate((Date) entry.getValue());
      } else if (MSG_ID_ALIAS.equals(entry.getKey())) {
        msgId = entry.getValue();
      } else if (CHANNEL_NAME_ALIAS.equals(entry.getKey())) {
        channelType = String.valueOf(entry.getValue());
      } else if (STATUS_COL_ALIAS.equals(entry.getKey())) {
        status = parseStatus((String) entry.getValue());
      } else if (PATIENT_ID_ALIAS.equals(entry.getKey())) {
        patientId = Integer.valueOf(entry.getValue().toString());
      } else if (ACTOR_ID_ALIAS.equals(entry.getKey())) {
        actorId = Integer.valueOf(entry.getValue().toString());
      } else if (BEST_CONTACT_TIME_ALIAS.equals(entry.getKey())) {
        bestContactTime = String.valueOf(entry.getValue());
      } else {
        params.put(entry.getKey(), entry.getValue());
      }
    }
    return new ServiceResult(
        date, msgId, channelType, patientId, actorId, status, params, bestContactTime);
  }

  public static List<ServiceResult> parseList(
      List<Map<String, Object>> list, PatientTemplate patientTemplate) {
    Map<String, ServiceResult> resultServices = new LinkedHashMap<String, ServiceResult>();

    for (Map<String, Object> row : list) {
      final ServiceResult result = ServiceResult.parse(row);

      setFieldsFromPatientTemplate(result, patientTemplate);

      result.setExecutionDate(
          adjustLocalTimeIfFuturePlannedEvent(
              patientTemplate.getActor(), result.getExecutionDate(), result.getServiceStatus()));

      final String key =
          DateUtil.formatToServerSideDateTime(result.getExecutionDate()) + result.getChannelType();

      if (resultServices.containsKey(key)) {
        if (result.getServiceStatus() != null
            && !(ServiceStatus.FUTURE == result.getServiceStatus())) {
          resultServices.put(key, result);
        }
      } else {
        resultServices.put(key, result);
      }
    }

    return new ArrayList<>(resultServices.values());
  }

  /**
   * If the {@code status} represents planned service execution, then a {@code date} is adjusted in
   * following way: the Local Time is retained and the Time Zone is changed to the default user time
   * zone. Otherwise, the {@code date} is returned as-is.
   *
   * @param actor the actor to adjust the local time for
   * @param date the date to adjust, not null
   * @param status the status
   * @return the adjusted date, never null
   * @see DateUtil#getPersonTimeZone(Person)
   */
  private static ZonedDateTime adjustLocalTimeIfFuturePlannedEvent(
      Person actor, ZonedDateTime date, ServiceStatus status) {
    requireNonNull(date);
    return status == null || status == ServiceStatus.FUTURE
        ? date.withZoneSameLocal(DateUtil.getPersonTimeZone(actor))
        : date;
  }

  /**
   * Sets fields from {@code patientTemplate} in case they were not provided by the result of
   * service query.
   *
   * @param serviceResult the service result to update, not null
   * @param patientTemplate the template to read values from, not null
   */
  private static void setFieldsFromPatientTemplate(
      final ServiceResult serviceResult, final PatientTemplate patientTemplate) {
    serviceResult.patientTemplateId = patientTemplate.getId();
    serviceResult.actorId =
        serviceResult.actorId == null ? patientTemplate.getActor().getId() : serviceResult.actorId;
    serviceResult.patientId =
        serviceResult.patientId == null
            ? patientTemplate.getPatient().getId()
            : serviceResult.patientId;
  }

  private static ServiceStatus parseStatus(String statusString) {
    if (StringUtils.isNotBlank(statusString)) {
      return ServiceStatus.valueOf(statusString);
    } else {
      return ServiceStatus.FUTURE;
    }
  }

  @Override
  public Integer getId() {
    // This DTO has no ID
    // Skipped throwing exception because of workaround for issues with JSON serialization in OMRS
    // 2.4 and later
    return null;
  }

  public ZonedDateTime getExecutionDate() {
    return executionDate;
  }

  public void setExecutionDate(ZonedDateTime executionDate) {
    this.executionDate = executionDate;
  }

  public Object getMessageId() {
    return messageId;
  }

  public void setMessageId(Object messageId) {
    this.messageId = messageId;
  }

  public String getChannelType() {
    return channelType;
  }

  public void setChannelType(String channelType) {
    this.channelType = channelType;
  }

  public ServiceStatus getServiceStatus() {
    return serviceStatus;
  }

  public void setServiceStatus(ServiceStatus serviceStatus) {
    this.serviceStatus = serviceStatus;
  }

  public Map<String, Object> getAdditionalParams() {
    return additionalParams;
  }

  public void setAdditionalParams(Map<String, Object> additionalParams) {
    this.additionalParams = additionalParams;
  }

  public Integer getPatientTemplateId() {
    return patientTemplateId;
  }

  public void setPatientTemplateId(Integer patientTemplateId) {
    this.patientTemplateId = patientTemplateId;
  }

  public Integer getPatientId() {
    return patientId;
  }

  public void setPatientId(Integer patientId) {
    this.patientId = patientId;
  }

  public Integer getActorId() {
    return actorId == null ? getPatientId() : actorId;
  }

  public void setActorId(Integer actorId) {
    this.actorId = actorId;
  }

  public String getBestContactTime() {
    return bestContactTime;
  }

  public void setBestContactTime(String bestContactTime) {
    this.bestContactTime = bestContactTime;
  }
}
