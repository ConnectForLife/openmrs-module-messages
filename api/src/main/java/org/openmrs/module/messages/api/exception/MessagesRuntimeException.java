package org.openmrs.module.messages.api.exception;

import org.openmrs.api.APIException;

public class MessagesRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -1434211965543042192L;

    public MessagesRuntimeException(String message, APIException exception) {
        super(message, exception);
    }
}
