/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Represents an actor schedule DTO
 */
public class ActorScheduleDTO extends BaseDTO implements Comparable<ActorScheduleDTO>, DTO {

    private static final long serialVersionUID = 6618441859584061402L;
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
