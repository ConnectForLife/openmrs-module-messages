package org.openmrs.module.messages.api.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import static org.openmrs.module.messages.api.model.TemplateFieldType.DAY_OF_WEEK;
import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EndDateUtilTest {

    private static final int YEAR_2020 = 2020 - 1900; // new Date implementation is x - 1900
    private static final int YEAR_2022 = 2022 - 1900; // new Date implementation is x - 1900
    private static final int DAY_15 = 15;
    private static final int DAY_30 = 30;
    private static final Date EXPECTED = new Date(YEAR_2020, Calendar.JANUARY, 8, 0, 0, 0);
    private static final String SEPARATOR = "|";

    @Test
    public void shouldGetValidDatePickerEndDate() {
        final Date expected = new Date(YEAR_2020, Calendar.JANUARY, 10, 0, 0, 0);
        Date result = EndDateUtil.getEndDate(getValidDatePickerValues());

        Assert.assertEquals(expected, result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseAfterTimesInvalidValue() {
        final String patientTemplateFieldValue = EndDateType.AFTER_TIMES.getName() + "|2020-sda01---|--08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseAfterTimesDateEmptyWithSeparator() {
        final String patientTemplateFieldValue = EndDateType.AFTER_TIMES.getName() + "|";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseAfterTimesDateType() {
        final String patientTemplateFieldValue = EndDateType.AFTER_TIMES.getName();

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseDatePickerDate() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName() + "|2020-01-08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertEquals(EXPECTED, result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseDatePickerInvalidValue() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName() + "|2020-01-----08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseDatePickerDateEmptyWithSeparator() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName() + "|";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateType() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName();

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseNoDateInvalidValue() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName() + "|2020---091---|08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparatorAndConstant() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName() + "|EMPTY";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseNoDateWithSeparator() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName() + "|";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseNoDate() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName();

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseNoDateBlank() {
        final String patientTemplateFieldValue = "";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue, null, null);

        Assert.assertNull(result);
    }

    @Test
    @Ignore // TODO: 16.01.2020 This test should be enabled when CFLM-459 will be finished
    public void shouldParseNoDateNull() {
        Date result = EndDateUtil.parseEndDate(null, null, null);

        Assert.assertNull(result);
    }

    @Test
    public void shouldReturnEndDateAfterMonths() {
        String templateFieldValue = EndDateType.OTHER + SEPARATOR + TimeType.MONTH + SEPARATOR + 2;
        Date startDate = createDate(YEAR_2020, Calendar.JANUARY, DAY_15);

        Date result = EndDateUtil.parseEndDate(templateFieldValue, startDate, new String[] { });

        Assert.assertEquals(createDate(YEAR_2020, Calendar.MARCH, DAY_15), result);
    }

    @Test
    public void shouldReturnEndDateAfterDays() {
        String templateFieldValue = EndDateType.OTHER + SEPARATOR + TimeType.DAY + SEPARATOR + DAY_15;
        Date startDate = createDate(YEAR_2020, Calendar.JANUARY, DAY_15);

        Date result = EndDateUtil.parseEndDate(templateFieldValue, startDate, new String[] { });

        Assert.assertEquals(createDate(YEAR_2020, Calendar.JANUARY, DAY_30), result);
    }

    @Test
    public void shouldReturnEndDateAfterYears() {
        String templateFieldValue = EndDateType.OTHER + SEPARATOR + TimeType.YEAR + SEPARATOR + 2;
        Date startDate = createDate(YEAR_2020, Calendar.JANUARY, DAY_15);

        Date result = EndDateUtil.parseEndDate(templateFieldValue, startDate, new String[] { });

        Assert.assertEquals(createDate(YEAR_2022, Calendar.JANUARY, DAY_15), result);
    }

    @Test
    public void shouldReturnNullTimeTypeBlank() {
        String templateFieldValue = EndDateType.OTHER + SEPARATOR + SEPARATOR + 2;
        Date startDate = createDate(YEAR_2020, Calendar.JANUARY, DAY_15);

        Date result = EndDateUtil.parseEndDate(templateFieldValue, startDate, new String[] { });

        Assert.assertNull(result);
    }

    @Test
    public void shouldReturnNullUnitsBlank() {
        String templateFieldValue = EndDateType.OTHER + SEPARATOR + TimeType.MONTH + SEPARATOR;
        Date startDate = createDate(YEAR_2020, Calendar.JANUARY, DAY_15);

        Date result = EndDateUtil.parseEndDate(templateFieldValue, startDate, new String[] { });

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
        String dateString = null;
        String result = EndDateUtil.getEndDateText(dateString);

        Assert.assertEquals("", result);
    }

    private List<TemplateFieldValue> getValidAfterTimesValues() {
        List<TemplateFieldValue> values = new ArrayList<>();

        values.add(buildTemplateFieldWithValue(DAY_OF_WEEK, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|1"));
        return values;
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

    private Date createDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
}
