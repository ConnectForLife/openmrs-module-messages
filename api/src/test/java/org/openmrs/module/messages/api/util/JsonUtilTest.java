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

import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.model.PersonStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class JsonUtilTest {

    private static final String STRING_LIST = "[\n"
            + "  {\n"
            + "    \"name\": \"NO_CONSENT\",\n"
            + "    \"style\": \"background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;\""
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"ACTIVATED\",\n"
            + "    \"style\": \"background-color: #51a351; border-color: #51a351; color: #f5f5f5;\""
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"DEACTIVATED\",\n"
            + "    \"style\": \"background-color: #f23722; border-color: #f23722; color: #f5f5f5;\""
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"MISSING_VALUE\",\n"
            + "    \"style\": \"background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;\""
            + "  }\n"
            + "]";

    private static final String STRING_MAP = ""
            + "  {\n"
            + "    \"property1\": \"String\",\n"
            + "    \"prop2\": \"20-02-2020\",\n"
            + "    \"\": \"#f5f5f5\"\n"
            + "  }";

    private static final List<PersonStatusConfigDTO> EXPECTED_LIST = new ArrayList<PersonStatusConfigDTO>();
    private static final Map<String, String> EXPECTED_MAP = new HashMap<String, String>();

    private static final String EXPECTED_ACTIVATED_STYLE =
            "background-color: #51a351; border-color: #51a351; color: #f5f5f5;";

    private static final String EXPECTED_MISSING_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    private static final String EXPECTED_DEACTIVATE_STYLE =
            "background-color: #f23722; border-color: #f23722; color: #f5f5f5;";

    private static final String EXPECTED_NO_CONSENT_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    @Before
    public void setUp() {
        buildExpectedList();
        buildExpectedMap();
    }

    @Test
    public void shouldMapJsonToList() {
        List<PersonStatusConfigDTO> actual = JsonUtil.toList(STRING_LIST,
                new TypeToken<ArrayList<PersonStatusConfigDTO>>() { });
        assertThat(actual, is(EXPECTED_LIST));
    }

    @Test
    public void shouldReturnEmptyMapForBlankString() {
        Map<String, String> actual = JsonUtil.toMap(" \t \n   ", JsonUtil.STRING_TO_STRING_MAP);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldReturnEmptyMapForEmptyString() {
        Map<String, String> actual = JsonUtil.toMap("", JsonUtil.STRING_TO_STRING_MAP);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldReturnEmptyMapForNull() {
        Map<String, String> actual = JsonUtil.toMap(null, JsonUtil.STRING_TO_STRING_MAP);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void shouldMapJsonToMap() {
        Map<String, String> actual = JsonUtil.toMap(STRING_MAP, JsonUtil.STRING_TO_STRING_MAP);
        assertThat(actual, is(EXPECTED_MAP));
    }

    private void buildExpectedMap() {
        EXPECTED_MAP.put("property1", "String");
        EXPECTED_MAP.put("prop2", "20-02-2020");
        EXPECTED_MAP.put("", "#f5f5f5");
    }

    private void buildExpectedList() {
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.NO_CONSENT.name())
                        .setStyle(EXPECTED_NO_CONSENT_STYLE));
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.ACTIVATED.name())
                        .setStyle(EXPECTED_ACTIVATED_STYLE));
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.DEACTIVATED.name())
                        .setStyle(EXPECTED_DEACTIVATE_STYLE));
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.MISSING_VALUE.name())
                        .setStyle(EXPECTED_MISSING_STYLE));
    }
}
