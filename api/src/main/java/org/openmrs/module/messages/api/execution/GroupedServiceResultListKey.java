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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.ZonedDateTime;

public class GroupedServiceResultListKey {

    private final String channelType;
    private final Integer actorId;
    private final Integer patientId;
    private final String actorType;
    private final ZonedDateTime date;

    public GroupedServiceResultListKey(final String singleChannelType, final ServiceResult serviceResult,
                                       final String actorType) {
        this.channelType = singleChannelType;
        this.actorId = serviceResult.getActorId();
        this.patientId = serviceResult.getPatientId();
        this.actorType = actorType;
        this.date = serviceResult.getExecutionDate();
    }

    public String getChannelType() {
        return channelType;
    }

    public Integer getActorId() {
        return actorId;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public String getActorType() {
        return actorType;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "GroupedServiceResultListKey {channelType='" + channelType + "', actorId=" + actorId + ", actorType='" +
                actorType + "', date=" + date + '}';
    }
}
