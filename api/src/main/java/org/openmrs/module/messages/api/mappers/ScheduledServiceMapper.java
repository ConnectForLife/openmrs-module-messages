package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.types.ServiceStatus;

public class ScheduledServiceMapper extends AbstractMapper<ServiceResult, ScheduledService> {

    private ScheduledParamsMapper paramsMapper;

    @Override
    public ServiceResult toDto(ScheduledService dao) {
        throw new NotImplementedException("mapping from ScheduledService to Pair<ServiceResult, String> " +
                "is not implemented yet");
    }

    @Override
    public ScheduledService fromDto(ServiceResult dto) {
        ScheduledService result = new ScheduledService();
        result.setStatus(ServiceStatus.PENDING);
        PatientTemplate template = new PatientTemplate();
        template.setId(dto.getPatientTemplateId());
        result.setPatientTemplate(template);
        result.setChannelType(dto.getChannelType());
        result.setScheduledServiceParameters(paramsMapper.fromDto(dto));
        return result;
    }

    @Override
    public void updateFromDto(ServiceResult source, ScheduledService target) {
        throw new NotImplementedException("update from DTO is not implemented yet");
    }

    public void setParamsMapper(ScheduledParamsMapper paramsMapper) {
        this.paramsMapper = paramsMapper;
    }
}
