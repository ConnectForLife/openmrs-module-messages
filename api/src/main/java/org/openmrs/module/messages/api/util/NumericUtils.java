package org.openmrs.module.messages.api.util;

public final class NumericUtils {

    public static boolean isPositive(Integer number) {
        return number != null && number > 0;
    }

    private NumericUtils() { }
}
