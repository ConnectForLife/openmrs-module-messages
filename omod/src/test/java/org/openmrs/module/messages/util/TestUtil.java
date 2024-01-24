/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.openmrs.Relationship;
import org.openmrs.api.context.Context;

import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

import static org.openmrs.module.messages.api.constants.ConfigConstants.ACTOR_TYPES_KEY;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtil {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    /**
     * Convert an object to JSON byte array.
     *
     * @param object the object to convert.
     * @return the JSON byte array.
     * @throws IOException
     */
    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        return MAPPER.writeValueAsBytes(object);
    }

    public static String writeValueAsString(Object object) throws IOException {
        return MAPPER.writeValueAsString(object);
    }

    public static <T> List<T> readValueAsList(String string, TypeReference<List<T>> typeReference) throws IOException {
        return MAPPER.readValue(string, typeReference);
    }

    public static <T> T readValue(String string, Class<T> clazz) throws IOException {
        return MAPPER.readValue(string, clazz);
    }

    public static void loadSystemRelationshipsToActorTypes() {
        Context.getAdministrationService()
                .setGlobalProperty(ACTOR_TYPES_KEY, extractRelationshipTypeUuidsToArray());
    }

    private static String extractRelationshipTypeUuidsToArray() {
        StringJoiner joiner = new StringJoiner(",");
        for (Relationship r : Context.getPersonService().getAllRelationships()) {
            String uuid = r.getRelationshipType().getUuid();
            joiner.add(uuid);
        }
        return joiner.toString();
    }

    private TestUtil() {
    }
}
