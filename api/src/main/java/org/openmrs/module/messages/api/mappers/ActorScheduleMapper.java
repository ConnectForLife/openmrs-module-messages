package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

public class ActorScheduleMapper extends AbstractMapper<ActorScheduleDTO, PatientTemplate> {

    @Override
    public ActorScheduleDTO toDto(PatientTemplate dao) {
        return new ActorScheduleDTO(dao.getActorType() == null ? null :
                dao.getActorType().getRelationshipType().getaIsToB(),
                null
        );
    }

    @Override
    public PatientTemplate fromDto(ActorScheduleDTO dto) {
        throw new NotImplementedException("mapping from ActorScheduleDTO to PatientTemplate is not implemented yet");
    }
}
