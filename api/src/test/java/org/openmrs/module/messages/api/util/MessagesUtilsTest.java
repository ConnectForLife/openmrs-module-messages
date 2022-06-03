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

import org.junit.Test;

import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class MessagesUtilsTest {

    private static final String KEY = "key";

    private static final String VALUE = "value";

    private static final String KEY2 = "key2";

    private static final String VALUE2 = "value2";

    private static final String MAP_AS_STRING = KEY + ":" + VALUE + "," + KEY2 + ":" + VALUE2;

    private static final String INCORRECT_MAP_AS_STRING = "key;value,key2;value2";

    @Test
    public void parseStringToMapCorrectParseMap() {
        Map<String, String> actual = MessagesUtils.parseStringToMap(MAP_AS_STRING);
        assertTrue(actual.containsKey(KEY));
        assertThat(actual.get(KEY), is(VALUE));
        assertTrue(actual.containsKey(KEY2));
        assertThat(actual.get(KEY2), is(VALUE2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parseStringToMapThrowWhenWrongFormat() {
        MessagesUtils.parseStringToMap(INCORRECT_MAP_AS_STRING);
    }
}
