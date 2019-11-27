package org.openmrs.module.messages.api.dao.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.helper.ScheduledServiceGroupHelper;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.TestConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

public class ScheduledServiceGroupITTest extends BaseModuleContextSensitiveTest {
    
    @Autowired
    private MessagingGroupDao messagingGroupDao;
    
    private ScheduledServiceGroup scheduledServiceGroup1;
    private ScheduledServiceGroup scheduledServiceGroup2;
    private ScheduledServiceGroup scheduledServiceGroup3;
    
    @Before
    public void setUp() {
        scheduledServiceGroup1 = ScheduledServiceGroupHelper.createTestInstance();
        messagingGroupDao.saveOrUpdate(scheduledServiceGroup1);
        scheduledServiceGroup2 = ScheduledServiceGroupHelper.createTestInstance();
        messagingGroupDao.saveOrUpdate(scheduledServiceGroup2);
        scheduledServiceGroup3 = ScheduledServiceGroupHelper.createTestInstance();
        messagingGroupDao.saveOrUpdate(scheduledServiceGroup3);
    }
    
    @Test
    public void shouldSaveAllPropertiesInDb() {
        ScheduledServiceGroup savedScheduledServiceGroup = messagingGroupDao.getByUuid(scheduledServiceGroup1.getUuid());
        
        Assert.assertThat(savedScheduledServiceGroup, hasProperty("uuid",
                is(scheduledServiceGroup1.getUuid())));
        Assert.assertThat(savedScheduledServiceGroup, hasProperty("msgSendTime",
                is(scheduledServiceGroup1.getMsgSendTime())));
        Assert.assertThat(savedScheduledServiceGroup, hasProperty("patient",
                is(scheduledServiceGroup1.getPatient())));
        Assert.assertThat(savedScheduledServiceGroup, hasProperty("status",
                is(scheduledServiceGroup1.getStatus())));
    }
    
    @Test
    public void shouldReturnAllSavedScheduleServiceGroups() {
        List<ScheduledServiceGroup> scheduledServiceGroups = messagingGroupDao.getAll(true);
        
        Assert.assertEquals(TestConstants.GET_ALL_EXPECTED_LIST_SIZE, scheduledServiceGroups.size());
    }
    
    @Test
    public void shouldDeleteScheduleServiceGroup() {
        messagingGroupDao.delete(scheduledServiceGroup3);
        
        Assert.assertNull(messagingGroupDao.getByUuid(scheduledServiceGroup3.getUuid()));
        Assert.assertEquals(TestConstants.DELETE_EXPECTED_LIST_SIZE, messagingGroupDao.getAll(true).size());
    }
    
    @Test
    public void shouldUpdateExistingScheduleServiceGroup() {
        scheduledServiceGroup2.setStatus(ServiceStatus.FAILED);
        messagingGroupDao.saveOrUpdate(scheduledServiceGroup2);
        
        Assert.assertThat(messagingGroupDao.getByUuid(scheduledServiceGroup2.getUuid()),
                hasProperty("status", is(ServiceStatus.FAILED)));
    }
}
