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
 * Represents an actor response DTO
 */
public class ActorResponseDTO extends BaseDTO {

    private static final long serialVersionUID = 7278190508572060822L;

    private String response;

    private String over;

    private long responseCount;

    public String getResponse() {
        return response;
    }

    public ActorResponseDTO setResponse(String response) {
        this.response = response;
        return this;
    }

    public String getOver() {
        return over;
    }

    public ActorResponseDTO setOver(String over) {
        this.over = over;
        return this;
    }

    public long getResponseCount() {
        return responseCount;
    }

    public ActorResponseDTO setResponseCount(long responseCount) {
        this.responseCount = responseCount;
        return this;
    }
}
