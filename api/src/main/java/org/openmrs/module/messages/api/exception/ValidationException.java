package org.openmrs.module.messages.api.exception;

import org.openmrs.module.messages.api.dto.ErrorResponseDTO;
import org.openmrs.module.messages.api.model.ErrorMessage;

import java.util.List;

public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -3530093625346314593L;

    private ErrorResponseDTO errorResponse;

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
