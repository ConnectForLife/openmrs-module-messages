package org.openmrs.module.messages.api.dto;

import java.util.List;

public class MessageDetailsDTO {

    private Integer patientId;

    private List<MessageDTO> messages;

    public MessageDetailsDTO() { }

    public MessageDetailsDTO(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public MessageDetailsDTO withPatientId(Integer patientId) {
        this.patientId = patientId;
        return this;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }
}
