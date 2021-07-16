package org.openmrs.module.messages.api.util.end.date;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.TimeType;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class OtherEndDateTest {

    private static final String INCORRECT_TIME_TYPE_PARAM = "INCORRECT";
    private static final ZonedDateTime TEST_NOW =
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(1625756392L), ZoneId.of("UTC"));
    private static final Integer AMOUNT_YEAR = 2;
    private static final ZonedDateTime EXPECTED_END_DATE_YEAR = TEST_NOW.plusYears(AMOUNT_YEAR);
    private static final Integer AMOUNT_MOTH = 1;
    private static final ZonedDateTime EXPECTED_END_DATE_MOTH = TEST_NOW.plusMonths(AMOUNT_MOTH);
    private static final Integer AMOUNT_DAY = 16;
    private static final ZonedDateTime EXPECTED_END_DATE_DAY = TEST_NOW.plusDays(AMOUNT_DAY);

    @Test
    public void shouldReturnNullWhenMissingStartDateParam() {
        ZonedDateTime actual = new OtherEndDate(new EndDateParams(TimeType.MONTH + "|" + 1)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenNullDatabaseValue() {
        ZonedDateTime actual = new OtherEndDate(new EndDateParams(null)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenEmptyDatabaseValue() {
        ZonedDateTime actual = new OtherEndDate(new EndDateParams(StringUtils.EMPTY)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenWrongTimeTypeParam() {
        ZonedDateTime actual = new OtherEndDate(new EndDateParams(INCORRECT_TIME_TYPE_PARAM + "|" + 1)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenWrongMissingAmount() {
        ZonedDateTime actual = new OtherEndDate(new EndDateParams(TimeType.MONTH.name())).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenIncorrectValueOfUnit() {
        ZonedDateTime actual = new OtherEndDate(new EndDateParams(TimeType.MONTH + "|incorrect")).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnProperDateForYearOption() {
        EndDateParams params = new EndDateParams(TimeType.YEAR + "|" + AMOUNT_YEAR, TEST_NOW);
        ZonedDateTime actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_YEAR));
    }

    @Test
    public void shouldReturnProperDateForMonthOption() {
        EndDateParams params = new EndDateParams(TimeType.MONTH + "|" + AMOUNT_MOTH, TEST_NOW);
        ZonedDateTime actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_MOTH));
    }

    @Test
    public void shouldReturnProperDateForDayOption() {
        EndDateParams params = new EndDateParams(TimeType.DAY + "|" + AMOUNT_DAY, TEST_NOW);
        ZonedDateTime actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_DAY));
    }

    @Test
    public void shouldReturnProperDateWhenMoreThenNecessaryGroups() {
        EndDateParams params = new EndDateParams(TimeType.DAY + "|" + AMOUNT_DAY + "|" + INCORRECT_TIME_TYPE_PARAM, TEST_NOW);
        ZonedDateTime actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_DAY));
    }
}
