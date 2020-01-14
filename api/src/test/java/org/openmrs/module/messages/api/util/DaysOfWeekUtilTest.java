package org.openmrs.module.messages.api.util;

import static org.openmrs.module.messages.api.util.DaysOfWeekUtil.EVERY_DAY_TEXT;
import static org.openmrs.module.messages.api.util.DaysOfWeekUtil.EVERY_WEEKDAY_TEXT;

import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class DaysOfWeekUtilTest {

    private static final String TEST_EVERY_DAY = "Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday";
    private static final String TEST_EVERY_WEEKDAY = "Monday,Tuesday,Wednesday,Thursday,Friday";
    private static final String TEST_EVERY_DAY_MIXED = "Wednesday,Thursday,Tuesday,Friday,Saturday,Sunday,Monday";
    private static final String TEST_EVERY_WEEKDAY_MIXED = "Tuesday,Wednesday,Thursday,Friday,Monday";
    private static final String TEST_ONE_DAY = "Monday";
    private static final String TEST_TWO_DAYS = "Tuesday,Friday";

    @Test
    public void shouldGenerateEveryDayText() {
        String allDaysOfWeek = TEST_EVERY_DAY;
        String[] days = StringUtils.split(allDaysOfWeek, ",");
        String actual = DaysOfWeekUtil.generateDayOfWeekText(days);

        Assert.assertEquals(EVERY_DAY_TEXT, actual);
    }

    @Test
    public void shouldGenerateEveryWeekDayText() {
        String allDaysOfWeek = TEST_EVERY_WEEKDAY;
        String[] days = StringUtils.split(allDaysOfWeek, ",");
        String actual = DaysOfWeekUtil.generateDayOfWeekText(days);

        Assert.assertEquals(EVERY_WEEKDAY_TEXT, actual);
    }

    @Test
    public void shouldGenerateEveryDayTextForMixedOrder() {
        String allDaysOfWeek = TEST_EVERY_DAY_MIXED;
        String[] days = StringUtils.split(allDaysOfWeek, ",");
        String actual = DaysOfWeekUtil.generateDayOfWeekText(days);

        Assert.assertEquals(EVERY_DAY_TEXT, actual);
    }

    @Test
    public void shouldGenerateEveryWeekDayTextForMixedOrder() {
        String allDaysOfWeek = TEST_EVERY_WEEKDAY_MIXED;
        String[] days = StringUtils.split(allDaysOfWeek, ",");
        String actual = DaysOfWeekUtil.generateDayOfWeekText(days);

        Assert.assertEquals(EVERY_WEEKDAY_TEXT, actual);
    }

    @Test
    public void shouldGenerateOneDayForOneDay() {
        String allDaysOfWeek = TEST_ONE_DAY;
        String[] days = StringUtils.split(allDaysOfWeek, ",");
        String actual = DaysOfWeekUtil.generateDayOfWeekText(days);

        Assert.assertEquals("Monday", actual);
    }

    @Test
    public void shouldGenerateTwoDaysForTwoDays() {
        String allDaysOfWeek = TEST_TWO_DAYS;
        String[] days = StringUtils.split(allDaysOfWeek, ",");
        String actual = DaysOfWeekUtil.generateDayOfWeekText(days);

        Assert.assertEquals("Tuesday, Friday", actual);
    }
}
