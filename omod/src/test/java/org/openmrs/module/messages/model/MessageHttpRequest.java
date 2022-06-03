/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.model;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
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
