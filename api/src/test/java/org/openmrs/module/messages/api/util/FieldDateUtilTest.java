/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.messages.TestUtil.getMaxTimeForDate;
import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;

public class FieldDateUtilTest {

    private static final ZoneId TEST_TZ = DateUtil.getDefaultSystemTimeZone();
    private static final int YEAR_2020 = 2020;
    private static final int YEAR_2022 = 2022;
    private static final int DAY_15 = 15;
    private static final int DAY_30 = 30;
    private static final ZonedDateTime EXPECTED = getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), 8, TEST_TZ);
    private static final String SEPARATOR = "|";
    private static final String OTHER_TEMPLATE = "Other";
    private static final int FIFTH_DAY_OF_MONTH = 5;

    @Test
    public void shouldParseAfterTimesDateForDayOfWeek() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Tuesday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-06-01"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|2"));
        ZonedDateTime endDate = getMaxTimeForDate(YEAR_2020, Month.JUNE.getValue(), 2, TEST_TZ);

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldParseAfterTimesDate() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Tuesday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-06-01"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|2"));
        ZonedDateTime endDate = getMaxTimeForDate(YEAR_2020, Month.JUNE.getValue(), 2, TEST_TZ);

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldGetValidDatePickerEndDate() {
        final ZonedDateTime expected = getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), 10, TEST_TZ);
        ZonedDateTime result = FieldDateUtil.getEndDate(getValidDatePickerValues(), OTHER_TEMPLATE);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldParseAfterTimesInvalidValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|2020-sda01---|--08"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseAfterTimesDateEmptyWithSeparator() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseAfterTimesDateType() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName()));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDate() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.DATE_PICKER.getName() + "|2020-01-08"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(EXPECTED, result);
    }

    @Test
    public void shouldParseDatePickerInvalidValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.DATE_PICKER.getName() + "|2020-01-----08"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateEmptyWithSeparator() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.DATE_PICKER.getName() + "|"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateType() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.DATE_PICKER.getName()));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateInvalidValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.NO_DATE.getName() + "|2020---091---|08"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparatorAndConstant() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.NO_DATE.getName() + "|EMPTY"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparator() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.NO_DATE.getName() + "|"));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDate() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.NO_DATE.getName()));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWhereExistAnotherDefaultValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValueAndDefaultValue(END_OF_MESSAGES, EndDateType.NO_DATE.getName() + "|EMPTY",
                "AFTER_TIMES|5"));

        ZonedDateTime actual = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(actual);
    }

    @Test
    public void shouldParseNoDateFromDefaultValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValueAndDefaultValue(END_OF_MESSAGES, null, "AFTER_TIMES|5"));
        ZonedDateTime expected = getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), FIFTH_DAY_OF_MONTH, TEST_TZ);
        ZonedDateTime actual = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldParseNoDateBlank() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, ""));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateNull() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, null));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldBuildEndDateTextForDate() {
        String dateString = "2020-01-08";
        String result = FieldDateUtil.getEndDateText(dateString);

        Assert.assertEquals(dateString, result);
    }

    @Test
    public void shouldBuildEndDateTextForNonDateText() {
        String dateString = "tomorrow";
        String result = FieldDateUtil.getEndDateText(dateString);

        Assert.assertEquals(dateString, result);
    }

    @Test
    public void shouldBuildEndDateTextForAfterTimesPhraseText() {
        String dateString = "AFTER_TIMES|5";
        String result = FieldDateUtil.getEndDateText(dateString);

        Assert.assertEquals("after 5 time(s)", result);
    }

    @Test
    public void shouldBuildEndDateTextForNoDatePhraseText() {
        String dateString = "NO_DATE|EMPTY";
        String result = FieldDateUtil.getEndDateText(dateString);

        Assert.assertEquals("never", result);
    }

    @Test
    public void shouldBuildBlankEndDateTextForNull() {
        String result = FieldDateUtil.getEndDateText(null);

        Assert.assertEquals("", result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimes() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));
        final ZonedDateTime endDate = getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), 20, TEST_TZ);
        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimesWhenFrequencyHasSeparatorAtTheEnd() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday,"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));

        final ZonedDateTime endDate = getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), 20, TEST_TZ);
        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimesWhenFrequencyHasSeparatorAtTheBeginning() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, ",Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));

        final ZonedDateTime endDate = getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), 20, TEST_TZ);
        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateForAfterTimesWhenFrequencyHasEmptyValue() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, ",Monday,,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|10"));
        final ZonedDateTime endDate = getMaxTimeForDate(2020, Month.JANUARY.getValue(), 20, TEST_TZ);
        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(endDate, result);
    }

    @Test
    public void shouldReturnEndDateAfterMonths() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + TimeType.MONTH + SEPARATOR + 2));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(getMaxTimeForDate(YEAR_2020, Month.MARCH.getValue(), DAY_15, TEST_TZ), result);
    }

    @Test
    public void shouldReturnEndDateAfterDays() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES,
                EndDateType.OTHER + SEPARATOR + TimeType.DAY + SEPARATOR + DAY_15));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(getMaxTimeForDate(YEAR_2020, Month.JANUARY.getValue(), DAY_30, TEST_TZ), result);
    }

    @Test
    public void shouldReturnEndDateAfterYears() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(
                buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.OTHER + SEPARATOR + TimeType.YEAR + SEPARATOR + 2));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertEquals(getMaxTimeForDate(YEAR_2022, Month.JANUARY.getValue(), DAY_15, TEST_TZ), result);
    }

    @Test
    public void shouldReturnNullTimeTypeBlank() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.OTHER + SEPARATOR + SEPARATOR + 2));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

        Assert.assertNull(result);
    }

    @Test
    public void shouldReturnNullUnitsBlank() {
        List<TemplateFieldValue> values = new ArrayList<>();
        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2020-01-15"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.OTHER + SEPARATOR + TimeType.MONTH + SEPARATOR));

        ZonedDateTime result = FieldDateUtil.getEndDate(values, OTHER_TEMPLATE);

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
                .withTemplateField(new TemplateFieldBuilder().withTemplateFieldType(type).build())
                .withValue(value)
                .build();
    }

    private TemplateFieldValue buildTemplateFieldWithValueAndDefaultValue(TemplateFieldType type, String value,
                                                                          String defaultValue) {
        return new TemplateFieldValueBuilder()
                .withTemplateField(
                        new TemplateFieldBuilder().withTemplateFieldType(type).withDefaultValue(defaultValue).build())
                .withValue(value)
                .build();
    }
}
