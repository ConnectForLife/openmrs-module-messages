/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openmrs.module.messages.BaseTest;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.api.util.EndDateType;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.text.ParseException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DateUtil.class})
public class ExecutionContextTest extends BaseTest {

    private static final ZonedDateTime TEST_NOW =
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(1625756392L), ZoneId.of("UTC"));
    private static final String DUMMY_BEST_CONTACT_TIME = "02:34";
    private static final String DUMMY_DATE = "2020-02-15";
    private static final long MILLISECOND = 1;

    @Before
    public void prepareTest() throws IllegalAccessException {
        // Sets DateUtil's clock to predefined and fixed point in time
        PowerMockito.field(DateUtil.class, "clock").set(null, Clock.fixed(TEST_NOW.toInstant(), TEST_NOW.getZone()));
    }

    @Test
    public void shouldSetRangeStartDateAsStartDateWhenItIsLaterThenTfvStartDate() {
        String tfvStartDateText = DUMMY_DATE;
        ZonedDateTime tfvStartDate = parseDate(tfvStartDateText);

        ZonedDateTime rangeStartDate = tfvStartDate.plus(MILLISECOND, ChronoUnit.MILLIS);
        Range<ZonedDateTime> dateRange = new Range<>(rangeStartDate, DateUtil.now());

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES, tfvStartDateText))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME, null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params,
                hasEntry(ExecutionContext.START_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(rangeStartDate)));
    }

    @Test
    public void shouldSetTfvStartDateAsStartDateWhenItIsLaterThenRangeStartDate() {
        String tfvStartDateText = DUMMY_DATE;
        ZonedDateTime tfvStartDate = parseDate(tfvStartDateText);

        ZonedDateTime rangeStartDate = tfvStartDate.minus(MILLISECOND, ChronoUnit.MILLIS);
        Range<ZonedDateTime> dateRange = new Range<>(rangeStartDate, DateUtil.now());

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(TemplateFieldType.START_OF_MESSAGES, tfvStartDateText))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME, null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params,
                hasEntry(ExecutionContext.START_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(tfvStartDate)));
    }

    @Test
    public void shouldSetTfvEndDateAsEndDateWhenItIsEarlierThenRangeEndDate() {
        String tfvEndDateText = DUMMY_DATE;
        ZonedDateTime tfvEndDate = parseDate(tfvEndDateText);

        ZonedDateTime rangeEndDate = tfvEndDate.plus(MILLISECOND, ChronoUnit.MILLIS);
        Range<ZonedDateTime> dateRange = new Range<>(DateUtil.now(), rangeEndDate);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
                        prepareDatePickerEndDateValue(tfvEndDateText)))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME, null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(ExecutionContext.END_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(tfvEndDate)));
    }

    @Test
    public void shouldSetRangeEndDateAsEndDateWhenItIsEarlierThenTfvEndDate() {
        String tfvEndDateText = DUMMY_DATE;
        ZonedDateTime tfvEndDate = parseDate(tfvEndDateText);

        ZonedDateTime rangeEndDate = tfvEndDate.minus(MILLISECOND, ChronoUnit.MILLIS);
        Range<ZonedDateTime> dateRange = new Range<>(DateUtil.now(), rangeEndDate);

        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withTemplateFieldValues(buildTemplateFieldWithValue(TemplateFieldType.END_OF_MESSAGES,
                        prepareDatePickerEndDateValue(tfvEndDateText)))
                .build();

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME, null);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params,
                hasEntry(ExecutionContext.END_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(rangeEndDate)));
    }

    @Test
    public void shouldReturnExpectedExecutionStartDateParamIfThisValueWasSet() {
        ZonedDateTime expectedExecutionStartDate = DateUtil.now();

        Range<ZonedDateTime> dateRange = new Range<>(DateUtil.now(), DateUtil.now());
        PatientTemplate patientTemplate = new PatientTemplateBuilder().build();
        ExecutionContext executionContext =
                new ExecutionContext(patientTemplate, dateRange, DUMMY_BEST_CONTACT_TIME, expectedExecutionStartDate);
        Map<String, Object> params = executionContext.getParams();

        assertThat(params, hasEntry(ExecutionContext.EXECUTION_START_DATE_TIME,
                DateUtil.formatToServerSideDateTime(expectedExecutionStartDate)));
    }

    private ZonedDateTime parseDate(String date) {
        final LocalDate localDate =
                LocalDate.parse(date, DateTimeFormatter.ofPattern(MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT));

        return ZonedDateTime.of(localDate, LocalTime.MIDNIGHT, TEST_NOW.getZone());
    }

    private String prepareDatePickerEndDateValue(String tfvEndDateText) {
        return EndDateType.DATE_PICKER.getName() + "|" + tfvEndDateText;
    }

    private List<TemplateFieldValue> buildTemplateFieldWithValue(TemplateFieldType type, String value) {
        return wrap(new TemplateFieldValueBuilder()
                .withTemplateField(new TemplateFieldBuilder().withTemplateFieldType(type).build())
                .withValue(value)
                .build());
    }

    private <T> List<T> wrap(T toWrap) {
        ArrayList<T> list = new ArrayList<>();
        list.add(toWrap);
        return list;
    }
}
