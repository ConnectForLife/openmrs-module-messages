package org.openmrs.module.messages.api.dto;

import java.util.List;

public class MessageDetailsDTO {

    private Integer patientId;

    private List<MessageDTO> messages;

    public MessageDetailsDTO() { }

    public MessageDetailsDTO(Integer patientId, List<MessageDTO> messages) {
        this.patientId = patientId;
        this.messages = messages;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }
}
