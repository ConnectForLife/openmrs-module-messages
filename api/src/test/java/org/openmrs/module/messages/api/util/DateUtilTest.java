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
import org.openmrs.module.messages.BaseTest;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class DateUtilTest extends BaseTest {

    private static final ZonedDateTime TEST_NOW =
            ZonedDateTime.ofInstant(Instant.ofEpochSecond(1625756392L), ZoneId.of("Asia/Kolkata"));
    private static final String DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_TIME_PATTERN_WITH_SECONDS_PRECISION = "yyyy-MM-dd HH:mm:ss";

    @Before
    public void prepareTest() throws IllegalAccessException {
        PowerMockito.field(DateUtil.class, "clock").set(null, Clock.fixed(TEST_NOW.toInstant(), TEST_NOW.getZone()));
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
