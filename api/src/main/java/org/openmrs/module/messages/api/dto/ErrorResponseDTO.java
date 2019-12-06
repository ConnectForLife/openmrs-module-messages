package org.openmrs.module.messages.api.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.openmrs.module.messages.api.model.ErrorMessage;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Represent the DTO for the Error Response
 */
public class ErrorResponseDTO implements Serializable {

    private static final long serialVersionUID = -8870048107491805181L;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
