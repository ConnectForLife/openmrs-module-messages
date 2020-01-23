package org.openmrs.module.messages;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public final class ApiConstant {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json",
            StandardCharsets.UTF_8);

    public static final String ROWS_PARAM = "rows";

    public static final String PAGE_PARAM = "page";

    public static final String CAREGIVER_RELATIONSHIP = "Caregiver";

    private ApiConstant() { }
}
