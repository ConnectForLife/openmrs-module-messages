package org.openmrs.module.messages.api.util;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EndDateUtilTest {

    private static final int YEAR_2020 = 2020;
    private static final int YEAR_2022 = 2022;
    private static final int DAY_15 = 15;
    private static final int DAY_30 = 30;
    private static final Date EXPECTED = createDate(YEAR_2020, Calendar.JANUARY, 8);
    private static final String SEPARATOR = "|";
    private static final String OTHER_TEMPLATE = "Other";

    @Test
    public void shouldParseAfterTimesDateForDayOfWeek() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Tuesday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-06-01"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.AFTER_TIMES.getName() + "|2"));
        Date endDate = createDate(YEAR_2020, Calendar.JUNE, 2);

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldParseAfterTimesDate() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Tuesday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-06-01"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.AFTER_TIMES.getName() + "|2"));
        Date endDate = createDate(YEAR_2020, Calendar.JUNE, 2);

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldGetValidDatePickerEndDate() {
        final Date expected = createDate(YEAR_2020, Calendar.JANUARY, 10);
        Date result = EndDateUtil.getEndDate(getValidDatePickerValues(), OTHER_TEMPLATE);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldParseAfterTimesInvalidValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.AFTER_TIMES.getName() + "|2020-sda01---|--08"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseAfterTimesDateEmptyWithSeparator() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.AFTER_TIMES.getName() + "|"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseAfterTimesDateType() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.AFTER_TIMES.getName()));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDate() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.DATE_PICKER.getName() + "|2020-01-08"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(EXPECTED, result);
    }

    @Test
    public void shouldParseDatePickerInvalidValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.DATE_PICKER.getName() + "|2020-01-----08"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateEmptyWithSeparator() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.DATE_PICKER.getName() + "|"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateType() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.DATE_PICKER.getName()));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateInvalidValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.NO_DATE.getName() + "|2020---091---|08"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparatorAndConstant() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.NO_DATE.getName() + "|EMPTY"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparator() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.NO_DATE.getName() + "|"));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDate() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.NO_DATE.getName()));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateBlank() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, ""));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateNull() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, null));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldBuildEndDateTextForDate() {
        String dateString = "2020-01-08";
        String result = EndDateUtil.getEndDateText(dateString);

        Assert.assertEquals(dateString, result);
    }

    @Test
    public void shouldBuildEndDateTextForNonDateText() {
        String dateString = "tomorrow";
        String result = EndDateUtil.getEndDateText(dateString);

        Assert.assertEquals(dateString, result);
    }

    @Test
    public void shouldBuildEndDateTextForAfterTimesPhraseText() {
        String dateString = "AFTER_TIMES|5";
        String result = EndDateUtil.getEndDateText(dateString);

        Assert.assertEquals("after 5 time(s)", result);
    }

    @Test
    public void shouldBuildEndDateTextForNoDatePhraseText() {
        String dateString = "NO_DATE|EMPTY";
        String result = EndDateUtil.getEndDateText(dateString);

        Assert.assertEquals("never", result);
    }

    @Test
    public void shouldBuildBlankEndDateTextForNull() {
        String result = EndDateUtil.getEndDateText(null);

        Assert.assertEquals("", result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimes() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));
        final Date endDate = createDate(YEAR_2020, Calendar.JANUARY, 20);
        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimesWhenFrequencyHasSeparatorAtTheEnd() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday,"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));

        final Date endDate = createDate(YEAR_2020, Calendar.JANUARY, 20);
        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimesWhenFrequencyHasSeparatorAtTheBeginning() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, ",Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));

        final Date endDate = createDate(YEAR_2020, Calendar.JANUARY, 20);
        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimesWhenFrequencyHasEmptyValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, ",Monday,,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));
        final Date endDate = createDate(2020, Calendar.JANUARY, 20);
        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateAfterMonths() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + TimeType.MONTH + SEPARATOR + 2));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(createDate(YEAR_2020, Calendar.MARCH, DAY_15), result);
    }

    @Test
    public void shouldReturnEndDateAfterDays() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + TimeType.DAY + SEPARATOR + DAY_15));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(createDate(YEAR_2020, Calendar.JANUARY, DAY_30), result);
    }

    @Test
    public void shouldReturnEndDateAfterYears() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + TimeType.YEAR + SEPARATOR + 2));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(createDate(YEAR_2022, Calendar.JANUARY, DAY_15), result);
    }

    @Test
    public void shouldReturnNullTimeTypeBlank() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + SEPARATOR + 2));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldReturnNullUnitsBlank() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + TimeType.MONTH + SEPARATOR));

        Date result = EndDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    private List<TemplateFieldValue> getValidDatePickerValues() {
        List<TemplateFieldValue> values = new ArrayList<>();

        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Tuesday,Wednesday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.DATE_PICKER.getName() + "|2020-01-10"));
        return values;
    }

    private TemplateFieldValue buildTemplateFieldWithValue(TemplateFieldType type, String value) {
        return new TemplateFieldValueBuilder()
                .withTemplateField(
                        new TemplateFieldBuilder()
                                .withTemplateFieldType(type)
                                .build())
                .withValue(value)
                .build();
    }

    private static Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
