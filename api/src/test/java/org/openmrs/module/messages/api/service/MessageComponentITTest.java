/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.builder.ActorResponseBuilder;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

// ToDo: Specify Exception
@SuppressWarnings({ "PMD.SignatureDeclareThrowsException" })
public class MessageComponentITTest extends BaseModuleContextSensitiveTest {

    private static final String XML_DATA_SET_PATH = "datasets/";

    private static final String XML_CONCEPTS_DATA_SET = "ConceptDataSet.xml";

    private static final String XML_MSG_DATA_SET = "MessageDataSet.xml";

    private static final String QUESTION_UUID = "16f1ce79-ef8a-47ca-bc40-fee648a835b4";

    private static final String RESPONSE_UUID = "9b251fd0-b900-4b11-9b77-b5174a0368b8";

    private static final String SCHEDULE_UUID = "b3de6d76-3e31-41cf-955d-ad14b9db07ff";

    // Java 1.6 Date usage
    private static final Date TIMESTAMP = new Date(2019, Calendar.NOVEMBER, 21);

    private Concept question;

    private Concept response;

    private ScheduledService scheduledService;

    @Autowired
    @Qualifier("messages.msgComponent")
    private MessageComponent msgComponent;

    @Autowired
    @Qualifier("messages.MessagingDao")
    private MessagingDao messagingDao;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + XML_CONCEPTS_DATA_SET);
        executeDataSet(XML_DATA_SET_PATH + XML_MSG_DATA_SET);
        question = Context.getConceptService().getConceptByUuid(QUESTION_UUID);
        response = Context.getConceptService().getConceptByUuid(RESPONSE_UUID);
        scheduledService = messagingDao.getByUuid(SCHEDULE_UUID);
    }

    @Test
    // ToDo: Remove @Ignore after implementing https://sd-cfl.atlassian.net/browse/CFLM-190
    @Ignore
    public void shouldReturnRegisteredActorResponse() {
        ActorResponseBuilder builder = new ActorResponseBuilder();
        ActorResponse expected = builder.withScheduledService(scheduledService)
            .withQuestion(question)
            .withResponse(response)
            .withTextResponse(scheduledService.getPatientTemplate().getTemplateFieldValue().getValue())
            .withAnsweredTime(TIMESTAMP)
            .build();

        ActorResponse actual = msgComponent.registerResponse(scheduledService.getId(),
            question.getId(),
            response.getId(),
            scheduledService.getPatientTemplate().getTemplateFieldValue().getValue(),
            TIMESTAMP);

        assertThat(actual, not(nullValue()));
        assertThat(actual.getId(), not(nullValue()));
        Assert.assertThat(messagingDao.getByUuid(actual.getUuid()), not(nullValue()));
        assertThat(actual.getTextResponse(), is(expected.getTextResponse()));
        assertThat(actual.getAnsweredTime(), is(expected.getAnsweredTime()));

        assertThat(actual.getQuestion().getConceptId(),
            is(expected.getQuestion().getConceptId()));
        assertThat(actual.getQuestion().getPreferredName(Locale.ENGLISH),
            is(expected.getQuestion().getPreferredName(Locale.ENGLISH)));

        assertThat(actual.getResponse().getConceptId(),
            is(expected.getResponse().getConceptId()));
        assertThat(actual.getResponse().getPreferredName(Locale.ENGLISH),
            is(expected.getResponse().getPreferredName(Locale.ENGLISH)));

        assertThat(actual.getScheduledService().getId(),
            is(expected.getScheduledService().getId()));
        assertThat(actual.getScheduledService().getLastServiceExecution(),
            is(expected.getScheduledService().getLastServiceExecution()));
        assertThat(actual.getScheduledService().getStatus(),
            is(expected.getScheduledService().getStatus()));
    }

    @Test(expected = Exception.class)
    // ToDo: Remove @Ignore after implementing https://sd-cfl.atlassian.net/browse/CFLM-190
    // ToDo: Specify Exception
    @Ignore
    public void shouldThrowExceptionWhenScheduledServiceIsNotSaved() throws Exception {
        ActorResponse actual = msgComponent.registerResponse(-1,
            question.getId(),
            response.getId(),
            scheduledService.getPatientTemplate().getTemplateFieldValue().getValue(),
            TIMESTAMP);
    }

    @Test(expected = Exception.class)
    // ToDo: Remove @Ignore after implementing https://sd-cfl.atlassian.net/browse/CFLM-190
    // ToDo: Specify Exception
    @Ignore
    public void shouldThrowExceptionWhenQuestionIsNotSaved() throws Exception {
        ActorResponse actual = msgComponent.registerResponse(scheduledService.getId(),
            -1,
            response.getId(),
            scheduledService.getPatientTemplate().getTemplateFieldValue().getValue(),
            TIMESTAMP);
    }

    @Test(expected = Exception.class)
    // ToDo: Remove @Ignore after implementing https://sd-cfl.atlassian.net/browse/CFLM-190
    // ToDo: Specify Exception
    @Ignore
    public void shouldThrowExceptionWhenResponseIsNotSaved() throws Exception {
        ActorResponse actual = msgComponent.registerResponse(scheduledService.getId(),
            question.getId(),
            -1,
            scheduledService.getPatientTemplate().getTemplateFieldValue().getValue(),
            TIMESTAMP);
    }
}
