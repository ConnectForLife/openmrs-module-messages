package org.openmrs.module.messages.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;

import java.io.IOException;
import java.util.List;

/**
 * Utility class for testing REST controllers.
 */
public final class TestUtil {

    private static final ObjectMapper MAPPER = createObjectMapper();

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
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

    private TestUtil() {
    }
}
