package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

public class MessageMapper extends AbstractMapper<MessageDTO, PatientTemplate> {

    private UserMapper userMapper;

    private ActorScheduleMapper actorScheduleMapper;

    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void setActorScheduleMapper(ActorScheduleMapper actorScheduleMapper) {
        this.actorScheduleMapper = actorScheduleMapper;
    }

    @Override
    public MessageDTO toDto(PatientTemplate dao) {
        return new MessageDTO(
                dao.getServiceQueryType(),
                dao.getDateCreated(),
                userMapper.toDto(dao.getCreator()),
                actorScheduleMapper.toDto(dao)
        );
    }

    @Override
    public PatientTemplate fromDto(MessageDTO dto) {
        throw new NotImplementedException("mapping from MessageDTO to PatientTemplate is not implemented yet");
    }
}
