package org.openmrs.module.messages.api.execution;

/**
 * Indicates an error during execution of a service.
 */
public class ExecutionException extends Exception {

    private static final long serialVersionUID = -1152215227994789621L;

    public ExecutionException(String message) {
        super(message);
    }

    public ExecutionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
