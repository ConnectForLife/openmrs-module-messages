package org.openmrs.module.messages.api.util;

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

    public static TimeZone getDefaultTimeZone() {
        return DEFAULT_TIME_ZONE;
    }

    private DateUtil() { }
}
