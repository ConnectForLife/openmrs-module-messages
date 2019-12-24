package org.openmrs.module.messages.api.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Concept;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.builder.ActorResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ActorResponseITTest extends ContextSensitiveTest {
    
    private static final String XML_DATA_SET_PATH = "datasets/";
    
    private static final String QUESTION_UUID = "16f1ce79-ef8a-47ca-bc40-fee648a835b4";
    
    private static final String RESPONSE_UUID = "9b251fd0-b900-4b11-9b77-b5174a0368b8";
    
    private static final String SCHEDULE_UUID = "b3de6d76-3e31-41cf-955d-ad14b9db07ff";
    
    private static final Date TIMESTAMP = new Date(2019, Calendar.NOVEMBER, 21);
    
    private static final int GET_ALL_EXPECTED_LIST_SIZE = 0;
    
    private static final int UPDATED_YEAR = 2019;
    
    private static final int UPDATED_DAY = 25;
    
    private Concept question;
    
    private Concept response;
    
    private ScheduledService scheduledService;
    
    @Autowired
    @Qualifier("messages.MessagingDao")
    private MessagingDao messagingDao;
    
    @Autowired
    @Qualifier("messages.ActorResponseDao")
    private ActorResponseDao actorResponseDao;
    
    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");
        question = Context.getConceptService().getConceptByUuid(QUESTION_UUID);
        response = Context.getConceptService().getConceptByUuid(RESPONSE_UUID);
        scheduledService = messagingDao.getByUuid(SCHEDULE_UUID);
    }
    
    @Test
    public void shouldSaveAllPropertiesInDb() {
        ActorResponse expected = createDefault();
        
        ActorResponse actual = actorResponseDao.saveOrUpdate(createDefault());
        
        assertThat(actual, not(nullValue()));
        assertThat(actual.getId(), not(nullValue()));
        
        assertThat(actual, hasProperty("scheduledService", is(expected.getScheduledService())));
        assertThat(actual, hasProperty("question", is(expected.getQuestion())));
        assertThat(actual, hasProperty("response", is(expected.getResponse())));
        assertThat(actual, hasProperty("textResponse", is(expected.getTextResponse())));
        assertThat(actual, hasProperty("answeredTime", is(expected.getAnsweredTime())));
        
        assertThat(actual, hasProperty("uuid", not(nullValue())));
        assertThat(actual, hasProperty("dateCreated", not(nullValue())));
        assertThat(actual, hasProperty("creator", not(nullValue())));
        assertThat(actual, hasProperty("voided", is(false)));
    }
    
    @Test
    public void shouldDeleteActorResponse() {
        ActorResponse actorResponse = actorResponseDao.saveOrUpdate(createDefault());
        int beforeRemove = actorResponseDao.getAll(true).size();
        
        actorResponseDao.delete(actorResponse);
        
        assertNull(actorResponseDao.getByUuid(actorResponse.getUuid()));
        assertEquals(GET_ALL_EXPECTED_LIST_SIZE + 1, beforeRemove);
        assertEquals(GET_ALL_EXPECTED_LIST_SIZE, actorResponseDao.getAll(true).size());
    }
    
    @Test
    public void shouldReturnAllSavedActorResponses() {
        actorResponseDao.saveOrUpdate(createDefault());
        List<ActorResponse> actorResponses = actorResponseDao.getAll(true);
        
        assertEquals(GET_ALL_EXPECTED_LIST_SIZE + 1, actorResponses.size());
    }
    
    @Test
    public void shouldUpdateExistingActorResponse() {
        ActorResponse actor = actorResponseDao.saveOrUpdate(createDefault());
        
        ScheduledService updatedService = messagingDao.getByUuid("532f0b56-3ff9-427b-b4f3-b92796c7eea2");
        Concept concept12 = Context.getConceptService().getConceptByUuid(
                "a8f03299-ab40-49ea-91df-d034d0de009c");
        Concept concept13 = Context.getConceptService().getConceptByUuid(
                "e3d2a6d1-9518-44f4-875c-40b8a83fd7e8");
        String updatedTextResponse = "updated text";
        Date updatedTimestamp = new Date(UPDATED_YEAR, Calendar.NOVEMBER, UPDATED_DAY);
        
        actor.setScheduledService(updatedService);
        actor.setQuestion(concept12);
        actor.setResponse(concept13);
        actor.setTextResponse(updatedTextResponse);
        actor.setAnsweredTime(updatedTimestamp);
        
        ActorResponse updated = actorResponseDao.saveOrUpdate(actor);
        
        assertThat(updated, hasProperty("scheduledService", is(updatedService)));
        assertThat(updated, hasProperty("question", is(concept12)));
        assertThat(updated, hasProperty("response", is(concept13)));
        assertThat(updated, hasProperty("textResponse", is(updatedTextResponse)));
        assertThat(updated, hasProperty("answeredTime", is(updatedTimestamp)));
    }
    
    private ActorResponse createDefault() {
        ActorResponseBuilder builder = new ActorResponseBuilder();
        return builder.withScheduledService(scheduledService)
                .withQuestion(question)
                .withResponse(response)
                .withTextResponse(scheduledService.getPatientTemplate().getTemplateFieldValues().get(0).getValue())
                .withAnsweredTime(TIMESTAMP)
                .buildAsNew();
    }
}
