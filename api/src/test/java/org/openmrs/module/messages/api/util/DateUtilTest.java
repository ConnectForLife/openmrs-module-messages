/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.Assert;
import org.junit.Test;
import org.openmrs.module.messages.BaseTest;

public class DateUtilTest extends BaseTest {

    private static final String DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_TIME_PATTERN_WITH_SECONDS_PRECISION = "yyyy-MM-dd HH:mm:ss";

    @Test
    public void shouldConvertToServerSideDateTimeWith24HoursTimeFormat() throws ParseException {
        final String expectedDateTime = "2020-02-15 16:17:18";
        final Date date = parseDateTimeWithSecondsPrecision(expectedDateTime);

        String actual = DateUtil.convertToServerSideDateTime(date);

        Assert.assertEquals(expectedDateTime, actual);
    }

    @Test
    public void shouldConvertToServerSideDateTimeWithSecondsPrecisionFormat() throws ParseException {
        final String expectedDateTime = "2020-02-09 10:17:18";
        final Date date = parseDateTimeWithMillisecondsPrecision("2020-02-09 10:17:18.123");

        String actual = DateUtil.convertToServerSideDateTime(date);

        Assert.assertEquals(expectedDateTime, actual);
    }

    private Date parseDateTimeWithMillisecondsPrecision(String dateTime) throws ParseException {
        return new SimpleDateFormat(DATE_TIME_PATTERN_WITH_MILLISECONDS_PRECISION).parse(dateTime);
    }

    private Date parseDateTimeWithSecondsPrecision(String dateTime) throws ParseException {
        return new SimpleDateFormat(DATE_TIME_PATTERN_WITH_SECONDS_PRECISION).parse(dateTime);
    }
}
