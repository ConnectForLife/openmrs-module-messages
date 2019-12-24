package org.openmrs.module.messages.api.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

public final class MapperUtil {

    public static Gson getGson() {
        return new GsonBuilder().setDateFormat(DateFormat.FULL, DateFormat.FULL).create();
    }

    private MapperUtil() {
    }
}
