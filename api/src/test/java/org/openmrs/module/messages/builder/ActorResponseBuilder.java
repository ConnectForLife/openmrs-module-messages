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
import org.openmrs.module.messages.api.model.ScheduledService;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings({ "PMD.ConstructorCallsOverridableMethod" })
public class ActorResponseBuilder extends AbstractBuilder<ActorResponse> {

    private static final int YEAR = 2019;
    private static final int DATE = 20;

    private Integer id;
    private ScheduledService scheduledService;
    private Concept question;
    private Concept response;
    private String textResponse;
    private Date answeredTime;

    public ActorResponseBuilder() {
        this.id = 1;
        this.scheduledService = new ScheduledServiceBuilder().build();
        this.question = new ConceptBuilder().build();
        this.response = new ConceptBuilder().build();
        this.textResponse = "AAAAAAAAAAAAAA";
        // Java 1.6 Date usage
        this.answeredTime = new Date(YEAR, Calendar.NOVEMBER, DATE);
    }

    @Override
    public ActorResponse build() {
        ActorResponse actor = new ActorResponse();
        actor.setId(id);
        actor.setScheduledService(scheduledService);
        actor.setQuestion(question);
        actor.setResponse(response);
        actor.setTextResponse(textResponse);
        actor.setAnsweredTime(answeredTime);
        return actor;
    }

    public ActorResponseBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ActorResponseBuilder withScheduledService(ScheduledService scheduledService) {
        this.scheduledService = scheduledService;
        return this;
    }

    public ActorResponseBuilder withQuestion(Concept question) {
        this.question = question;
        return this;
    }

    public ActorResponseBuilder withResponse(Concept response) {
        this.response = response;
        return this;
    }

    public ActorResponseBuilder withTextResponse(String textResponse) {
        this.textResponse = textResponse;
        return this;
    }

    public ActorResponseBuilder withAnsweredTime(Date answeredTime) {
        this.answeredTime = answeredTime;
        return this;
    }
}
