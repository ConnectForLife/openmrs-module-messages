/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.module.messages.api.execution.ServiceResult;

import java.time.ZonedDateTime;
import java.util.Map;

public final class ServiceResultBuilder extends AbstractBuilder<ServiceResult> {

    private Integer patientId;
    private Integer actorId;
    private ZonedDateTime executionDate;
    private String channelType;
    private Integer patientTemplateId;
    private Map<String, Object> additionalParams;

    public ServiceResultBuilder() {
        super();
    }

    @Override
    public ServiceResult build() {
        ServiceResult result = new ServiceResult();
        result.setPatientId(patientId);
        result.setActorId(actorId);
        result.setExecutionDate(executionDate);
        result.setChannelType(channelType);
        result.setPatientTemplateId(patientTemplateId);
        result.setAdditionalParams(additionalParams);
        return result;
    }

    @Override
    public ServiceResult buildAsNew() {
        return build();
    }

    public ServiceResultBuilder withPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public ServiceResultBuilder withActorId(Integer actorId) {
        this.actorId = actorId;
        return this;
    }

    public ServiceResultBuilder withExecutionDate(ZonedDateTime executionDate) {
        this.executionDate = executionDate;
        return this;
    }

    public ServiceResultBuilder withChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }

    public ServiceResultBuilder withPatientTemplate(Integer patientTemplateId) {
        this.patientTemplateId = patientTemplateId;
        return this;
    }

    public ServiceResultBuilder withParams(Map<String, Object> additionalParams) {
        this.additionalParams = additionalParams;
        return this;
    }
}
