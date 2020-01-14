/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides base operation which can be performed on JSON object
 */
public final class JsonUtil {

    /**
     * Convert list from string JSON representation to Java ArrayList
     * @param stringList - string value of list
     * @param listType - the target Java list type token {@link TypeToken}
     * @param <T> - the target type
     * @return - the Java representation of list
     */
    public static <T> List<T> listFromJson(String stringList, TypeToken listType) {
        List<T> results = new ArrayList<T>();
        if (StringUtils.isNotBlank(stringList)) {
            Gson gson = MapperUtil.getGson();
            results = gson.fromJson(stringList, listType.getType());
        }
        return results;
    }

    private JsonUtil() { }
}
