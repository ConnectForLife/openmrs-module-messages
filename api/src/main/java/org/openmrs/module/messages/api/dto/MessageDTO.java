package org.openmrs.module.messages.api.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a message DTO
 */
public class MessageDTO {

    private String type;

    private Date createdAt;

    private UserDTO author;

    private List<ActorScheduleDTO> actorSchedules;

    /**
     * Constructor of an Message DTO object
     *
     * @param type type of message, in other words name of template e.g. Adherence report daily
     * @param createdAt date of message type creation
     * @param author creator of message type, in other words system user e.g. admin
     * @param actorSchedules list of ActorScheduleDTO objects
     */
    public MessageDTO(String type, Date createdAt, UserDTO author, List<ActorScheduleDTO> actorSchedules) {
        this.type = type;
        this.createdAt = createdAt;
        this.author = author;
        this.actorSchedules = actorSchedules;
    }

    public MessageDTO() { }

    public MessageDTO(String type) {
        this.type = type;
        this.actorSchedules = new ArrayList<>();
    }

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

    public MessageDTO setActorSchedules(List<ActorScheduleDTO> actorSchedules) {
        this.actorSchedules = actorSchedules;
        return this;
    }
}
