package org.openmrs.module.messages.api.dto;

import java.util.Date;
import java.util.List;

public class MessageDTO {

    private String type;

    private Date createdAt;

    private UserDTO author;

    private List<ActorScheduleDTO> actorSchedules;

    public MessageDTO(String type, Date createdAt, UserDTO author, List<ActorScheduleDTO> actorSchedules) {
        this.type = type;
        this.createdAt = createdAt;
        this.author = author;
        this.actorSchedules = actorSchedules;
    }

    public MessageDTO() { }

    public String getType() {
        return type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public UserDTO getAuthor() {
        return author;
    }

    public List<ActorScheduleDTO> getActorSchedules() {
        return actorSchedules;
    }
}
