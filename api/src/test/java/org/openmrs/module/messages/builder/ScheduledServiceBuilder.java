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
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

@SuppressWarnings({ "PMD.UnusedPrivateField", "PMD.ConstructorCallsOverridableMethod" })
public class ScheduledServiceBuilder extends AbstractBuilder<ScheduledService> {
    // Please, consider creating ScheduledServiceGroup and PatientTemplate builders if needed.
    private Integer id;
    private ScheduledServiceGroup group;
    private Concept service;
    private PatientTemplate template;
    private Concept channelType;
    private ServiceStatus status;
    private String serviceExec;

    public void setTemplate(PatientTemplate template) {
        this.template = template;
    }

    public ScheduledServiceBuilder() {
        this.id = 1;
        this.group = new ScheduledServiceGroup();
        this.service = new ConceptBuilder().build();
        this.template = new PatientTemplate();
        this.channelType = new ConceptBuilder().build();
        this.status = ServiceStatus.PENDING;
        this.serviceExec = "test";
    }

    @Override
    public ScheduledService build() {
        ScheduledService scheduled = new ScheduledService();
        scheduled.setId(id);
        scheduled.setService(service);
        scheduled.setChannelType(channelType);
        scheduled.setStatus(status);
        scheduled.setLastServiceExecution(serviceExec);
        return scheduled;
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
}
