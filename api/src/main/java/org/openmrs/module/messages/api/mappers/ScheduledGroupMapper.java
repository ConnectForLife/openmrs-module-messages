package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.module.messages.api.execution.GroupedServiceResult;
import org.openmrs.module.messages.api.execution.GroupedServiceResultList;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;
import org.openmrs.module.messages.api.model.types.ServiceStatus;
import org.openmrs.module.messages.api.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

public class ScheduledGroupMapper extends AbstractOpenMrsDataMapper<GroupedServiceResultList, ScheduledServiceGroup> {

    private ScheduledServiceMapper serviceMapper;

    @Override
    public GroupedServiceResultList toDto(ScheduledServiceGroup dao) {
        throw new NotImplementedException("mapping from ScheduledService to ServiceResult is not implemented yet");
    }

    @Override
    public ScheduledServiceGroup fromDto(GroupedServiceResultList dto) {
        ScheduledServiceGroup result = new ScheduledServiceGroup();
        result.setPatient(new Patient(dto.getKey().getPatientId()));
        result.setActor(new Person(dto.getKey().getActorId()));
        result.setStatus(ServiceStatus.PENDING);
        result.setScheduledServices(getScheduledServices(dto.getGroup()));
        result.setMsgSendTime(DateUtil.toDate(dto.getKey().getDate()));
        result.setChannelType(dto.getKey().getChannelType());
        return result;
    }

    @Override
    public void updateFromDto(GroupedServiceResultList source, ScheduledServiceGroup target) {
        throw new NotImplementedException("update from DTO is not implemented yet");
    }

    public void setServiceMapper(ScheduledServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    private List<ScheduledService> getScheduledServices(List<GroupedServiceResult> serviceResults) {
        final List<ScheduledService> result = new ArrayList<>(serviceResults.size());

        for (GroupedServiceResult groupedServiceResult : serviceResults) {
            final ScheduledService scheduledService = serviceMapper.fromDto(groupedServiceResult.getServiceResult());
            scheduledService.setService(groupedServiceResult.getServiceName());
            result.add(scheduledService);
        }

        return result;
    }
}
