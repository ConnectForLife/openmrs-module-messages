package org.openmrs.module.messages.api.model;

public enum ErrorMessageEnum {

    ERR_SYSTEM("system.error"),
    ERR_BAD_PARAM("system.param");

    private String code;

    ErrorMessageEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
