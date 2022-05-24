package org.openmrs.module.messages.api.model;

import java.io.Serializable;

public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 5227514623273315149L;

    private String code;

    private String message;

    public ErrorMessage() {
    }

    public ErrorMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public ErrorMessage setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public ErrorMessage setMessage(String message) {
        this.message = message;
        return this;
    }
}
