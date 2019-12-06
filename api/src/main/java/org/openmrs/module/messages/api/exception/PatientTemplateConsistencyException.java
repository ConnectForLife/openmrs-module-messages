package org.openmrs.module.messages.api.exception;

public class PatientTemplateConsistencyException extends RuntimeException {

    private static final long serialVersionUID = -5403674568485759357L;

    public PatientTemplateConsistencyException(String message) {
        super(message);
    }

    public PatientTemplateConsistencyException(String message, Throwable exception) {
        super(message, exception);
    }

    public PatientTemplateConsistencyException(Throwable throwable) {
        super(throwable);
    }
}
