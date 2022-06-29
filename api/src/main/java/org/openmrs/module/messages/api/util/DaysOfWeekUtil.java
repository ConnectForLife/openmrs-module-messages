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

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
        List<DayOfWeek> givenDays = loadDaysOfWeek(days);
        if (isEveryDay(givenDays)) {
            return EVERY_DAY_TEXT;
        }

        if (isEveryWeekday(givenDays)) {
            return EVERY_WEEKDAY_TEXT;
        }

        Collections.sort(givenDays);

        return StringUtils.join(mapToStringArray(givenDays), DAY_NAMES_SEPARATOR);
    }

    private static List<DayOfWeek> loadDaysOfWeek(String[] days) {
        List<DayOfWeek> givenDays = new ArrayList<DayOfWeek>();
        for (String day : days) {
            if (StringUtils.isNotBlank(day) && isParsable(day)) {
                givenDays.add(DayOfWeek.valueOf(day.toUpperCase()));
            }
        }
        return givenDays;
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

    private static String[] mapToStringArray(List<DayOfWeek> givenDays) {
        String[] result = new String[givenDays.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = StringUtils.capitalize(givenDays.get(i).name().toLowerCase());
        }
        return result;
    }

    private static boolean isParsable(String day) {
        try {
            DayOfWeek.valueOf(day.toUpperCase());
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private DaysOfWeekUtil() {
    }
}
