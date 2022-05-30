/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.BaseTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Date;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, DateUtil.class})
public class DateUtilTest extends BaseTest {

    private static final ZonedDateTime TEST_NOW =
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(1625756392L), ZoneId.of("Asia/Kolkata"));
    private static final String DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_TIME_PATTERN_WITH_SECONDS_PRECISION = "yyyy-MM-dd HH:mm:ss";

    @Mock
    private AdministrationService administrationService;

    @Before
    public void prepareTest() throws IllegalAccessException {
        PowerMockito.field(DateUtil.class, "clock").set(null, Clock.fixed(TEST_NOW.toInstant(), TEST_NOW.getZone()));

        mockStatic(Context.class);
        when(Context.isSessionOpen()).thenReturn(true);
        when(Context.getAdministrationService()).thenReturn(administrationService);
    }

    @Test
    public void shouldConvertToServerSideDateTimeWith24HoursTimeFormat() {
        final String expectedDateTime = "2020-02-15 16:17:18";
        final ZonedDateTime date = parseDateTimeWithSecondsPrecision(expectedDateTime);

        String actual = DateUtil.formatToServerSideDateTime(date);

        Assert.assertEquals(expectedDateTime, actual);
    }

    @Test
    public void shouldConvertToServerSideDateTimeWithSecondsPrecisionFormat() {
        final String expectedDateTime = "2020-02-09 10:17:18";
        final ZonedDateTime date = parseDateTimeWithMillisecondsPrecision("2020-02-09 10:17:18.123");

        String actual = DateUtil.formatToServerSideDateTime(date);

        Assert.assertEquals(expectedDateTime, actual);
    }

    @Test
    public void shouldConvertDateTimeWithSecondsPrecisionFormat() {
        final String expectedDateTime = "2020-02-09 10:17:18.123";
        final ZonedDateTime date = parseDateTimeWithMillisecondsPrecision(expectedDateTime);

        String actual = DateUtil.formatDateTime(date, DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION);

        Assert.assertEquals(expectedDateTime, actual);
    }

    @Test
    public void shouldReturnEmptyStringWhenDateIsNull() {
        Assert.assertEquals(StringUtils.EMPTY, DateUtil.formatDateTime(null, DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION));
    }

    @Test
    public void convertServerSideDateFormatToFrontendShouldSupportTimePartInInput() {
        // given
        final String dateWithTimePart = "2021-07-15 12:41:17";
        final String expected = "15 Jul 2021";

        // when
        final String actual = DateUtil.convertServerSideDateFormatToFrontend(dateWithTimePart);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void convertServerSideDateFormatToFrontendShouldParseDateOnly() {
        // given
        final String dateWithTimePart = "2021-07-15";
        final String expected = "15 Jul 2021";

        // when
        final String actual = DateUtil.convertServerSideDateFormatToFrontend(dateWithTimePart);

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void parseServerSideDateShouldSupportTimePartInInput() {
        // given
        final String requestedZone = "Asia/Manila";
        final String dateWithTimePart = "2021-07-15 12:41:17";
        final ZonedDateTime expected = ZonedDateTime.parse("2021-07-15T00:00:00+05:30[Asia/Manila]");

        // when
        final ZonedDateTime actual = DateUtil.parseServerSideDate(dateWithTimePart, ZoneId.of(requestedZone));

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void parseServerSideDateShouldSupportDateOnly() {
        // given
        final String requestedZone = "Asia/Manila";
        final String dateWithoutTimePart = "2021-07-15";
        final ZonedDateTime expected = ZonedDateTime.parse("2021-07-15T00:00:00+05:30[Asia/Manila]");

        // when
        final ZonedDateTime actual = DateUtil.parseServerSideDate(dateWithoutTimePart, ZoneId.of(requestedZone));

        // then
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnDefaultUserTimeZone() {
        when(administrationService.getGlobalProperty("messages.defaultUserTimezone")).thenReturn("Europe/Brussels");

        ZoneId actual = DateUtil.getDefaultUserTimeZone();

        assertNotNull(actual);
        assertEquals("Europe/Brussels", actual.getId());
    }

    @Test
    public void shouldGetDateTimeUsingMilliseconds() {
        when(administrationService.getGlobalProperty("messages.defaultUserTimezone")).thenReturn("Europe/Brussels");
        long dateInMilliseconds = 1653484755000L; //Wed 25 May 2022 15:19:15
        ZonedDateTime expected = ZonedDateTime.of(2022, 5, 25, 15, 19, 15, 0, DateUtil.getDefaultUserTimeZone());

        ZonedDateTime actual = DateUtil.ofEpochMilliInUserTimeZone(dateInMilliseconds);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldConvertSqlDateToZonedDateTime() {
        Date sqlDate = new Date(1653429600000L); //Wed 25 May 2022 00:00:00
        ZonedDateTime expected = ZonedDateTime.parse("2022-05-25T00:00:00+08:00[Asia/Kolkata]");

        ZonedDateTime actual = DateUtil.convertOpenMRSDatabaseDate(sqlDate);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnNullWhenPassedDateIsNull() {
        ZonedDateTime actual = DateUtil.convertOpenMRSDatabaseDate(null);

        assertNull(actual);
    }

    @Test
    public void shouldFormatDateToServerFormat() {
        ZonedDateTime dateTime = ZonedDateTime.of(2022, 5, 25, 15, 19, 15, 0, DateUtil.getDefaultUserTimeZone());

        String actual = DateUtil.formatToServerSideDate(dateTime);

        assertNotNull(actual);
        assertEquals("2022-05-25 15:19:15", actual);
    }

    @Test
    public void shouldFormatDateToDefaultFrontendFormat() {
        ZonedDateTime dateTime = ZonedDateTime.of(2022, 5, 25, 15, 19, 15, 0, DateUtil.getDefaultUserTimeZone());

        String actual = DateUtil.formatToFrontSideDate(dateTime);

        assertNotNull(actual);
        assertEquals("25 May 2022", actual);
    }

    @Test
    public void shouldAddDaysToDate() {
        java.util.Date dateToProcess = new Date(1653429600000L); //25-05-2022 00:00:00;
        java.util.Date expected = new Date(1653602400000L); //27-05-2022 00:00:00;

        java.util.Date actual = DateUtil.addDaysToDate(dateToProcess, 2);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldReturnCorrectDateWithTimeOfDay() {
        final TimeZone testTimeZone = TimeZone.getTimeZone("UTC");
        final Calendar calendar = Calendar.getInstance(testTimeZone);
        calendar.set(2021, Calendar.MAY, 6, 13, 23, 45);
        calendar.set(Calendar.MILLISECOND, 765);
        final java.util.Date date = calendar.getTime();
        final String timeStr = "14:11";

        final java.util.Date result = DateUtil.getDateWithTimeOfDay(date, timeStr, testTimeZone);

        final Calendar expectedCalendar = Calendar.getInstance(testTimeZone);
        expectedCalendar.set(2021, Calendar.MAY, 6, 14, 11, 0);
        expectedCalendar.set(Calendar.MILLISECOND, 0);
        final java.util.Date expectedDate = expectedCalendar.getTime();

        assertNotNull(result);
        assertThat(result, is(expectedDate));
    }

    @Test
    public void shouldConvertZoneIdToTimeZone() {
        ZoneId zoneIdToConvert = ZoneId.of("Europe/Brussels");

        TimeZone actual = DateUtil.convertZoneIdToTimeZone(zoneIdToConvert);

        assertNotNull(actual);
        assertEquals("Central European Time", actual.getDisplayName());
    }

    private ZonedDateTime parseDateTimeWithMillisecondsPrecision(String localDateTime) {
        return LocalDateTime
                .parse(localDateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION))
                .atZone(TEST_NOW.getZone());
    }

    private ZonedDateTime parseDateTimeWithSecondsPrecision(String localDateTime) {
        return LocalDateTime
                .parse(localDateTime, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN_WITH_SECONDS_PRECISION))
                .atZone(TEST_NOW.getZone());
    }
}
