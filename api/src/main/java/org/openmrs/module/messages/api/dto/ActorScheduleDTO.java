package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Represents an actor schedule DTO
 */
public class ActorScheduleDTO extends BaseDTO implements Comparable<ActorScheduleDTO>, DTO {

    private static final long serialVersionUID = -7315030726149122509L;
    private Integer actorId;
    private String actorType;
    private String schedule;
    private Integer patientId;
    private String patientName;

    /**
     * Constructor of an ActorSchedule DTO object
     *
     * @param actorId actor id
     * @param actorType actor type
     * @param schedule comma separated string containing patient template field values
     * @param patientId patient id
     * @param patientName patient name
     */
    public ActorScheduleDTO(Integer actorId, String actorType, String schedule, Integer patientId, String patientName) {
        this.actorId = actorId;
        this.actorType = actorType;
        this.schedule = schedule;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public ActorScheduleDTO(Integer actorId, String actorType, String schedule) {
        this.actorId = actorId;
        this.actorType = actorType;
        this.schedule = schedule;
    }

    public ActorScheduleDTO(String actorType, String schedule) {
        this.actorType = actorType;
        this.schedule = schedule;
    }

    public ActorScheduleDTO(Integer actorId, String actorType) {
        this.actorId = actorId;
        this.actorType = actorType;
    }

    public ActorScheduleDTO() { }

    public Integer getActorId() {
        return actorId;
    }

    public String getActorType() {
        return actorType;
    }

    public String getSchedule() {
        return schedule;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    @Override
    @JsonIgnore
    public Integer getId() {
        throw new NotImplementedException("not implemented yet");
    }

    @Override
    public int compareTo(ActorScheduleDTO o) {
        return new CompareToBuilder()
                .append(this.getActorId(), o.getActorId())
                .toComparison();
    }
}
