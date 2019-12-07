package org.openmrs.module.messages.api.exception;

public class MessagesRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -1434211965543042192L;

    public MessagesRuntimeException(String message) {
        super(message);
    }

    public MessagesRuntimeException(String message, Throwable exception) {
        super(message, exception);
    }

    public MessagesRuntimeException(Throwable throwable) {
        super(throwable);
    }
}
