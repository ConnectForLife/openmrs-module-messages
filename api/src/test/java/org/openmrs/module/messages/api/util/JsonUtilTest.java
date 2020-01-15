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
            + "    \"backgroundColor\": \"#EEA616\",\n"
            + "    \"textColor\": \"#f5f5f5\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"ACTIVE\",\n"
            + "    \"backgroundColor\": \"#51a351\",\n"
            + "    \"textColor\": \"#f5f5f5\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"DEACTIVATE\",\n"
            + "    \"backgroundColor\": \"#f23722\",\n"
            + "    \"textColor\": \"#f5f5f5\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"name\": \"MISSING_VALUE\",\n"
            + "    \"backgroundColor\": \"#EEA616\",\n"
            + "    \"textColor\": \"#f5f5f5\"\n"
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

    private static final String TEXT_COLOR = "#f5f5f5";

    private static final String NO_CONSENT_BACKGROUND_COLOR = "#EEA616";

    private static final String ACTIVE_BACKGROUND_COLOR = "#51a351";

    private static final String DEACTIVATE_BACKGROUND_COLOR = "#f23722";

    private static final String MISSING_VALUE_BACKGROUND_COLOR = "#EEA616";

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
                        .setBackgroundColor(NO_CONSENT_BACKGROUND_COLOR)
                        .setTextColor(TEXT_COLOR));
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.ACTIVE.name())
                        .setBackgroundColor(ACTIVE_BACKGROUND_COLOR)
                        .setTextColor(TEXT_COLOR));
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.DEACTIVATE.name())
                        .setBackgroundColor(DEACTIVATE_BACKGROUND_COLOR)
                        .setTextColor(TEXT_COLOR));
        EXPECTED_LIST.add(
                new PersonStatusConfigDTO()
                        .setName(PersonStatus.MISSING_VALUE.name())
                        .setBackgroundColor(MISSING_VALUE_BACKGROUND_COLOR)
                        .setTextColor(TEXT_COLOR));
    }
}
