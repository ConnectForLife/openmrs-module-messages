package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.ScheduledServiceParameter;
import org.openmrs.module.messages.builder.DateBuilder;
import org.openmrs.module.messages.builder.ServiceResultListBuilder;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.openmrs.module.messages.api.service.DatasetConstants.PERSON_ROMAN_ID;
import static org.openmrs.module.messages.api.service.DatasetConstants.XML_DATA_SET_PATH;

@Ignore //TODO in CFLM-471: Remove after adding logic
public class MessagesDeliveryServiceITTest extends BaseModuleContextSensitiveTest {

    @Mock
    private MessagesSchedulerService schedulerService;

    @Autowired
    @InjectMocks
    @Qualifier("messages.deliveryService")
    private MessagesDeliveryService deliveryService;

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @Autowired
    @Qualifier("messages.messagingGroupService")
    private MessagingGroupService messagingGroupService;

    @Autowired
    @Qualifier("messages.messagingParameterService")
    private MessagingParameterService messagingParameterService;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "MessageDataSet.xml");
    }

    @Test
    public void shouldSaveScheduledService() {
        ServiceResultList resultList = new ServiceResultListBuilder()
                .withActorId(PERSON_ROMAN_ID).build();
        Date date = new DateBuilder().build();

        GroupedServiceResultList group = new GroupedServiceResultList(PERSON_ROMAN_ID, date, resultList);

        int dbSizeBefore = messagingService.getAll(false).size();
        deliveryService.schedulerDelivery(group);

        List<ScheduledService> savedServices = messagingService.getAll(false);
        assertEquals(dbSizeBefore + 1, savedServices.size());
        //TODO in CFLM-471: verify if fields are saved properly
    }

    @Test
    public void shouldSaveScheduledServiceGroup() {
        ServiceResultList resultList = new ServiceResultListBuilder()
                .withActorId(PERSON_ROMAN_ID).build();
        Date date = new DateBuilder().build();

        GroupedServiceResultList group = new GroupedServiceResultList(PERSON_ROMAN_ID, date, resultList);

        int dbSizeBefore = messagingGroupService.getAll(false).size();
        deliveryService.schedulerDelivery(group);

        List<ScheduledServiceGroup> savedServices = messagingGroupService.getAll(false);
        assertEquals(dbSizeBefore + 1, savedServices.size());
        //TODO in CFLM-471: verify if fields are saved properly
    }

    @Test
    public void shouldSaveScheduledServiceParameter() {
        ServiceResultList resultList = new ServiceResultListBuilder()
                .withActorId(PERSON_ROMAN_ID).build();
        Date date = new DateBuilder().build();

        GroupedServiceResultList group = new GroupedServiceResultList(PERSON_ROMAN_ID, date, resultList);

        int dbSizeBefore = messagingParameterService.getAll(false).size();
        deliveryService.schedulerDelivery(group);

        List<ScheduledServiceParameter> savedServices = messagingParameterService.getAll(false);
        assertEquals(dbSizeBefore + 1, savedServices.size());
        //TODO in CFLM-471: verify if fields are saved properly
    }
}
