package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.List;

public class MessageDetailsMapper implements ListMapper<MessageDetailsDTO, PatientTemplate> {

    private MessageMapper messageMapper;

    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageDetailsDTO toDto(List<PatientTemplate> patientTemplates) {
        return new MessageDetailsDTO(
                messageMapper.toDtos(patientTemplates)
        );
    }
}
