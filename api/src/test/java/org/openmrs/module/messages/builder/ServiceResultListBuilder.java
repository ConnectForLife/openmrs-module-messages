/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class ServiceResultListBuilder extends AbstractBuilder<ServiceResultList> {
    private Integer actorId;
    private Integer patientId;
    private List<ServiceResult> results = Collections.emptyList();
    private String serviceName;
    private String channelType;

    public ServiceResultListBuilder() {
        super();
    }

    @Override
    public ServiceResultList build() {
        Person actor = new PersonBuilder().withId(actorId).build();
        Patient patient = new PatientBuilder().withId(patientId).build();

        TemplateField templateField = new TemplateFieldBuilder().withName(MessagesConstants.CHANNEL_TYPE_PARAM_NAME).build();

        TemplateFieldValue templateFieldValue =
                new TemplateFieldValueBuilder().withTemplateField(templateField).withValue(channelType).build();

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withActor(actor)
                .withPatient(patient)
                .withTemplateFieldValues(Collections.singletonList(templateFieldValue))
                .build();

        Relationship relationship;
        if (!actorId.equals(patientId)) {
            relationship = new RelationshipBuilder().withPersonA(actor).withPersonB(patient.getPerson()).build();
        } else {
            relationship = null;
        }
        //Relationship should be present only if actorId and patientId are not equals
        patientTemplate.setActorType(relationship);

        // Ensures the data consistency
        for (ServiceResult serviceResult : results) {
            serviceResult.setActorId(actorId);
            serviceResult.setPatientId(patientId);
            serviceResult.setChannelType(channelType);
        }

        ServiceResultList result = ServiceResultList.createList(new ArrayList<>(), patientTemplate, new Range<>(null, null));
        result.setResults(results);
        result.setServiceName(serviceName);
        return result;
    }

    @Override
    public ServiceResultList buildAsNew() {
        return withActorId(null).build();
    }

    public ServiceResultListBuilder withActorId(Integer actorId) {
        this.actorId = actorId;

        if (patientId == null) {
            patientId = actorId;
        }

        return this;
    }

    public ServiceResultListBuilder withPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public ServiceResultListBuilder withServiceResults(List<ServiceResult> results) {
        this.results = results;
        return this;
    }

    public ServiceResultListBuilder withServiceResults(int count, Date date) {
        List<ServiceResult> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            list.add(new ServiceResultBuilder()
                    .withActorId(actorId)
                    .withPatientId(patientId)
                    .withChannelType(channelType)
                    .withExecutionDate(date)
                    .build());
        }
        return withServiceResults(list);
    }

    public ServiceResultListBuilder withServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }

    public ServiceResultListBuilder withChannelType(String channelType) {
        this.channelType = channelType;
        return this;
    }
}
