package org.openmrs.module.messages.api.util;

import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.openmrs.module.messages.api.util.DateUtil.DEFAULT_SYSTEM_TIME_ZONE;

public class ZoneConverterUtilTest {

    private static final String INDIA_TIMEZONE = "Asia/Dacca";
    private static final String EUROPE_TIMEZONE = "Europe/Warsaw";
    private static final ZoneId DEFAULT_ZONE = ZoneId.of(DEFAULT_SYSTEM_TIME_ZONE.getID());
    private static final int YEAR_2020 = 2020;
    private static final int MONTH_3 = 3;
    private static final int DAY_26 = 26;
    private static final int DAY_29 = 29;
    private static final int HOUR_12 = 12;
    private static final int SIX = 6;
    private static final int ONE = 1;
    private static final int TWO = 2;

    @Test
    public void convertToZoneForIndiaTimezone() {
        Date given = Date.from(
                ZonedDateTime.of(YEAR_2020, MONTH_3, DAY_26, HOUR_12, 0, 0, 0,
                        DEFAULT_ZONE)
                        .toInstant()
        );
        Date actual = ZoneConverterUtil.convertToZone(given, ZoneId.of(INDIA_TIMEZONE));
        long diff = TimeUnit.HOURS.convert(given.getTime() - actual.getTime(), TimeUnit.MILLISECONDS);
        Assert.assertEquals(SIX, diff);
    }

    @Test
    public void convertToZoneForEuropeDaylightSavingTimezone() {
        Date given = Date.from(
                ZonedDateTime.of(YEAR_2020, MONTH_3, DAY_26, HOUR_12, 0, 0, 0,
                        DEFAULT_ZONE)
                        .toInstant()
        );
        Date actual = ZoneConverterUtil.convertToZone(given, ZoneId.of(EUROPE_TIMEZONE));
        long diff = TimeUnit.HOURS.convert(given.getTime() - actual.getTime(), TimeUnit.MILLISECONDS);
        Assert.assertEquals(ONE, diff);
    }

    @Test
    public void convertToZoneForEuropeTimezone() {
        Date given = Date.from(
                ZonedDateTime.of(YEAR_2020, MONTH_3, DAY_29, HOUR_12, 0, 0, 0,
                        DEFAULT_ZONE)
                        .toInstant()
        );
        Date actual = ZoneConverterUtil.convertToZone(given, ZoneId.of(EUROPE_TIMEZONE));
        long diff = TimeUnit.HOURS.convert(given.getTime() - actual.getTime(), TimeUnit.MILLISECONDS);
        Assert.assertEquals(TWO, diff);
    }
}
