package org.openmrs.module.messages.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageDTO {

    private String type;

    private Date createdAt;

    private UserDTO author;

    private List<ActorScheduleDTO> actorSchedules;

    //TODO in CFLM-319: Remove this constructor
    public MessageDTO(String type, Date createdAt, UserDTO author, ActorScheduleDTO actorSchedule) {
        this(type, createdAt, author, new ArrayList<>());
        this.actorSchedules.add(actorSchedule);
    }

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
