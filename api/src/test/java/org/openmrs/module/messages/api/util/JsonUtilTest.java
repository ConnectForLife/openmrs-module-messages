package org.openmrs.module.messages.api.util;

import com.google.gson.reflect.TypeToken;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.model.PersonStatus;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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

    private static final List<PersonStatusConfigDTO> EXPECTED_LIST = new ArrayList<>();

    private static final String TEXT_COLOR = "#f5f5f5";

    private static final String NO_CONSENT_BACKGROUND_COLOR = "#EEA616";

    private static final String ACTIVE_BACKGROUND_COLOR = "#51a351";

    private static final String DEACTIVATE_BACKGROUND_COLOR = "#f23722";

    private static final String MISSING_VALUE_BACKGROUND_COLOR = "#EEA616";

    @Before
    public void setUp() {
        buildExpectedList();
    }

    @Test
    public void listFromJson() {
        List<PersonStatusConfigDTO> actual = JsonUtil.listFromJson(STRING_LIST,
                new TypeToken<ArrayList<PersonStatusConfigDTO>>() { });
        assertThat(actual, is(EXPECTED_LIST));
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