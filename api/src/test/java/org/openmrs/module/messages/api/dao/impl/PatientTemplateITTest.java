package org.openmrs.module.messages.api.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.helper.PatientTemplateHelper;
import org.openmrs.module.messages.api.helper.TemplateFieldHelper;
import org.openmrs.module.messages.api.helper.TemplateFieldValueHelper;
import org.openmrs.module.messages.api.helper.TemplateHelper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.TestConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class PatientTemplateITTest extends BaseModuleContextSensitiveTest {
    
    @Autowired
    private PatientTemplateDao patientTemplateDao;
    
    @Autowired
    private TemplateDao templateDao;
    
    private PatientTemplate patientTemplate;
    
    @Before
    public void setUp() {
        createTestInstance();
        createTestInstance();
        createTestInstance();
    }
    
    @Test
    public void shouldSaveAllPropertiesInDb() {
        PatientTemplate savedPatientTemplate = patientTemplateDao.getByUuid(patientTemplate.getUuid());
        
        Assert.assertThat(savedPatientTemplate, hasProperty("uuid", is(patientTemplate.getUuid())));
        Assert.assertThat(savedPatientTemplate, hasProperty("actor", is(patientTemplate.getActor())));
        Assert.assertThat(savedPatientTemplate, hasProperty("actorType",
                is(patientTemplate.getActorType())));
        Assert.assertThat(savedPatientTemplate, hasProperty("serviceQuery",
                is(patientTemplate.getServiceQuery())));
        Assert.assertThat(savedPatientTemplate, hasProperty("serviceQueryType",
                is(patientTemplate.getServiceQueryType())));
        Assert.assertThat(savedPatientTemplate, hasProperty("patient",
                is(patientTemplate.getPatient())));
        Assert.assertThat(savedPatientTemplate, hasProperty("template",
                is(patientTemplate.getTemplate())));
    }
    
    @Test
    public void shouldReturnAllSavedPatientTemplates() {
        List<PatientTemplate> patientTemplates = patientTemplateDao.getAll(true);
        
        Assert.assertEquals(TestConstants.GET_ALL_EXPECTED_LIST_SIZE, patientTemplates.size());
    }
    
    @Test
    public void shouldDeletePatientTemplate() {
        patientTemplateDao.delete(patientTemplate);
        
        Assert.assertNull(patientTemplateDao.getByUuid(patientTemplate.getUuid()));
        Assert.assertEquals(TestConstants.DELETE_EXPECTED_LIST_SIZE, patientTemplateDao.getAll(true).size());
    }
    
    @Test
    public void shouldUpdateExistingPatientTemplate() {
        patientTemplate.setServiceQueryType("updated service query type");
        patientTemplateDao.saveOrUpdate(patientTemplate);
        
        Assert.assertThat(patientTemplateDao.getByUuid(patientTemplate.getUuid()),
                hasProperty("serviceQueryType", is("updated service query type")));
    }
    
    private void createTestInstance() {
        Template template = TemplateHelper.createTestInstance();
        templateDao.saveOrUpdate(template);
        Template savedTemplate = templateDao.getByUuid(template.getUuid());
        
        TemplateField templateField = TemplateFieldHelper.createTestInstace();
        templateField.setTemplate(savedTemplate);
        
        TemplateFieldValue templateFieldValue = TemplateFieldValueHelper.createTestInstance();
        templateFieldValue.setTemplateField(templateField);
        
        patientTemplate = PatientTemplateHelper.createTestInstance();
        patientTemplate.setTemplate(savedTemplate);
        
        patientTemplateDao.saveOrUpdate(patientTemplate);
    }
}
