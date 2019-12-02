package org.openmrs.module.messages.api.dto;

public class ActorScheduleDTO {

    private String actorType;

    public ActorScheduleDTO(String actorType) {
        this.actorType = actorType;
    }

    public ActorScheduleDTO() { }

    public String getActorType() {
        return actorType;
    }
}
