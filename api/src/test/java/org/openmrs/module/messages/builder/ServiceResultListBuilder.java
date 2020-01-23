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
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.DateRange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ServiceResultListBuilder extends AbstractBuilder<ServiceResultList> {

    private Integer actorId;
    private Integer patientId;
    private List<ServiceResult> results;
    private String serviceName;

    public ServiceResultListBuilder() {
        super();
    }

    @Override
    public ServiceResultList build() {
        Person actor = new PersonBuilder().withId(actorId).build();
        if (patientId == null) {
            patientId = actorId;
        }
        Patient patient = new PatientBuilder().withId(patientId).build();

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withActor(actor)
                .withPatient(patient)
                .build();

        Relationship relationship;
        if (!actorId.equals(patientId)) {
            relationship = new RelationshipBuilder()
                    .withPersonA(actor)
                    .withPersonB(patient.getPerson())
                    .build();
        } else {
            relationship = null;
        }
        //Relationship should be present only if actorId and patientId are not equals
        patientTemplate.setActorType(relationship);

        ServiceResultList result = ServiceResultList.createList(
                new ArrayList<>(),
                patientTemplate,
                new DateRange(null, null));
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
            list.add(new ServiceResultBuilder().withExecutionDate(date).build());
        }
        return withServiceResults(list);
    }

    public ServiceResultListBuilder withServiceName(String serviceName) {
        this.serviceName = serviceName;
        return this;
    }
}
