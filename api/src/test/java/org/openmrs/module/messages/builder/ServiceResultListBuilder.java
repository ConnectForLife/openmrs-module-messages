/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Person;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.Range;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class ServiceResultListBuilder extends AbstractBuilder<ServiceResultList> {

    private Integer actorId;
    private List<ServiceResult> results;

    public ServiceResultListBuilder() {
        super();
    }

    @Override
    public ServiceResultList build() {
        Person actor = new Person();
        actor.setId(actorId);
        ServiceResultList result = ServiceResultList.createList(
                new ArrayList<>(),
                new PatientTemplateBuilder().withActor(actor).build(),
                new Range<>(null, null));
        result.setResults(results);
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
}
