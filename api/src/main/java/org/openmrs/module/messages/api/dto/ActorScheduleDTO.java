package org.openmrs.module.messages.api.dto;

public class ActorScheduleDTO {

    private String actorType;

    private String schedule;

    public ActorScheduleDTO(String actorType, String schedule) {
        this.actorType = actorType;
        this.schedule = schedule;
    }

    public ActorScheduleDTO() { }

    public String getActorType() {
        return actorType;
    }

    public String getSchedule() {
        return schedule;
    }
}
