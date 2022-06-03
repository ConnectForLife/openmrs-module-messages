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

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.module.SimpleModule;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;

import java.io.IOException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.requireNonNull;

/**
 * Utilities related to ScheduledExecutionContext.
 * <p>
 * <b>Impl Note</b>: The JSON serialization/deserialization methods use {@link DateTimeFormatter#RFC_1123_DATE_TIME} to
 * handle execution date-time (and any other Instant). It was selected to be backwards compatible with
 * ScheduledExecutionContext created when a Date.class was used to store execution date.
 * </p>
 */
public final class ScheduledExecutionContextUtil {

    /**
     * The date-time pattern used to serialize objects of Instant.class (execution date) by this utils.
     * <p><b>Note:</b> The pattern was selected to make the serialization/deserialization to JSON backwards compatible
     * with contexts serialized when a Date.class was used to store execution date.</p>
     */
    public static final String DATE_TIME_PATTERN = "EEEE',' MMMM d',' yyyy h:mm:ss a zzz";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        final SimpleModule scheduledExecutionContextModule =
                new SimpleModule("cfl.scheduledExecutionContext", Version.unknownVersion());

        scheduledExecutionContextModule.addSerializer(Instant.class, new JsonSerializer<Instant>() {
            @Override
            public void serialize(Instant instant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString(instant
                        .atZone(DateUtil.getDefaultSystemTimeZone())
                        .format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)));
            }
        });

        scheduledExecutionContextModule.addDeserializer(Instant.class, new JsonDeserializer<Instant>() {
            @Override
            public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
                    throws IOException {
                return ZonedDateTime.parse(jsonParser.getText(), DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)).toInstant();
            }
        });

        OBJECT_MAPPER.registerModule(scheduledExecutionContextModule);
    }

    private ScheduledExecutionContextUtil() {
    }

    /**
     * Converts {@code context} to JSON String.
     *
     * @param context the instance to convert to JSON, not null
     * @return the JSON, never null
     * @throws MessagesRuntimeException if conversion failed
     */
    public static String toJson(ScheduledExecutionContext context) {
        requireNonNull(context);
        try {
            return OBJECT_MAPPER.writeValueAsString(context);
        } catch (IOException e) {
            throw new MessagesRuntimeException("Failed to write ScheduledExecutionContext to JSON!", e);
        }
    }

    /**
     * Converts {@code json} JSON String to new instance of ScheduledExecutionContext.
     *
     * @param json the String to read from, not null
     * @return the ScheduledExecutionContext, never null
     * @throws MessagesRuntimeException if conversion failed
     */
    public static ScheduledExecutionContext fromJson(String json) {
        requireNonNull(json);
        try {
            return OBJECT_MAPPER.readValue(json, ScheduledExecutionContext.class);
        } catch (IOException e) {
            throw new MessagesRuntimeException("Failed to read ScheduledExecutionContext from JSON! Content: " + json, e);
        }
    }
}
