/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Person;
import org.openmrs.module.messages.api.util.OpenmrsObjectUtil;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScheduledExecutionContext implements Serializable {

  private static final long serialVersionUID = 7130097358348208474L;

  private List<Integer> serviceIdsToExecute;
  private String channelType;
  private Instant executionDate;
  private int actorId;
  private int patientId;
  private String actorType;
  private int groupId;
  private boolean rescheduled;
  private Map<String, String> channelConfiguration;

  public ScheduledExecutionContext() {
    this.rescheduled = false;
    this.channelConfiguration = new HashMap<>();
  }

  /**
   * @param scheduledServices the scheduledServices
   * @param channelType       the channelType
   * @param executionDate     the executionDate
   * @param actor             the actor
   * @param patientId         the patientId
   * @param actorType         the actorType
   * @param groupId           the groupId
   * @deprecated Use constructor with new Java Time API instead Date.
   */
  @Deprecated
  public ScheduledExecutionContext(List<ScheduledService> scheduledServices, String channelType, Date executionDate,
                                   Person actor, Integer patientId, String actorType, int groupId) {
    this(scheduledServices, channelType, executionDate.toInstant(), actor, patientId, actorType, groupId);
  }

  public ScheduledExecutionContext(List<ScheduledService> scheduledServices, String channelType, Instant executionDate,
                                   Person actor, Integer patientId, String actorType, int groupId) {
    this();
    this.serviceIdsToExecute = OpenmrsObjectUtil.getIds(scheduledServices);
    this.channelType = channelType;
    this.executionDate = executionDate;
    this.actorId = actor.getId();
    this.patientId = patientId;
    this.actorType = actorType;
    this.groupId = groupId;
  }

  public List<Integer> getServiceIdsToExecute() {
    return serviceIdsToExecute;
  }

  public void setServiceIdsToExecute(List<Integer> serviceIdsToExecute) {
    this.serviceIdsToExecute = serviceIdsToExecute;
  }

  public String getChannelType() {
    return channelType;
  }

  public void setChannelType(String channelType) {
    this.channelType = channelType;
  }

  public Instant getExecutionDate() {
    return executionDate;
  }

  public void setExecutionDate(Instant executionDate) {
    this.executionDate = executionDate;
  }

  public int getActorId() {
    return actorId;
  }

  public void setActorId(int actorId) {
    this.actorId = actorId;
  }

  public int getPatientId() {
    return this.patientId;
  }

  public void setPatientId(int patientId) {
    this.patientId = patientId;
  }

  public String getActorType() {
    return actorType;
  }

  public void setActorType(String actorType) {
    this.actorType = actorType;
  }

  public int getGroupId() {
    return groupId;
  }

  public void setGroupId(int groupId) {
    this.groupId = groupId;
  }

  public boolean isRescheduled() {
    return rescheduled;
  }

  public void setRescheduled(boolean rescheduled) {
    this.rescheduled = rescheduled;
  }

  public Map<String, String> getChannelConfiguration() {
    return channelConfiguration;
  }

  public void setChannelConfiguration(Map<String, String> channelConfiguration) {
    this.channelConfiguration = channelConfiguration;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    return EqualsBuilder.reflectionEquals(this, o);
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }
}
