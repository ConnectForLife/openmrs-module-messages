package org.openmrs.module.messages.api.util;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public final class DaysOfWeekUtil {

    public static final String EVERY_DAY_TEXT = "Every day";
    public static final String EVERY_WEEKDAY_TEXT = "Every weekday";
    public static final String DAY_NAMES_SEPARATOR = ", ";

    public static final List<DayOfWeek> WEEKEND_DAYS = Arrays.asList(DayOfWeek.SATURDAY,
        DayOfWeek.SUNDAY);
    public static final List<DayOfWeek> EVERY_DAY_OF_WEEK = loadEveryDaysOfWeek();
    public static final List<DayOfWeek> EVERY_WEEKDAY_OF_WEEK = loadWeekDaysOfWeek();

    public static String generateDayOfWeekText(String[] days) {
        List<DayOfWeek> givenDays = new ArrayList<DayOfWeek>();
        for (String day : days) {
            givenDays.add(DayOfWeek.valueOf(day.toUpperCase()));
        }
        if (isEveryDay(givenDays)) {
            return EVERY_DAY_TEXT;
        }

        if (isEveryWeekday(givenDays)) {
            return EVERY_WEEKDAY_TEXT;
        }
        return StringUtils.join(days, DAY_NAMES_SEPARATOR);
    }

    private static boolean isEveryDay(List<DayOfWeek> givenDays) {
        if (givenDays.size() != EVERY_DAY_OF_WEEK.size()) {
            return false;
        }
        for (DayOfWeek dayOfWeek : EVERY_DAY_OF_WEEK) {
            if (!givenDays.contains(dayOfWeek)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isEveryWeekday(List<DayOfWeek> givenDays) {
        if (givenDays.size() != EVERY_WEEKDAY_OF_WEEK.size()) {
            return false;
        }
        for (DayOfWeek dayOfWeek : EVERY_WEEKDAY_OF_WEEK) {
            if (!givenDays.contains(dayOfWeek)) {
                return false;
            }
        }
        return true;
    }

    private static List<DayOfWeek> loadEveryDaysOfWeek() {
        return Arrays.asList(DayOfWeek.values());
    }

    private static List<DayOfWeek> loadWeekDaysOfWeek() {
        List<DayOfWeek> weekdays =
            new ArrayList<DayOfWeek>(loadEveryDaysOfWeek());
        weekdays.removeAll(WEEKEND_DAYS);
        return weekdays;
    }

    private DaysOfWeekUtil() {
    }
}
