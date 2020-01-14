package org.openmrs.module.messages.api.exception;

/**
 * Occurred in case of unsuccessful looking specific object
 */
public class MessagesObjectNotFound extends RuntimeException {

    private static final long serialVersionUID = -6703715907061392190L;

    public MessagesObjectNotFound() {
        super();
    }

    public MessagesObjectNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    public MessagesObjectNotFound(String message) {
        super(message);
    }

    public MessagesObjectNotFound(Throwable cause) {
        super(cause);
    }
}
