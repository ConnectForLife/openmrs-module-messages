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
 * Represents a person status DTO
 */
public class PersonStatusDTO extends BaseDTO {

    private static final long serialVersionUID = 6372003167420924058L;

    private String title;

    private String value;

    private String style;

    private String reason;

    private Integer personId;

    private String personUuid;

    public String getTitle() {
        return title;
    }

    public PersonStatusDTO setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getValue() {
        return value;
    }

    public PersonStatusDTO setValue(String value) {
        this.value = value;
        return this;
    }

    public String getStyle() {
        return style;
    }

    public PersonStatusDTO setStyle(String style) {
        this.style = style;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public PersonStatusDTO setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Integer getPersonId() {
        return personId;
    }

    public PersonStatusDTO setPersonId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public String getPersonUuid() {
        return personUuid;
    }

    public PersonStatusDTO setPersonUuid(String personUuid) {
        this.personUuid = personUuid;
        return this;
    }
}
