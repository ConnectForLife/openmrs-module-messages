package org.openmrs.module.messages.api.util.end.date.after.times;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.util.FrequencyType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AfterTimesTest {

    private static final int YEAR_2020 = 2020;

    private static final ZonedDateTime SECOND_PART_OF_THE_WEEK_CASE_START = getDateFrom2020(Month.JUNE.getValue(), 1);
    private static final ZonedDateTime FIRST_PART_OF_THE_WEEK_CASE_START = getDateFrom2020(Month.JUNE.getValue(), 4);
    private static final ZonedDateTime MIDDLE_OF_THE_WEEK_CASE_START = getDateFrom2020(Month.JANUARY.getValue(), 20);
    private static final ZonedDateTime SIMPLE_CASE_START_MONDAY = getDateFrom2020(Month.JANUARY.getValue(), 20);
    private static final ZonedDateTime SIMPLE_CASE_START_TUESDAY = getDateFrom2020(Month.JANUARY.getValue(), 21);
    private static final ZonedDateTime LAST_DAY_OF_MONTH_START = getDateFrom2020(Month.JANUARY.getValue(), 31);
    private static final ZonedDateTime FIRST_DAY_OF_MONTH_START = getDateFrom2020(Month.FEBRUARY.getValue(), 1);
    private static final ZonedDateTime MISSED_FIRST_WEEKDAY_IN_MONTH_START = getDateFrom2020(Month.FEBRUARY.getValue(), 3);

    private static final int SIMPLE_CASE_WEEKS = 7;
    private static final int THREE_OCCURRENCES = 3;
    private static final int THREE_DAYS = 3;
    private static final int FOUR_DAYS = 4;
    private static final int EXPECTED_DAY_COUNT = 27;

    private static final int FRIDAY = DayOfWeek.FRIDAY.getValue();
    private static final int MONDAY = DayOfWeek.MONDAY.getValue();
    private static final int SATURDAY = DayOfWeek.SATURDAY.getValue();
    private static final int SUNDAY = DayOfWeek.SUNDAY.getValue();
    private static final int THURSDAY = DayOfWeek.THURSDAY.getValue();
    private static final int TUESDAY = DayOfWeek.TUESDAY.getValue();
    private static final int WEDNESDAY = DayOfWeek.WEDNESDAY.getValue();

    private static ZonedDateTime getDateFrom2020(int month, int day) {
        return ZonedDateTime.of(LocalDate.of(YEAR_2020, month, day), LocalTime.MIDNIGHT, ZoneId.of("UTC"));
    }

    @Test
    public void shouldReturnEndDateForSimpleCase() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), 2), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithTwoDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 3;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithThreeDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 4;

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFourDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 5;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFiveDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 6;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSixDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 7;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseForWholeWeek() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithMondayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithTuesdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithWednesdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithThursdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFridayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSaturdayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSundayMissing() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY));
        int occurrences = SIMPLE_CASE_WEEKS * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SIMPLE_CASE_START_MONDAY);
        final int day = 7;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithThreeDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 4;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithoutLastWednesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 3;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithoutLastWednesdayAndTuesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 2;
        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCase() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 19;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSunday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size() - 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 18;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size() - 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 17;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturdayAndFriday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        final int threeDays = 3;
        int occurrences = 7 * days.size() - threeDays;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 16;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturdayAndFridayAndThursday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * days.size() - days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 12;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCase() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size();
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 22;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 21;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesdayAndTuesday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * days.size() - 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 20;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesdayAndTuesdayAndMonday() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        final int threeDays = 3;
        int occurrences = 7 * days.size() - threeDays;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 15;
        Assert.assertEquals(getDateFrom2020(Month.JULY.getValue(), day), afterTimes.get());
    }

    @Test
    public void shouldReturnProperMondayForDailyPeriodForOneDay() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, THREE_OCCURRENCES, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.FEBRUARY.getValue(), THREE_DAYS), afterTimes.get());
    }

    @Test
    public void shouldReturnProperMondayForDailyPeriodForTwoDays() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.DAILY, days, THREE_OCCURRENCES, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.JANUARY.getValue(), EXPECTED_DAY_COUNT), afterTimes.get());
    }

    @Test
    public void shouldReturnProperMondayForWeeklyPeriod() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.WEEKLY, days, THREE_OCCURRENCES, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.FEBRUARY.getValue(), THREE_DAYS), afterTimes.get());
    }

    @Test
    public void shouldReturnProperProperTuesdayForWeeklyPeriod() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(TUESDAY));
        AfterTimes afterTimes = new AfterTimes(FrequencyType.WEEKLY, days, THREE_OCCURRENCES, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.FEBRUARY.getValue(), FOUR_DAYS), afterTimes.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfMultipleDaysForWeekly() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = 2;
        new AfterTimes(FrequencyType.WEEKLY, days, occurrences, SIMPLE_CASE_START_MONDAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfNoOccurrencesPlanned() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = 0;
        new AfterTimes(FrequencyType.WEEKLY, days, occurrences, SIMPLE_CASE_START_MONDAY);
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodForMonday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), 2), afterTimes.get());
    }

    @Test
    public void shouldReturnProperTuesdayForMonthlyPeriodForMonday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(TUESDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences, SIMPLE_CASE_START_MONDAY);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), THREE_DAYS), afterTimes.get());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfMultipleDaysForMonthly() {
        Set<Integer> days = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = 2;
        new AfterTimes(FrequencyType.MONTHLY, days, occurrences, SIMPLE_CASE_START_MONDAY);

    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodForSunday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SUNDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences, SIMPLE_CASE_START_TUESDAY);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), 1), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayIfLastDayOfMonthWasSet() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = 2;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences, LAST_DAY_OF_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), 2), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodBeforeFirstWeekday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SUNDAY));
        int occurrences = 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences, FIRST_DAY_OF_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Month.FEBRUARY.getValue(), 2), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodWhenMissedFirstWeekDayInMonth() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SUNDAY));
        int occurrences = 1;
        AfterTimes afterTimes =
                new AfterTimes(FrequencyType.MONTHLY, days, occurrences, MISSED_FIRST_WEEKDAY_IN_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), 1), afterTimes.get());
    }

    @Test
    public void shouldReturnProperDayForMonthlyPeriodWhenStartsAtFirstWeekday() {
        Set<Integer> days = new HashSet<>(Collections.singletonList(SATURDAY));
        int occurrences = 1;
        AfterTimes afterTimes = new AfterTimes(FrequencyType.MONTHLY, days, occurrences, FIRST_DAY_OF_MONTH_START);

        Assert.assertEquals(getDateFrom2020(Month.MARCH.getValue(), 7), afterTimes.get());
    }
}
