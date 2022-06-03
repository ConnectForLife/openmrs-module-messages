/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event.listener;

import org.apache.activemq.command.ActiveMQMapMessage;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Relationship;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.api.event.listener.subscribable.RemovingRelationshipListener;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.DatasetConstants;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;

public class RemovingRelationshipListenerTest extends ContextSensitiveWithActivatorTest {

    private static final String GENERAL_VOIDING_REASON = "General voiding reason";
    private static final String DATASET_NAME = "MessageDataSet.xml";
    private static final int INITIAL_TEMPLATE_COUNT = 2;
    private static final int EXPECTED_TEMPLATE_COUNT = 1;
    private static final int RELATIONSHIP_ID = 997;

    private Relationship relationship;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    @Autowired
    @Qualifier("messages.removingRelationshipListener")
    private RemovingRelationshipListener listener;

    @Before
    public void setUp() throws Exception {
        executeDataSet(DatasetConstants.XML_DATA_SET_PATH + DATASET_NAME);
        relationship = personService.getRelationship(RELATIONSHIP_ID);
    }

    @Test
    public void shouldVoidPatientTemplateAfterVoidingRelationship() throws JMSException {
        List<PatientTemplate> templateBefore = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forActorId(DatasetConstants.DEFAULT_PERSON_ID));
        personService.voidRelationship(relationship, GENERAL_VOIDING_REASON);
        mockFireEvent(buildMessage(relationship));
        List<PatientTemplate> templateAfter = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forActorId(DatasetConstants.DEFAULT_PERSON_ID));
        Assert.assertThat(templateBefore.size(), CoreMatchers.is(INITIAL_TEMPLATE_COUNT));
        Assert.assertThat(templateAfter.size(), CoreMatchers.is(EXPECTED_TEMPLATE_COUNT));
    }

    private void mockFireEvent(Message message) {
        listener.performAction(message);
    }

    private Message buildMessage(Relationship relationship) throws JMSException {
        ActiveMQMapMessage message = new ActiveMQMapMessage();
        message.setString(Constant.UUID_KEY, relationship.getUuid());
        return message;
    }
}
