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

/**
 * Represents default contact time DTO
 */
public class DefaultContactTimeDTO extends BaseDTO {

    private static final long serialVersionUID = 6793419256280543903L;

    private String actor;

    private String time;

    public DefaultContactTimeDTO() { }

    /**
     * Constructor of a DefaultContactTime DTO object
     *
     * @param actor uuid of relationship type taken from best contact time config (global property)
     * @param time time as a text value assigned to particular actor
     */
    public DefaultContactTimeDTO(String actor, String time) {
        this.actor = actor;
        this.time = time;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
