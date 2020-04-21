package org.openmrs.module.messages.api.util.end.date.after.times;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.util.FrequencyType;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.joda.time.DateTimeConstants.MONDAY;
import static org.joda.time.DateTimeConstants.TUESDAY;
import static org.joda.time.DateTimeConstants.WEDNESDAY;
import static org.joda.time.DateTimeConstants.THURSDAY;
import static org.joda.time.DateTimeConstants.FRIDAY;
import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.SUNDAY;

public class AfterTimesTest {

    private static final int YEAR_2020 = 2020 - 1900; // new Date implementation is x - 1900

    private static final Date SECOND_PART_OF_THE_WEEK_CASE_START = getDateFrom2020(Calendar.JUNE,
        1);

    private static final Date FIRST_PART_OF_THE_WEEK_CASE_START = getDateFrom2020(Calendar.JUNE, 4);

    private static final Date MIDDLE_OF_THE_WEEK_CASE_START = getDateFrom2020(Calendar.JANUARY, 20);

    private static final Date SIMPLE_CASE_START_MONDAY = getDateFrom2020(Calendar.JANUARY, 20);
    private static final Date SIMPLE_CASE_START_TUESDAY = getDateFrom2020(Calendar.JANUARY, 21);
    private static final Date LAST_DAY_OF_MONTH_START = getDateFrom2020(Calendar.JANUARY, 31);
    private static final Date FIRST_DAY_OF_MONTH_START = getDateFrom2020(Calendar.FEBRUARY, 1);
    private static final Date MISSED_FIRST_WEEKDAY_IN_MONTH_START = getDateFrom2020(Calendar.FEBRUARY, 3);
    private static final int SIMPLE_CASE_WEEKS = 7;
    private static final int THREE_OCCURRENCES = 3;
    private static final int THREE_DAYS = 3;
    private static final int FOUR_DAYS = 4;
    private static final int EXPECTED_DAY_COUNT = 27;

    @Test
    public void shouldReturnEndDateForSimpleCase() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 2), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithTwoDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 3;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithThreeDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 4;

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFourDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 5;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFiveDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
            FRIDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 6;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSixDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
            FRIDAY, SATURDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 7;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseForWholeWeek() {
        Set<Integer> days = new HashSet<>(Arrays.asList(
            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithMondayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(
            TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithTuesdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, WEDNESDAY, THURSDAY, FRIDAY,
            SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithWednesdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, THURSDAY, FRIDAY,
            SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithThursdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, FRIDAY,
            SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFridayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
            SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSaturdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
            FRIDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSundayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY,
            FRIDAY, SATURDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
        final int day = 7;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithThreeDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 4;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithoutLastWednesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 3;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithoutLastWednesdayAndTuesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 2;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCase() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 19;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSunday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size() - 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 18;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size() - 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 17;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturdayAndFriday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        final int threeDays = 3;
        int occurrences = 7 * days.size() - threeDays;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 16;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturdayAndFridayAndThursday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size() - days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 12;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCase() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 22;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 21;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesdayAndTuesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 20;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesdayAndTuesdayAndMonday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        final int threeDays = 3;
        int occurrences = 7 * days.size() - threeDays;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences,
            FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 15;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnProperMondayForDailyPeriodForOneDay() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, THREE_OCCURRENCES,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.FEBRUARY, THREE_DAYS), afterTimes.get());
    }

    @Test
    public void shouldReturnProperMondayForDailyPeriodForTwoDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, THREE_OCCURRENCES,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.JANUARY, EXPECTED_DAY_COUNT), afterTimes.get());
    }

    @Test
    public void shouldReturnProperMondayForWeeklyPeriod() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.WEEKLY, days, THREE_OCCURRENCES,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.FEBRUARY, THREE_DAYS), afterTimes.get());
    }

    @Test
    public void shouldReturnProperProperTuesdayForWeeklyPeriod() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(TUESDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.WEEKLY, days, THREE_OCCURRENCES,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.FEBRUARY, FOUR_DAYS), afterTimes.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfMultipleDaysForWeekly() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = 2;
        new AfterTimes(FrequencyType.WEEKLY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoOccurrencesPlanned() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = 0;
        new AfterTimes(FrequencyType.WEEKLY, days, occurrences,
                SIMPLE_CASE_START_MONDAY);
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodForMonday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 2), afterTimes.get());
    }

    @Test
    public void shouldReturnProperTuesdayForMonthlyPeriodForMonday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(TUESDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, THREE_DAYS), afterTimes.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfMultipleDaysForMonthly() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = 2;
        new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
            SIMPLE_CASE_START_MONDAY);

    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodForSunday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SUNDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
            SIMPLE_CASE_START_TUESDAY);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 1), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayIfLastDayOfMonthWasSet() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
            LAST_DAY_OF_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 2), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodBeforeFirstWeekday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SUNDAY));
        int occurrences = 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
                FIRST_DAY_OF_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Calendar.FEBRUARY, 2), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodWhenMissedFirstWeekDayInMonth() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SUNDAY));
        int occurrences = 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
                MISSED_FIRST_WEEKDAY_IN_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 1), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodWhenStartsAtFirstWeekday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SATURDAY));
        int occurrences = 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences,
                FIRST_DAY_OF_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 7), afterTimes.get());
    }

    private static Date getDateFrom2020(int month, int day) {
        return new Date(YEAR_2020, month, day, 0, 0, 0);
    }
}
