/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.Person;
import org.openmrs.module.messages.api.util.OpenmrsObjectUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ScheduledExecutionContext implements Serializable {

    private static final long serialVersionUID = 7043667008864304408L;

    private List<Integer> serviceIdsToExecute;

    private String channelType;

    private Date executionDate;

    private int actorId;

    private int patientId;

    private String actorType;

    private int groupId;

    public ScheduledExecutionContext() {
    }

    public ScheduledExecutionContext(List<ScheduledService> scheduledServices, String channelType, Date executionDate,
                                     Person actor, Integer patientId, String actorType, int groupId) {
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

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
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
