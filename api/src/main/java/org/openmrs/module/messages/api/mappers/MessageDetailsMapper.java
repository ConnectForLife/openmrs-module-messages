package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.ArrayList;
import java.util.List;

public class MessageDetailsMapper {

    private UserMapper userMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public MessageDetailsDTO toDto(List<PatientTemplate> patientTemplates, Integer patientId) {
        return new MessageDetailsDTO(
                patientId,
                toMessageDTOs(patientTemplates)
        );
    }

    private List<MessageDTO> toMessageDTOs(List<PatientTemplate> patientTemplates) {
        List<MessageDTO> messages = new ArrayList<>();
        for (PatientTemplate template : patientTemplates) {
            messages.add(toMessageDTO(template));
        }
        return messages;
    }

    private MessageDTO toMessageDTO(PatientTemplate patientTemplate) {
        return new MessageDTO(
                patientTemplate.getServiceQueryType(),
                patientTemplate.getDateCreated(),
                userMapper.toDto(patientTemplate.getCreator()),
                toActorScheduleDTO(patientTemplate)
        );
    }

    private ActorScheduleDTO toActorScheduleDTO(PatientTemplate patientTemplate) {
        return new ActorScheduleDTO(patientTemplate.getActorType() == null ? null :
                patientTemplate.getActorType().getRelationshipType().getaIsToB(),
                null
        );
    }
}
