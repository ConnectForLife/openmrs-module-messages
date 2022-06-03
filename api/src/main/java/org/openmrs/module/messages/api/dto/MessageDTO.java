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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a message DTO
 */
public class MessageDTO extends BaseDTO {

    private static final long serialVersionUID = 2608456352851663522L;

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
