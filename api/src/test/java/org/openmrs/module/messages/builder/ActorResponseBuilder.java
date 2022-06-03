/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.ActorResponseType;

import java.util.Calendar;
import java.util.Date;

public class ActorResponseBuilder extends AbstractBuilder<ActorResponse> {

    private static final int DEFAULT_YEAR = 2019;
    private static final int DEFAULT_DATE = 20;

    private Integer id;
    private Person actor;
    private Patient patient;
    private String sourceId;
    private ActorResponseType sourceType;
    private Concept question;
    private String textQuestion;
    private Concept response;
    private String textResponse;
    private Date answeredTime;

    public ActorResponseBuilder() {
        this.id = getInstanceNumber();
        this.question = new ConceptBuilder().build();
        this.textQuestion = Constant.ACTOR_RESPONSE_TEXT_QUESTION;
        this.response = new ConceptBuilder().build();
        this.textResponse = Constant.ACTOR_RESPONSE_TEXT_RESPONSE;
        this.answeredTime = new Date(DEFAULT_YEAR, Calendar.NOVEMBER, DEFAULT_DATE);
        this.actor = new PersonBuilder().build();
        this.patient = new PatientBuilder().build();
        this.sourceId = new ScheduledServiceBuilder().build().getId().toString();
        this.sourceType = new ActorResponseType(MessagesConstants.DEFAULT_ACTOR_RESPONSE_TYPE);
    }

    @Override
    public ActorResponse build() {
        ActorResponse actorResponse = new ActorResponse();
        actorResponse.setId(id);
        actorResponse.setActor(actor);
        actorResponse.setPatient(patient);
        actorResponse.setSourceId(sourceId);
        actorResponse.setSourceType(sourceType);
        actorResponse.setQuestion(question);
        actorResponse.setTextQuestion(textQuestion);
        actorResponse.setResponse(response);
        actorResponse.setTextResponse(textResponse);
        actorResponse.setAnsweredTime(answeredTime);
        return actorResponse;
    }

    @Override
    public ActorResponse buildAsNew() {
        return withId(null).build();
    }

    public ActorResponseBuilder withId(Integer id) {
        this.id = id;
        return this;
    }

    public ActorResponseBuilder withActor(Person actor) {
        this.actor = actor;
        return this;
    }

    public ActorResponseBuilder withPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public ActorResponseBuilder withSourceId(String sourceId) {
        this.sourceId = sourceId;
        return this;
    }

    public ActorResponseBuilder withSourceType(ActorResponseType sourceType) {
        this.sourceType = sourceType;
        return this;
    }

    public ActorResponseBuilder withQuestion(Concept question) {
        this.question = question;
        return this;
    }

    public ActorResponseBuilder withTextQuestion(String textQuestion) {
        this.textQuestion = textQuestion;
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
