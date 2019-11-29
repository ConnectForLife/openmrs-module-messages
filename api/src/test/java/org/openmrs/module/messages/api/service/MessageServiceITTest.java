package org.openmrs.module.messages.api.service;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.UUID;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dao.MessagingDao;
import org.openmrs.module.messages.api.model.DeliveryAttempt;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class MessageServiceITTest extends BaseModuleContextSensitiveTest {

    private static final String XML_DATA_SET_PATH = "datasets/";

    private static final String XML_CONCEPTS_DATA_SET = "ConceptDataSet.xml";

    private static final String XML_MSG_DATA_SET = "MessageDataSet.xml";

    private static final String SCHEDULE_UUID = "b3de6d76-3e31-41cf-955d-ad14b9db07ff";

    private ScheduledService scheduledService;

    @Autowired
    @Qualifier("messages.messagingService")
    private MessagingService messagingService;

    @Autowired
    @Qualifier("messages.MessagingDao")
    private MessagingDao messagingDao;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_DATA_SET_PATH + XML_CONCEPTS_DATA_SET);
        executeDataSet(XML_DATA_SET_PATH + XML_MSG_DATA_SET);
        scheduledService = messagingDao.getByUuid(SCHEDULE_UUID);
    }

    @Test
    public void registerAttemptShouldAddFirstAttemptAndUpdateScheduledService() {
        Assume.assumeThat(scheduledService.getDeliveryAttempts().size(), is(0));

        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        ScheduledService actualScheduledService = messagingService
                .registerAttempt(scheduledService.getId(), newStatus, timestamp, serviceExecution);
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(0);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(1, actualScheduledService.getDeliveryAttempts().size());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(timestamp, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }

    @Test
    public void registerAttemptShouldAddNextAttemptAndUpdateScheduledService() {
        scheduledService = messagingService.registerAttempt(scheduledService.getId(), ServiceStatus.PENDING,
                new Date(), UUID.randomUUID().toString());
        assertEquals(1, scheduledService.getDeliveryAttempts().size());

        final ServiceStatus newStatus = ServiceStatus.DELIVERED;
        final String serviceExecution = "321";
        final Date timestamp = new Date();

        ScheduledService actualScheduledService = messagingService
                .registerAttempt(scheduledService.getId(), newStatus, timestamp, serviceExecution);
        DeliveryAttempt actualAttempt = actualScheduledService.getDeliveryAttempts().get(1);

        assertEquals(newStatus, actualScheduledService.getStatus());
        assertEquals(serviceExecution, actualScheduledService.getLastServiceExecution());
        assertEquals(2, actualScheduledService.getDeliveryAttempts().size());
        assertEquals(scheduledService.getId(), actualAttempt.getScheduledService().getId());
        assertEquals(newStatus, actualAttempt.getStatus());
        assertEquals(timestamp, actualAttempt.getTimestamp());
        assertEquals(serviceExecution, actualAttempt.getServiceExecution());
    }
}
