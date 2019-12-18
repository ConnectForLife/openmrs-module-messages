package org.openmrs.module.messages.api.dao.impl;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.dao.MessagingGroupDao;
import org.openmrs.module.messages.api.helper.ScheduledServiceGroupHelper;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.TestConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class ScheduledServiceGroupITTest extends BaseModuleContextSensitiveTest {

    private static final String XML_DATA_SET_PATH = "datasets/";
    private static final String SCHEDULE_1_UUID = "b3de6d76-3e31-41cf-955d-ad14b9db07ff";
    private static final String SCHEDULE_2_UUID = "532f0b56-3ff9-427b-b4f3-b92796c7eea2";
    private static final String GROUP_UUID = "3d3a36a6-ff59-4c7e-bf3e-fe3d7a6b47a6";

    @Autowired
    @Qualifier("messages.MessagingGroupDao")
    private MessagingGroupDao messagingGroupDao;

    @Autowired
    @Qualifier("messages.MessagingDao")
    private MessagingDao messagingDao;
    
    private ScheduledServiceGroup scheduledServiceGroup1;
    private ScheduledServiceGroup scheduledServiceGroup2;
    private ScheduledServiceGroup scheduledServiceGroup3;

    @Before
    public void setUp() {
        scheduledServiceGroup1 = messagingGroupDao.saveOrUpdate(ScheduledServiceGroupHelper.createTestInstance());
        scheduledServiceGroup2 = messagingGroupDao.saveOrUpdate(ScheduledServiceGroupHelper.createTestInstance());
        scheduledServiceGroup3 = messagingGroupDao.saveOrUpdate(ScheduledServiceGroupHelper.createTestInstance());
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
        Assert.assertThat(savedScheduledServiceGroup, hasProperty("actor",
                is(scheduledServiceGroup1.getActor())));
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

    @Test
    public void shouldGetRelatedScheduledServicesImmediatelyAfterAssociation() throws Exception {
        executeMessageDataSet();

        ScheduledService scheduledService1 = messagingDao.getByUuid(SCHEDULE_1_UUID);

        scheduledService1.setGroup(scheduledServiceGroup1);
        scheduledService1 = messagingDao.saveOrUpdate(scheduledService1);

        List<ScheduledService> scheduledServices = messagingGroupDao
                .getByUuid(scheduledServiceGroup1.getUuid())
                .getScheduledServices();

        Assert.assertNotNull(scheduledServices);
        Assert.assertEquals(1, scheduledServices.size());
        Assert.assertEquals(scheduledService1.getId(), scheduledServices.get(0).getId());
    }

    @Test
    public void shouldNotChangeGroupOfScheduledServiceBySettingItInScheduledServiceGroup() throws Exception {
        executeMessageDataSet();
        Context.evictFromSession(scheduledServiceGroup1);

        ScheduledService scheduledService1 = messagingDao.getByUuid(SCHEDULE_1_UUID);

        ArrayList<ScheduledService> scheduledServices = new ArrayList<>();
        scheduledServices.add(scheduledService1);
        scheduledServiceGroup1.setScheduledServices(scheduledServices);
        scheduledServiceGroup1 = messagingGroupDao.saveOrUpdate(scheduledServiceGroup1);

        ScheduledServiceGroup group = messagingDao
                .getByUuid(SCHEDULE_1_UUID)
                .getGroup();

        Assert.assertNotEquals(scheduledServiceGroup1, group);
    }

    @Test
    public void shouldGetRelatedScheduledServicesForAlreadyPersistedEntries() throws Exception {
        executeMessageDataSet();

        ScheduledService scheduledService1 = messagingDao.getByUuid(SCHEDULE_1_UUID);
        ScheduledService scheduledService2 = messagingDao.getByUuid(SCHEDULE_2_UUID);
        ScheduledServiceGroup scheduledServiceGroup = messagingGroupDao.getByUuid(GROUP_UUID);

        Assume.assumeNotNull(scheduledService1.getGroup(), scheduledService2.getGroup());
        Assume.assumeThat(scheduledService1.getGroup().getUuid(), equalTo(scheduledServiceGroup.getUuid()));
        Assume.assumeThat(scheduledService2.getGroup().getUuid(), equalTo(scheduledServiceGroup.getUuid()));

        List<ScheduledService> scheduledServices = scheduledServiceGroup.getScheduledServices();

        Assert.assertNotNull(scheduledServices);
        Assert.assertThat(scheduledServices, containsInAnyOrder(scheduledService1, scheduledService2));
    }

    private void executeMessageDataSet() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");
    }
}
