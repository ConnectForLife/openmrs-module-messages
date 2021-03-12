package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.util.ActorScheduleBuildingUtil;

public class ActorScheduleMapper extends AbstractOpenMrsDataMapper<ActorScheduleDTO, PatientTemplate> {

    @Override
    public ActorScheduleDTO toDto(PatientTemplate dao) {
        return ActorScheduleBuildingUtil.build(dao);
    }

    @Override
    public PatientTemplate fromDto(ActorScheduleDTO dto) {
        throw new NotImplementedException("mapping from ActorScheduleDTO to PatientTemplate is not implemented yet");
    }

    @Override
    public void updateFromDto(ActorScheduleDTO source, PatientTemplate target) {
        throw new NotImplementedException("update from DTO is not implemented yet");
    }
}
