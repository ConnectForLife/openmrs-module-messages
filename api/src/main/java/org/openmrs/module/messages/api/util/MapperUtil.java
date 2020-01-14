package org.openmrs.module.messages.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;

public final class MapperUtil {

    private static final Type STRING_TO_STRING_MAP = new TypeToken<Map<String, String>>() { }.getType();

    public static Gson getGson() {
        return new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    }

    public static Map<String, String> getAsStringToStringMap(String source) {
        if (source == null) {
            return new HashMap<>();
        }
        return getGson().fromJson(source, STRING_TO_STRING_MAP);
    }

    private MapperUtil() {
    }
}
