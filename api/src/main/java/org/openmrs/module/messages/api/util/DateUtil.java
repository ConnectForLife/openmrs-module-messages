/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public final class DateUtil {

    public static final int DAY_IN_SECONDS = 24 * 60 * 60;

    private static final TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("UTC");

    public static Date now() {
        return getDateWithDefaultTimeZone(new Date());
    }

    public static Date getDatePlusSeconds(long seconds) {
        return DateUtils.addSeconds(DateUtil.now(), (int) seconds);
    }

    public static boolean isNotAfter(Date first, Date second) {
        return !first.after(second);
    }

    public static Date getDateWithDefaultTimeZone(Date timestamp) {
        return getDateWithTimeZone(timestamp, getDefaultTimeZone());
    }

    public static Date getDateWithTimeZone(Date timestamp, TimeZone timeZone) {
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTime(timestamp);
        return calendar.getTime();
    }

    public static Date getDateSecondsAgo(long seconds) {
        return DateUtils.addSeconds(DateUtil.now(), (int) (seconds * -1));
    }

    public static boolean isSameInstant(Date date1, Date date2) {
        return DateUtils.isSameInstant(date1, date2);
    }

    public static TimeZone getDefaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    public static String convertServerSideDateFormatToFrontend(String date) throws ParseException {
        return convertDate(date, DEFAULT_SERVER_SIDE_DATE_FORMAT, DEFAULT_FRONT_END_DATE_FORMAT);
    }

    public static String convertFrontendDateFormatToServerSide(String date) throws ParseException {
        return convertDate(date, DEFAULT_FRONT_END_DATE_FORMAT, DEFAULT_SERVER_SIDE_DATE_FORMAT);
    }

    public static Date toSimpleDate(Date date) {
        if (date instanceof java.sql.Date) { // java.sql.Date cannot be persisted by hibernate
            return new Date(date.getTime());
        }
        return date;
    }

    private static String convertDate(String date, String fromFormat, String toFormat) throws ParseException {
        SimpleDateFormat newDateFormat = new SimpleDateFormat(fromFormat);
        Date myDate = newDateFormat.parse(date);
        newDateFormat.applyPattern(toFormat);
        return newDateFormat.format(myDate);
    }

    private DateUtil() { }
}
