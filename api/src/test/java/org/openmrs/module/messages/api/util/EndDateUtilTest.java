package org.openmrs.module.messages.api.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import static org.openmrs.module.messages.api.model.TemplateFieldType.START_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.END_OF_MESSAGES;
import static org.openmrs.module.messages.api.model.TemplateFieldType.MESSAGING_FREQUENCY;

public class EndDateUtilTest {

    private static final int YEAR_2020 = 2020 - 1900; // new Date implementation is x - 1900
    private static final Date EXPECTED = new Date(YEAR_2020, Calendar.JANUARY, 8, 0, 0, 0);

    @Test
    public void shouldGetValidDatePickerEndDate() {
        final Date expected = new Date(YEAR_2020, Calendar.JANUARY, 10, 0, 0, 0);
        Date result = EndDateUtil.getEndDate(getValidDatePickerValues());

        Assert.assertEquals(expected, result);
    }

    @Test
    public void shouldParseAfterTimesInvalidValue() {
        final String patientTemplateFieldValue = EndDateType.AFTER_TIMES.getName() + "|2020-sda01---|--08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseAfterTimesDateEmptyWithSeparator() {
        final String patientTemplateFieldValue = EndDateType.AFTER_TIMES.getName() + "|";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseAfterTimesDateType() {
        final String patientTemplateFieldValue = EndDateType.AFTER_TIMES.getName();

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDate() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName() + "|2020-01-08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertEquals(EXPECTED, result);
    }

    @Test
    public void shouldParseDatePickerInvalidValue() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName() + "|2020-01-----08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateEmptyWithSeparator() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName() + "|";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseDatePickerDateType() {
        final String patientTemplateFieldValue = EndDateType.DATE_PICKER.getName();

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateInvalidValue() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName() + "|2020---091---|08";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparatorAndConstant() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName() + "|EMPTY";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateWithSeparator() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName() + "|";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDate() {
        final String patientTemplateFieldValue = EndDateType.NO_DATE.getName();

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateBlank() {
        final String patientTemplateFieldValue = "";

        Date result = EndDateUtil.parseEndDate(patientTemplateFieldValue);

        Assert.assertNull(result);
    }

    @Test
    public void shouldParseNoDateNull() {
        Date result = EndDateUtil.parseEndDate(null);

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

        values.add(buildTemplateFieldWithValue(MESSAGING_FREQUENCY, "Monday,Sunday"));
        values.add(buildTemplateFieldWithValue(START_OF_MESSAGES, "2019-12-20"));
        values.add(buildTemplateFieldWithValue(END_OF_MESSAGES, EndDateType.AFTER_TIMES.getName() + "|1"));
        return values;
    }

    private List<TemplateFieldValue> getValidDatePickerValues() {
        List<TemplateFieldValue> values = new ArrayList<>();

        values.add(buildTemplateFieldWithValue(MESSAGING_FREQUENCY, "Tuesday,Wednesday"));
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
}
