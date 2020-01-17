package org.openmrs.module.messages.api.util.end.date.times;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimes;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.joda.time.DateTimeConstants.FRIDAY;
import static org.joda.time.DateTimeConstants.MONDAY;
import static org.joda.time.DateTimeConstants.SATURDAY;
import static org.joda.time.DateTimeConstants.SUNDAY;
import static org.joda.time.DateTimeConstants.THURSDAY;
import static org.joda.time.DateTimeConstants.TUESDAY;
import static org.joda.time.DateTimeConstants.WEDNESDAY;

public class AfterTimesTest {

    private static final int YEAR_2020 = 2020 - 1900; // new Date implementation is x - 1900

    private static final Date SECOND_PART_OF_THE_WEEK_CASE_START = getDateFrom2020(Calendar.JUNE, 1);

    private static final Date FIRST_PART_OF_THE_WEEK_CASE_START = getDateFrom2020(Calendar.JUNE, 4);

    private static final Date MIDDLE_OF_THE_WEEK_CASE_START = getDateFrom2020(Calendar.JANUARY, 20);

    private static final Date SIMPLE_CASE_START = getDateFrom2020(Calendar.JANUARY, 20);
    private static final int SIMPLE_CASE_WEEKS = 7;

    @Test
    public void shouldReturnEndDateForSimpleCase() {
        Set<Integer> frequency = new HashSet<>(Collections.singletonList(MONDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, 2), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithTwoDays() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 3;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithThreeDays() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 4;

        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFourDays() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 5;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFiveDays() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 6;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSixDays() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 7;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseForWholeWeek() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(
                MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithMondayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(
                TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithTuesdayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithWednesdayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithThursdayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithFridayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, SATURDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSaturdayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SUNDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 8;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSimpleCaseWithSundayMissing() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY));
        int occurrences = SIMPLE_CASE_WEEKS * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SIMPLE_CASE_START);
        final int day = 7;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithThreeDays() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 4;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithoutLastWednesday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * frequency.size() - 1;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 3;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForMiddleOfTheWeekCaseWithoutLastWednesdayAndTuesday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * frequency.size() - 2;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, MIDDLE_OF_THE_WEEK_CASE_START);
        final int day = 2;
        Assert.assertEquals(getDateFrom2020(Calendar.MARCH, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCase() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 19;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSunday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * frequency.size() - 1;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 18;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * frequency.size() - 2;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 17;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturdayAndFriday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        final int threeDays = 3;
        int occurrences = 7 * frequency.size() - threeDays;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 16;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForSecondPartTheWeekCaseWithoutLastSundayAndSaturdayAndFridayAndThursday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(THURSDAY, FRIDAY, SATURDAY, SUNDAY));
        int occurrences = 7 * frequency.size() - frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, SECOND_PART_OF_THE_WEEK_CASE_START);
        final int day = 12;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCase() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * frequency.size();
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 22;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * frequency.size() - 1;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 21;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesdayAndTuesday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        int occurrences = 7 * frequency.size() - 2;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 20;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    @Test
    public void shouldReturnEndDateForFirstPartTheWeekCaseWithoutLastWednesdayAndTuesdayAndMonday() {
        Set<Integer> frequency = new HashSet<>(Arrays.asList(MONDAY, TUESDAY, WEDNESDAY));
        final int threeDays = 3;
        int occurrences = 7 * frequency.size() - threeDays;
        AfterTimes afterTimes = new AfterTimes(frequency, occurrences, FIRST_PART_OF_THE_WEEK_CASE_START);
        final int day = 15;
        Assert.assertEquals(getDateFrom2020(Calendar.JULY, day), afterTimes.get());
    }

    private static Date getDateFrom2020(int month, int day) {
        return new Date(YEAR_2020, month, day, 0, 0, 0);
    }
}
