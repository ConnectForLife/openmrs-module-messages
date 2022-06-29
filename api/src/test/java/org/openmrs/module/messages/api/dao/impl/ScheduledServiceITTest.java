/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao.impl;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.helper.PatientTemplateHelper;
import org.openmrs.module.messages.api.helper.ScheduledServiceGroupHelper;
import org.openmrs.module.messages.api.helper.ScheduledServiceHelper;
import org.openmrs.module.messages.api.helper.TemplateFieldHelper;
import org.openmrs.module.messages.api.helper.TemplateFieldValueHelper;
import org.openmrs.module.messages.api.helper.TemplateHelper;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.TestConstants;
import org.openmrs.module.messages.builder.DeliveryAttemptBuilder;
import org.openmrs.module.messages.builder.ScheduledServiceParameterBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class ScheduledServiceITTest extends ContextSensitiveTest {
    
    @Autowired
    private MessagingDao messagingDao;
    
    @Autowired
    private PatientTemplateDao patientTemplateDao;
    
    @Autowired
    private TemplateDao templateDao;
    
    @Autowired
    private MessagingGroupDao messagingGroupDao;
    
    private ScheduledService scheduledService;
    
    @Before
    public void setUp() {
        createTestInstance();
        createTestInstance();
        createTestInstance();
    }
    
    @Test
    public void shouldSaveAllPropertiesInDb() {
        ScheduledService savedScheduledService = messagingDao.getByUuid(scheduledService.getUuid());
        
        Assert.assertThat(savedScheduledService, hasProperty("uuid", is(scheduledService.getUuid())));
        Assert.assertThat(savedScheduledService, hasProperty("group", is(scheduledService.getGroup())));
        Assert.assertThat(savedScheduledService, hasProperty("service", is(scheduledService.getService())));
        Assert.assertThat(savedScheduledService, hasProperty("patientTemplate",
                is(scheduledService.getPatientTemplate())));
        Assert.assertThat(savedScheduledService, hasProperty("status", is(scheduledService.getStatus())));
        Assert.assertThat(savedScheduledService, hasProperty("lastServiceExecution",
                is(scheduledService.getLastServiceExecution())));
    }
    
    @Test
    public void shouldReturnAllSavedScheduledServices() {
        List<ScheduledService> scheduledServices = messagingDao.getAll(true);
        
        Assert.assertEquals(TestConstants.GET_ALL_EXPECTED_LIST_SIZE, scheduledServices.size());
    }
    
    @Test
    public void shouldDeleteScheduledService() {
        messagingDao.delete(scheduledService);
        
        Assert.assertNull(messagingDao.getByUuid(scheduledService.getUuid()));
        Assert.assertEquals(TestConstants.DELETE_EXPECTED_LIST_SIZE, messagingDao.getAll(true).size());
    }
    
    @Test
    public void shouldUpdateExistingScheduledService() {
        scheduledService.setLastServiceExecution(TestConstants.TEST_UPDATED_SERVICE_EXECUTION_ID);
        messagingDao.saveOrUpdate(scheduledService);
        
        Assert.assertThat(messagingDao.getByUuid(scheduledService.getUuid()),
                hasProperty("lastServiceExecution", is(TestConstants.TEST_UPDATED_SERVICE_EXECUTION_ID)));
    }

    @Test
    public void shouldGetTemplateName() {
        Assert.assertEquals(TemplateHelper.DUMMY_SERVICE_NAME, scheduledService.getTemplateName());
    }

    @Test
    public void shouldGetAttemptsNumber() {
        Assume.assumeThat(scheduledService.getDeliveryAttempts().size(), equalTo(0));

        Assert.assertEquals(0, scheduledService.getNumberOfAttempts());

        List<DeliveryAttempt> deliveryAttempts = scheduledService.getDeliveryAttempts();
        deliveryAttempts.add(new DeliveryAttemptBuilder().buildAsNew());
        Assert.assertEquals(1, scheduledService.getNumberOfAttempts());
    }

    @Test
    public void shouldGetAdditionalParametersAsMap() {
        Assume.assumeThat(scheduledService.getScheduledServiceParameters().size(), equalTo(0));

        Assert.assertEquals(0, scheduledService.getParameters().size());

        List<ScheduledServiceParameter> params = scheduledService.getScheduledServiceParameters();
        params.add(getParam("FLOATPARAM", "1.23"));
        params.add(getParam("INTPARAM", "1"));
        params.add(getParam("STRINGPARAM", "testString"));

        Assert.assertEquals(params.size(), scheduledService.getParameters().size());
        Assert.assertThat(scheduledService.getParameters(), hasEntry("FLOATPARAM", "1.23"));
        Assert.assertThat(scheduledService.getParameters(), hasEntry("INTPARAM", "1"));
        Assert.assertThat(scheduledService.getParameters(), hasEntry("STRINGPARAM", "testString"));
    }

    private void createTestInstance() {
        Template template = TemplateHelper.createTestInstance();
        templateDao.saveOrUpdate(template);
        Template savedTemplate = templateDao.getByUuid(template.getUuid());
        
        TemplateField templateField = TemplateFieldHelper.createTestInstace();
        templateField.setTemplate(savedTemplate);
        
        TemplateFieldValue templateFieldValue = TemplateFieldValueHelper.createTestInstance();
        templateFieldValue.setTemplateField(templateField);
        
        PatientTemplate patientTemplate = PatientTemplateHelper.createTestInstance();
        patientTemplate.setTemplate(savedTemplate);
        patientTemplateDao.saveOrUpdate(patientTemplate);
        PatientTemplate savedPatientTemplate = patientTemplateDao.getByUuid(patientTemplate.getUuid());
        
        ScheduledServiceGroup scheduledServiceGroup = ScheduledServiceGroupHelper.createTestInstance();
        messagingGroupDao.saveOrUpdate(scheduledServiceGroup);
        ScheduledServiceGroup savedScheduledServiceGroup = messagingGroupDao.getByUuid(scheduledServiceGroup.getUuid());
        
        scheduledService = ScheduledServiceHelper.createTestInstance();
        scheduledService.setPatientTemplate(savedPatientTemplate);
        scheduledService.setGroup(savedScheduledServiceGroup);
        
        messagingDao.saveOrUpdate(scheduledService);
    }

    private ScheduledServiceParameter getParam(String key, String value) {
        return new ScheduledServiceParameterBuilder()
                .withType(key)
                .withValue(value)
                .build();
    }
}
