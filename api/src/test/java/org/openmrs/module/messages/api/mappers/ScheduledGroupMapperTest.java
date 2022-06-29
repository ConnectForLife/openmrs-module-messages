package org.openmrs.module.messages.api.mappers;

import org.junit.Test;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.GroupedServiceResultListKey;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ScheduledGroupMapperTest {

    @Test
    public void shouldMapResultListToServiceGroup() {
        ScheduledGroupMapper mapper = new ScheduledGroupMapper();

        ScheduledServiceGroup actual = mapper.fromDto(createTestResultList());

        assertNotNull(actual);
        assertEquals(Integer.valueOf(100), actual.getPatient().getPatientId());
        assertEquals(Integer.valueOf(100), actual.getActor().getId());
        assertEquals(ServiceStatus.PENDING, actual.getStatus());
        assertEquals("SMS", actual.getChannelType());
    }


    private GroupedServiceResultList createTestResultList() {
        return new GroupedServiceResultList(
                new GroupedServiceResultListKey("SMS", createTestServiceResult(), "Patient"),
                new ArrayList<>()
        );
    }

    private ServiceResult createTestServiceResult() {
        ServiceResult serviceResult = new ServiceResult();
        serviceResult.setPatientId(100);
        serviceResult.setActorId(100);
        serviceResult.setExecutionDate(ZonedDateTime.now());
        serviceResult.setChannelType("SMS");
        return serviceResult;
    }
}
