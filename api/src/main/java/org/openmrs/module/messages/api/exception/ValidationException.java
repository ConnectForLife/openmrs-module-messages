/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.exception;

import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.model.ErrorMessage;

import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 2L;

    private final ErrorResponseDTO errorResponse;

    public ValidationException(String error) {
        this.errorResponse = new ErrorResponseDTO(error);
    }

    public ValidationException(String error, List<ErrorMessage> errorMessages) {
        this.errorResponse = new ErrorResponseDTO(error, errorMessages);
    }

    public ValidationException(List<ErrorMessage> errorMessages) {
        this.errorResponse = new ErrorResponseDTO(errorMessages);
    }

    public ValidationException(ErrorMessage errorMessage) {
        this.errorResponse = new ErrorResponseDTO(errorMessage);
    }

    public ErrorResponseDTO getErrorResponse() {
        return errorResponse;
    }
}
