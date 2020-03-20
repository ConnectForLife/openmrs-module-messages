package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

import java.util.List;

public class ScheduledGroupMapper extends AbstractMapper<GroupedServiceResultList, ScheduledServiceGroup> {

    private ScheduledServiceMapper serviceMapper;

    @Override
    public GroupedServiceResultList toDto(ScheduledServiceGroup dao) {
        throw new NotImplementedException("mapping from ScheduledService to ServiceResult is not implemented yet");
    }

    @Override
    public ScheduledServiceGroup fromDto(GroupedServiceResultList dto) {
        ScheduledServiceGroup result = new ScheduledServiceGroup();
        result.setPatient(new Patient(dto.getGroup().getPatientId()));
        result.setActor(new Person(dto.getActorWithExecutionDate().getActorId()));
        result.setStatus(ServiceStatus.PENDING);
        result.setScheduledServices(getScheduledServices(dto.getGroup()));
        result.setMsgSendTime(dto.getActorWithExecutionDate().getDate());
        return result;
    }

    @Override
    public void updateFromDto(GroupedServiceResultList source, ScheduledServiceGroup target) {
        throw new NotImplementedException("update from DTO is not implemented yet");
    }

    public void setServiceMapper(ScheduledServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    private List<ScheduledService> getScheduledServices(ServiceResultList dto) {
        List<ScheduledService> result = serviceMapper.fromDtos(dto.getResults());

        for (ScheduledService service : result) {
            service.setService(dto.getServiceName());
        }

        return result;
    }
}
