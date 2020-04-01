/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.junit.Test;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.EndDateType;
import org.openmrs.module.messages.api.util.ZoneConverterUtil;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

public class ExecutionContextTest extends BaseTest {

    private static final long MILLISECOND = 1;
    public static final String DUMMY_BEST_CONTACT_TIME = "02:34";
    public static final String DUMMY_DATE = "2020-02-15";

    @Test
    public void shouldSetRangeStartDateAsStartDateWhenItIsLaterThenTfvStartDate() throws ParseException {
        String tfvStartDateText = DUMMY_DATE;
        Date tfvStartDate = parseDate(tfvStartDateText);

        Date rangeStartDate = new Date(tfvStartDate.getTime() + MILLISECOND);
        Range<Date> dateRange = new Range<>(rangeStartDate, DateUtil.now());

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(
                        TemplateFieldType.START_OF_MESSAGES, tfvStartDateText))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME,
                null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(
                ExecutionContext.START_DATE_TIME_PARAM,
                ZoneConverterUtil.formatToUserZone(rangeStartDate)));
    }

    @Test
    public void shouldSetTfvStartDateAsStartDateWhenItIsLaterThenRangeStartDate() throws ParseException {
        String tfvStartDateText = DUMMY_DATE;
        Date tfvStartDate = parseDate(tfvStartDateText);

        Date rangeStartDate = new Date(tfvStartDate.getTime() - MILLISECOND);
        Range<Date> dateRange = new Range<>(rangeStartDate, DateUtil.now());

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(
                        TemplateFieldType.START_OF_MESSAGES, tfvStartDateText))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME,
                null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(
                ExecutionContext.START_DATE_TIME_PARAM,
                ZoneConverterUtil.formatToUserZone(tfvStartDate)));
    }

    @Test
    public void shouldSetTfvEndDateAsEndDateWhenItIsEarlierThenRangeEndDate() throws ParseException {
        String tfvEndDateText = DUMMY_DATE;
        Date tfvEndDate = parseDate(tfvEndDateText);

        Date rangeEndDate = new Date(tfvEndDate.getTime() + MILLISECOND);
        Range<Date> dateRange = new Range<>(DateUtil.now(), rangeEndDate);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(
                        TemplateFieldType.END_OF_MESSAGES, prepareDatePickerEndDateValue(tfvEndDateText)))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME,
                null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(
                ExecutionContext.END_DATE_TIME_PARAM,
                ZoneConverterUtil.formatToUserZone(tfvEndDate)));
    }

    @Test
    public void shouldSetRangeEndDateAsEndDateWhenItIsEarlierThenTfvEndDate() throws ParseException {
        String tfvEndDateText = DUMMY_DATE;
        Date tfvEndDate = parseDate(tfvEndDateText);

        Date rangeEndDate = new Date(tfvEndDate.getTime() - MILLISECOND);
        Range<Date> dateRange = new Range<>(DateUtil.now(), rangeEndDate);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(
                        TemplateFieldType.END_OF_MESSAGES, prepareDatePickerEndDateValue(tfvEndDateText)))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME,
                null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(
                ExecutionContext.END_DATE_TIME_PARAM,
                ZoneConverterUtil.formatToUserZone(rangeEndDate)));
    }

    @Test
    public void shouldReturnExpectedExecutionStartDateParamIfThisValueWasSet() {
        Date expectedExecutionStartDate = new Date();

        Range<Date> dateRange = new Range<>(DateUtil.now(), DateUtil.now());
        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();
        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME,
                expectedExecutionStartDate);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(
                ExecutionContext.EXECUTION_START_DATE_TIME,
                ZoneConverterUtil.formatToUserZone(expectedExecutionStartDate)));
    }

    private Date parseDate(String dateTime) throws ParseException {
        return new SimpleDateFormat(MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT).parse(dateTime);
    }

    private String prepareDatePickerEndDateValue(String tfvEndDateText) {
        return EndDateType.DATE_PICKER.getName() + "|" + tfvEndDateText;
    }

    private List<TemplateFieldValue> buildTemplateFieldWithValue(TemplateFieldType type, String value) {
        return wrap(new TemplateFieldValueBuilder()
                .withTemplateField(
                        new TemplateFieldBuilder()
                                .withTemplateFieldType(type)
                                .build())
                .withValue(value)
                .build());
    }

    private <T> List<T> wrap(T toWrap) {
        ArrayList<T> list = new ArrayList<>();
        list.add(toWrap);
        return list;
    }
}
