package org.openmrs.module.messages.api.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.TestConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class ScheduledServiceITTest extends BaseModuleContextSensitiveTest {
    
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
        Assert.assertThat(savedScheduledService, hasProperty("channelType",
                is(scheduledService.getChannelType())));
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
    
    private void createTestInstance() {
        Template template = TemplateHelper.createTestInstance();
        templateDao.saveOrUpdate(template);
        Template savedTemplate = templateDao.getByUuid(template.getUuid());
        
        TemplateField templateField = TemplateFieldHelper.createTestInstace();
        templateField.setTemplate(savedTemplate);
        
        TemplateFieldValue templateFieldValue = TemplateFieldValueHelper.createTestInstance();
        templateFieldValue.setTemplateField(templateField);
        
        PatientTemplate patientTemplate = PatientTemplateHelper.createTestInstance();
        patientTemplate.setTemplateFieldValue(templateFieldValue);
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
}
