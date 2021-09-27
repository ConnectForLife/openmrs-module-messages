package org.openmrs.module.messages.api.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.ContextSensitiveTest;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.helper.TemplateHelper;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.TestConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class TemplateDaoITTest extends ContextSensitiveTest {
    
    @Autowired
    private TemplateDao templateDao;
    
    private Template template1;
    private Template template2;
    private Template template3;
    
    @Before
    public void setUp() {
        template1 = TemplateHelper.createTestInstance();
        templateDao.saveOrUpdate(template1);
        template2 = TemplateHelper.createTestInstance();
        templateDao.saveOrUpdate(template2);
        template3 = TemplateHelper.createTestInstance();
        templateDao.saveOrUpdate(template3);
    }
    
    @Test
    public void shouldSaveAllPropertiesInDb() {
        Template savedTemplate = templateDao.getByUuid(template1.getUuid());
        
        Assert.assertThat(savedTemplate, hasProperty("uuid", is(template1.getUuid())));
        Assert.assertThat(savedTemplate, hasProperty("serviceQuery", is(template1.getServiceQuery())));
        Assert.assertThat(savedTemplate, hasProperty("serviceQueryType",
                is(template1.getServiceQueryType())));
        Assert.assertThat(savedTemplate, hasProperty("calendarServiceQuery", is(template1.getCalendarServiceQuery())));
    }
    
    @Test
    public void shouldReturnAllSavedTemplates() {
        List<Template> templates = templateDao.getAll(true);
        
        Assert.assertEquals(TestConstants.GET_ALL_EXPECTED_LIST_SIZE, templates.size());
    }
    
    @Test
    public void shouldDeleteTemplate() {
        templateDao.delete(template2);
        
        Assert.assertNull(templateDao.getByUuid(template2.getUuid()));
        Assert.assertEquals(TestConstants.DELETE_EXPECTED_LIST_SIZE, templateDao.getAll(true).size());
    }
    
    @Test
    public void shouldUpdateExistingTemplate() {
        template1.setServiceQuery("updated service query");
        templateDao.saveOrUpdate(template1);
        
        Assert.assertThat(templateDao.getByUuid(template1.getUuid()),
                hasProperty("serviceQuery", is("updated service query")));
    }

    @Test
    public void shouldReturnCalendarQueryIfNotBlank() {
        template1.setCalendarServiceQuery(Constant.EXAMPLE_PATIENT_TEMPLATE_CALENDAR_SERVICE_QUERY);
        Assert.assertEquals(Constant.EXAMPLE_PATIENT_TEMPLATE_CALENDAR_SERVICE_QUERY, template1.getCalendarServiceQuery());
    }

    @Test
    public void shouldReturnServiceQueryIfCalendarQueryIsNull() {
        template1.setCalendarServiceQuery(null);
        Assert.assertEquals(Constant.EXAMPLE_TEMPLATE_SERVICE_QUERY, template1.getCalendarServiceQuery());
    }

    @Test
    public void shouldReturnServiceQueryIfCalendarQueryIsEmpty() {
        template1.setCalendarServiceQuery("");
        Assert.assertEquals(Constant.EXAMPLE_TEMPLATE_SERVICE_QUERY, template1.getCalendarServiceQuery());
    }

}
