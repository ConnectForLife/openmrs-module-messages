/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Concept;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.ArrayList;
import java.util.List;

public class ScheduledServiceBuilder extends AbstractBuilder<ScheduledService> {
    // Please, consider creating ScheduledServiceGroup and PatientTemplate builders if needed.
    private Integer id;
    private ScheduledServiceGroup group;
    private Concept service;
    private PatientTemplate template;
    private Concept channelType;
    private ServiceStatus status;
    private String serviceExec;
    private List<DeliveryAttempt> deliveryAttempts;
    private List<ScheduledServiceParameter> scheduledServiceParameters;
    private List<ActorResponse> actorResponses;

    public ScheduledServiceBuilder() {
        this.id = getInstanceNumber();
        this.group = new ScheduledServiceGroup();
        this.service = new ConceptBuilder().build();
        this.template = new PatientTemplate();
        this.channelType = new ConceptBuilder().build();
        this.status = ServiceStatus.PENDING;
        this.serviceExec = "test";
        this.deliveryAttempts = new ArrayList<>();
        this.scheduledServiceParameters = new ArrayList<>();
        this.actorResponses = new ArrayList<>();
    }

    @Override
    public ScheduledService build() {
        ScheduledService scheduled = new ScheduledService();
        scheduled.setId(id);
        scheduled.setService(service);
        scheduled.setChannelType(channelType);
        scheduled.setStatus(status);
        scheduled.setLastServiceExecution(serviceExec);
        scheduled.setDeliveryAttempts(deliveryAttempts);
        scheduled.setScheduledServiceParameters(scheduledServiceParameters);
        scheduled.setActorResponses(actorResponses);
        return scheduled;
    }

    @Override
    public ScheduledService buildAsNew() {
        return withId(null).build();
    }

    public ScheduledServiceBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ScheduledServiceBuilder withGroup(ScheduledServiceGroup group) {
        this.group = group;
        return this;
    }

    public ScheduledServiceBuilder withService(Concept service) {
        this.service = service;
        return this;
    }

    public ScheduledServiceBuilder withTemplate(PatientTemplate template) {
        this.template = template;
        return this;
    }

    public ScheduledServiceBuilder withChannelType(Concept channelType) {
        this.channelType = channelType;
        return this;
    }

    public ScheduledServiceBuilder withStatus(ServiceStatus status) {
        this.status = status;
        return this;
    }

    public ScheduledServiceBuilder withServiceExec(String serviceExec) {
        this.serviceExec = serviceExec;
        return this;
    }

    public ScheduledServiceBuilder withDeliveryAttempts(List<DeliveryAttempt> deliveryAttempts) {
        this.deliveryAttempts = deliveryAttempts;
        return this;
    }

    public ScheduledServiceBuilder withScheduledServiceParameters(
            List<ScheduledServiceParameter> scheduledServiceParameters) {
        this.scheduledServiceParameters = scheduledServiceParameters;
        return this;
    }

    public ScheduledServiceBuilder withActorResponses(List<ActorResponse> actorResponses) {
        this.actorResponses = actorResponses;
        return this;
    }
}
