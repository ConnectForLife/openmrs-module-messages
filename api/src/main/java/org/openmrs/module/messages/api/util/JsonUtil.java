/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides base operation which can be performed on JSON object
 */
public final class JsonUtil {

    public static final TypeToken STRING_TO_STRING_MAP = new TypeToken<Map<String, String>>() { };

    public static Gson getGson() {
        return new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    }

    /**
     * Convert list from string JSON representation to Java ArrayList
     * @param source - string value of list
     * @param listType - the target Java list type token {@link TypeToken}
     * @param <T> - the target type
     * @return - the Java representation of list
     */
    public static <T> List<T> toList(String source, TypeToken listType) {
        List<T> results = new ArrayList<T>();
        if (StringUtils.isNotBlank(source)) {
            results = getGson().fromJson(source, listType.getType());
        }
        return results;
    }

    /**
     * Convert map from string JSON representation to Java HashMap
     * @param source - string value of map
     * @param mapType - the target Java map type token {@link TypeToken}
     * @param <K> - the target map key type
     * @param <V> - the target map value type
     * @return - the Java representation of map
     */
    public static <K, V> Map<K, V> toMap(String source, TypeToken mapType) {
        if (StringUtils.isBlank(source)) {
            return new HashMap<K, V>();
        }
        return getGson().fromJson(source, mapType.getType());
    }

    /**
     * Convert map from Java HashMap to string JSON representation
     * @param source - map to be stringified
     * @param <K> - the source map key type
     * @param <V> - the source map value type
     * @return - the Java representation of map
     */
    public static <K, V> String fromMap(Map<K, V> source) {
        return getGson().toJson(source);
    }

    private JsonUtil() { }
}
