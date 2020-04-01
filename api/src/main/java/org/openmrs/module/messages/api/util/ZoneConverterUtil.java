package org.openmrs.module.messages.api.util;

import org.openmrs.api.context.Context;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.openmrs.module.messages.api.constants.ConfigConstants.DEFAULT_USER_TIMEZONE;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATETIME_FORMAT;

public final class ZoneConverterUtil {

    public static Date convertToUserZone(Date date) {
        return convertToZone(date, getUserZone());
    }

    public static Date convertToZone(Date date, ZoneId zoneId) {
        ZonedDateTime newDate = date.toInstant().atZone(ZoneId.of(DateUtil.DEFAULT_SYSTEM_TIME_ZONE.getID()));
        newDate = newDate.withZoneSameLocal(zoneId);
        return Date.from(newDate.toInstant());
    }

    public static ZoneId getUserZone() {
        String zoneName = DateUtil.DEFAULT_SYSTEM_TIME_ZONE.getID();
        if (Context.isSessionOpen()) {
            zoneName = Context.getAdministrationService().getGlobalProperty(DEFAULT_USER_TIMEZONE, zoneName);
        }
        return ZoneId.of(zoneName);
    }

    public static String formatToUserZone(Date date) {
        return formatToUserZone(date, DEFAULT_SERVER_SIDE_DATETIME_FORMAT);
    }

    public static String formatToUserZone(Date date, String toFormat) {
        return formatToZone(date, toFormat, getUserZone());
    }

    public static String formatToZone(Date date, String toFormat, ZoneId zoneId) {
        return ZonedDateTime.ofInstant(date.toInstant(), zoneId)
            .format(DateTimeFormatter.ofPattern(toFormat));
    }

    private ZoneConverterUtil() {
    }
}
