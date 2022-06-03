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
import java.util.List;

/**
 * Represents a message details DTO
 */
public class MessageDetailsDTO extends BaseDTO {

    private static final long serialVersionUID = 280222211128011218L;

    private Integer personId;

    private List<MessageDTO> messages;

    public MessageDetailsDTO() {
        messages = new ArrayList<>();
    }

    public MessageDetailsDTO(List<MessageDTO> messages) {
        this.messages = messages;
    }

    public MessageDetailsDTO withPersonId(Integer personId) {
        this.personId = personId;
        return this;
    }

    public Integer getPersonId() {
        return personId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }
}
