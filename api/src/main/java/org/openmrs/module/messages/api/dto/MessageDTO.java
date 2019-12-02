package org.openmrs.module.messages.api.dto;

import java.util.Date;

public class MessageDTO {

    private String type;

    private Date createdAt;

    private UserDTO author;

    private ActorScheduleDTO actorSchedule;

    public MessageDTO(String type, Date createdAt, UserDTO author, ActorScheduleDTO actorSchedule) {
        this.type = type;
        this.createdAt = createdAt;
        this.author = author;
        this.actorSchedule = actorSchedule;
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

    public ActorScheduleDTO getActorSchedule() {
        return actorSchedule;
    }
}
