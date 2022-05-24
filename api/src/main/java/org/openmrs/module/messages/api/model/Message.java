/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

    private static final long serialVersionUID = -6672369644680287938L;

    private final String name;
    private final int messageId;
    private final Map<String, String> params;

    public Message(String name, int messageId, Map<String, String> params) {
        this.name = name;
        this.messageId = messageId;
        this.params = params;
    }

    public String getName() {
        return name;
    }

    public int getMessageId() {
        return messageId;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Map<String, Object> toPrimitivesMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("messageId", messageId);
        map.put("params", params);
        return map;
    }
}
