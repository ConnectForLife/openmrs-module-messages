package org.openmrs.module.messages.model;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.UUID;

public class MessageHttpRequest extends MockHttpServletRequest {

    private boolean validSessionId;

    private boolean generatedSessionID = true;

    @Override
    public String getRequestedSessionId() {
        if (generatedSessionID) {
            return UUID.randomUUID().toString();
        }
        return null;
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        return this.validSessionId;
    }

    public boolean isValidSessionId() {
        return validSessionId;
    }

    public MessageHttpRequest setValidSessionId(boolean validSessionId) {
        this.validSessionId = validSessionId;
        return this;
    }

    public boolean isGeneratedSessionID() {
        return generatedSessionID;
    }

    public MessageHttpRequest setGeneratedSessionID(boolean generatedSessionID) {
        this.generatedSessionID = generatedSessionID;
        return this;
    }

    public MessageHttpRequest setAuthorization(String username, String password) {
        String encoding = Base64.encodeBase64String((username + ":" + password).getBytes());
        this.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        return this;
    }

    public String getAuthorization() {
        return this.getHeader(HttpHeaders.AUTHORIZATION);
    }
}
