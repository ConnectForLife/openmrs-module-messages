package org.openmrs.module.messages.api.util.end.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.openmrs.module.messages.api.util.TimeType;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class OtherEndDateTest {

    private static final String INCORRECT_TIME_TYPE_PARAM = "INCORRECT";
    private static final Date NOW = new Date();
    private static final Integer AMOUNT_YEAR = 2;
    private static final Date EXPECTED_END_DATE_YEAR = DateUtils.addYears(NOW, AMOUNT_YEAR);
    private static final Integer AMOUNT_MOTH = 1;
    private static final Date EXPECTED_END_DATE_MOTH = DateUtils.addMonths(NOW, AMOUNT_MOTH);
    private static final Integer AMOUNT_DAY = 16;
    private static final Date EXPECTED_END_DATE_DAY = DateUtils.addDays(NOW, AMOUNT_DAY);

    @Test
    public void shouldReturnNullWhenMissingStartDateParam() {
        Date actual = new OtherEndDate(new EndDateParams(TimeType.MONTH + "|" + 1)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenNullDatabaseValue() {
        Date actual = new OtherEndDate(new EndDateParams(null)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenEmptyDatabaseValue() {
        Date actual = new OtherEndDate(new EndDateParams(StringUtils.EMPTY)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenWrongTimeTypeParam() {
        Date actual = new OtherEndDate(new EndDateParams(INCORRECT_TIME_TYPE_PARAM + "|" + 1)).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenWrongMissingAmount() {
        Date actual = new OtherEndDate(new EndDateParams(TimeType.MONTH.name())).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnNullWhenIncorrectValueOfUnit() {
        Date actual = new OtherEndDate(new EndDateParams(TimeType.MONTH + "|incorrect")).getDate();
        assertThat(actual, is(nullValue()));
    }

    @Test
    public void shouldReturnProperDateForYearOption() {
        EndDateParams params = new EndDateParams(TimeType.YEAR + "|" + AMOUNT_YEAR, NOW);
        Date actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_YEAR));
    }

    @Test
    public void shouldReturnProperDateForMonthOption() {
        EndDateParams params = new EndDateParams(TimeType.MONTH + "|" + AMOUNT_MOTH, NOW);
        Date actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_MOTH));
    }

    @Test
    public void shouldReturnProperDateForDayOption() {
        EndDateParams params = new EndDateParams(TimeType.DAY + "|" + AMOUNT_DAY, NOW);
        Date actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_DAY));
    }

    @Test
    public void shouldReturnProperDateWhenMoreThenNecessaryGroups() {
        EndDateParams params = new EndDateParams(TimeType.DAY + "|" + AMOUNT_DAY + "|"
                + INCORRECT_TIME_TYPE_PARAM, NOW);
        Date actual = new OtherEndDate(params).getDate();
        assertThat(actual, is(EXPECTED_END_DATE_DAY));
    }
}
