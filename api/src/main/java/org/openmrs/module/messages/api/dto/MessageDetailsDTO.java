package org.openmrs.module.messages.api.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a message details DTO
 */
public class MessageDetailsDTO extends BaseDTO {

    private static final long serialVersionUID = 8491509272261320704L;

    private Integer personId;

    private List<MessageDTO> messages;

    public MessageDetailsDTO() {
        messages = new ArrayList<>();
    }

    public MessageDetailsDTO(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public MessageDetailsDTO withPersonId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public Integer getPersonId() {
        return personId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }
}
