package org.openmrs.module.messages;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;

public final class Constant {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json",
            StandardCharsets.UTF_8);

    private Constant() { }
}
