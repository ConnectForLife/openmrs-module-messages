/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public final class MessagesUtils {

    /**
     * This method converts comma separated string to Map (key:value)
     *
     * @param mapAsString - comma separated string
     * @return - result Map
     */
    public static Map<String, String> parseStringToMap(String mapAsString) {
        Map<String, String> result = new HashMap<>();
        if (StringUtils.isBlank(mapAsString)) {
            return result;
        }
        String[] entries = mapAsString.split("\\s*,\\s*");
        for (String entry : entries) {
            String[] keyValue = entry.split("\\s*:\\s*");
            if (keyValue.length == 2) {
                result.put(keyValue[0], keyValue[1]);
            } else {
                throw new IllegalArgumentException(String.format("%s is an invalid map. "
                        + "Required colon separator", mapAsString));
            }
        }
        return result;
    }

    private MessagesUtils() { }
}
