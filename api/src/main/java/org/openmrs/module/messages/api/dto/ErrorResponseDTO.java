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

import org.openmrs.module.messages.api.model.ErrorMessage;

import java.util.Collections;
import java.util.List;

/**
 * Represent the DTO for the Error Response
 */
public class ErrorResponseDTO extends BaseDTO {

    private static final long serialVersionUID = -3664657366331121837L;

    private String error;

    private List<ErrorMessage> errorMessages;

    public ErrorResponseDTO(String error) {
        this.error = error;
    }

    public ErrorResponseDTO(String error, List<ErrorMessage> errorMessages) {
        this.error = error;
        this.errorMessages = errorMessages;
    }

    public ErrorResponseDTO(List<ErrorMessage> errorMessages) {
        this.error = "Occurred errors:";
        this.errorMessages = errorMessages;
    }

    public ErrorResponseDTO(ErrorMessage errorMessage) {
        this(Collections.singletonList(errorMessage));
    }

    public String getError() {
        return error;
    }

    public ErrorResponseDTO setError(String error) {
        this.error = error;
        return this;
    }

    public List<ErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public ErrorResponseDTO setErrorMessages(List<ErrorMessage> errorMessages) {
        this.errorMessages = errorMessages;
        return this;
    }
}
