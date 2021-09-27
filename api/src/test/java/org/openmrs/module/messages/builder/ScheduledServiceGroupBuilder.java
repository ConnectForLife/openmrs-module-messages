/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduledServiceGroupBuilder extends AbstractBuilder<ScheduledServiceGroup> {

    private Integer id;
    private Date msgSendTime;
    private Integer patientId;
    private Integer actorId;
    private ServiceStatus status;
    private String channelType;
    private List<ScheduledService> scheduledServices = new ArrayList<>();

    public ScheduledServiceGroupBuilder() {
    }

    @Override
    public ScheduledServiceGroup build() {
        ScheduledServiceGroup result = new ScheduledServiceGroup();
        result.setId(id);
        result.setMsgSendTime(msgSendTime);
        result.setPatient(new PatientBuilder().withId(patientId).build());
        result.setActor(new PersonBuilder().withId(actorId).build());
        result.setStatus(status);
        result.setScheduledServices(scheduledServices);
        result.setChannelType(channelType);
        return result;
    }

    @Override
    public ScheduledServiceGroup buildAsNew() {
        return withId(null).build();
    }

    public ScheduledServiceGroupBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ScheduledServiceGroupBuilder withMsgSendTime(Date msgSendTime) {
        this.msgSendTime = msgSendTime;
        return this;
    }

    public ScheduledServiceGroupBuilder withPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public ScheduledServiceGroupBuilder withActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public ScheduledServiceGroupBuilder withStatus(ServiceStatus status) {
        this.status = status;
        return this;
    }

    public ScheduledServiceGroupBuilder withChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public ScheduledServiceGroupBuilder withScheduledServices(List<ScheduledService> scheduledServices) {
        this.scheduledServices = scheduledServices;
        return this;
    }
}
