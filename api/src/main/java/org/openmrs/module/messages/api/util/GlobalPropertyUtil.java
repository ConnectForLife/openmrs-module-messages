package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GlobalPropertyUtil {

    private static final char ENTRY_SEPARATOR = ',';
    private static final char KEY_VALUE_SEPARATOR = ':';
    private static final int PAIR_SIZE = 2;

    private static final String TRUE = "true";

    public static boolean parseBool(String value) {
        return StringUtils.equalsIgnoreCase(TRUE, value);
    }

    public static int parseInt(String name, String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new MessagesRuntimeException(
                    String.format("Cannot parse the global property %s=%s. Expected int value.", name, value),
                    ex
            );
        }
    }

    public static Map<String, String> parseMap(String name, String value) {
        String[] entries = StringUtils.split(value, ENTRY_SEPARATOR);
        if (entries == null) {
            return new HashMap<>();
        }

        HashMap<String, String> result = new HashMap<>();
        for (String entry : entries) {
            String[] keyValue = StringUtils.split(entry, KEY_VALUE_SEPARATOR);
            if (keyValue.length != PAIR_SIZE) {
                throw new MessagesRuntimeException(String.format(
                        "Cannot parse the global property %s=%s. Expected serialized map value.",
                        name, value
                ));
            }
            result.put(keyValue[0].toUpperCase(), keyValue[1]);
        }
        return result;
    }

    public static List<String> parseList(String value, String delimiter) {
        if (StringUtils.isNotBlank(delimiter) && value.contains(delimiter)) {
            return Arrays.asList(value.split(delimiter));
        }
        return Collections.singletonList(value);
    }

    private GlobalPropertyUtil() {
    }
}
