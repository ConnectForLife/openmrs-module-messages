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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;

public class PropertiesTest {

    public static final String A_KEY = "aKey";
    public static final String B_KEY = "bKey";
    public static final int B_VALUE = 1234;
    public static final String A_VALUE = "aValue";

    @Test
    public void getShouldReturnExistingProperty() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(A_KEY, A_VALUE);

        Properties properties = new Properties(map);
        Object value = properties.get(A_KEY);

        assertThat(value, is(A_VALUE));
    }

    @Test
    public void getShouldReturnNullIfKeyDoesNotExist() {
        HashMap<String, Object> map = new HashMap<>();

        Properties properties = new Properties(map);
        Object value = properties.get(A_KEY);

        assertNull(value);
    }

    @Test
    public void getStringShouldReturnValidString() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(A_KEY, A_VALUE);

        Properties properties = new Properties(map);
        String value = properties.getString(A_KEY);

        assertThat(value, is(A_VALUE));
    }

    @Test
    public void getStringShouldReturnToStringMethodResultIfObjectPassed() {
        Object obj = new Object();
        HashMap<String, Object> map = new HashMap<>();
        map.put(A_KEY, obj);

        Properties properties = new Properties(map);
        String value = properties.getString(A_KEY);

        assertThat(value, is(obj.toString()));
    }

    @Test
    public void getIntShouldReturnValidInt() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(B_KEY, B_VALUE);

        Properties properties = new Properties(map);
        Integer value = properties.getInt(B_KEY);

        assertThat(value, is(B_VALUE));
    }

    @Test
    public void getIntShouldReturnParseStringToInt() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(B_KEY, String.valueOf(B_VALUE));

        Properties properties = new Properties(map);
        Integer value = properties.getInt(B_KEY);

        assertThat(value, is(B_VALUE));
    }

    @Test
    public void getIntShouldReturnNullIfNullPassed() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(B_KEY, null);

        Properties properties = new Properties(map);
        Integer value = properties.getInt(B_KEY);

        assertNull(value);
    }

    @Test(expected = MessagesRuntimeException.class)
    public void getIntShouldThrowExceptionIfNotParsableObjectPassed() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(B_KEY, new Object());

        Properties properties = new Properties(map);
        properties.getInt(B_KEY);
    }

    @Test
    public void getNestedPropertiesReturnNestedMapWrappedByPropertiesClass() {
        HashMap<String, Object> aKeyMap = new HashMap<>();
        aKeyMap.put(A_KEY, A_VALUE);
        HashMap<String, Object> bKeyMap = new HashMap<>();
        bKeyMap.put(B_KEY, aKeyMap);

        Properties properties = new Properties(bKeyMap);
        Object value = properties.getNestedProperties(B_KEY);

        assertThat(value, is(new Properties(aKeyMap)));
    }

    @Test(expected = MessagesRuntimeException.class)
    public void getNestedPropertiesShouldThrowExceptionIfValueIsNotMap() {
        HashMap<String, Object> bKeyMap = new HashMap<>();
        bKeyMap.put(B_KEY, new Object());

        Properties properties = new Properties(bKeyMap);
        properties.getNestedProperties(B_KEY);
    }

    @Test(expected = MessagesRuntimeException.class)
    public void getNestedPropertiesShouldThrowExceptionIfNestedMapKeysAreNotStrings() {
        HashMap<Object, Object> aKeyMap = new HashMap<>();
        aKeyMap.put(A_KEY, A_VALUE);
        aKeyMap.put(new Object(), A_VALUE);
        HashMap<String, Object> bKeyMap = new HashMap<>();
        bKeyMap.put(B_KEY, aKeyMap);

        Properties properties = new Properties(bKeyMap);
        properties.getNestedProperties(B_KEY);
    }

    @Test
    public void getNestedPropertiesShouldReturnNullIfKeyDoesNotExist() {
        HashMap<String, Object> map = new HashMap<>();

        Properties properties = new Properties(map);
        Object value = properties.getNestedProperties(A_KEY);

        assertNull(value);
    }

    @Test
    public void toStringShouldReturnStringContainingAllProperties() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(A_KEY, A_VALUE);
        map.put(B_KEY, B_VALUE);

        Properties properties = new Properties(map);
        String toStringResult = properties.toString();

        assertThat(toStringResult, Matchers.containsString(A_KEY));
        assertThat(toStringResult, Matchers.containsString(B_KEY));
        assertThat(toStringResult, Matchers.containsString(A_VALUE));
        assertThat(toStringResult, Matchers.containsString(String.valueOf(B_VALUE)));
    }
}
