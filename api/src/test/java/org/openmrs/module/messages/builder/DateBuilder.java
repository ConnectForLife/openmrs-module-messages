/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.builder;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class DateBuilder extends AbstractBuilder<ZonedDateTime> {

    public static final int DEFAULT_YEAR = 2019;
    public static final int DEFAULT_MONTH = 1;
    public static final int DEFAULT_DAY = 1;
    public static final int DEFAULT_HRS = 0;
    public static final int DEFAULT_MIN = 0;
    public static final int DEFAULT_SEC = 0;
    public static final ZoneId DEFAULT_TZ = ZoneId.of("UTC");

    private int year = DEFAULT_YEAR;
    private int month = DEFAULT_MONTH;
    private int day = DEFAULT_DAY;
    private int hrs = DEFAULT_HRS;
    private int min = DEFAULT_MIN;
    private int sec = DEFAULT_SEC;
    private ZoneId zone = DEFAULT_TZ;

    public DateBuilder() {
        super();
    }

    @Override
    public ZonedDateTime build() {
        return ZonedDateTime.of(year, month, day, hrs, min, sec, 0, zone);
    }

    @Override
    public ZonedDateTime buildAsNew() {
        return build();
    }

    public DateBuilder withYear(int year) {
        this.year = year;
        return this;
    }

    public DateBuilder withMonth(int month) {
        this.month = month;
        return this;
    }

    public DateBuilder withDay(int day) {
        this.day = day;
        return this;
    }

    public DateBuilder withHrs(int hrs) {
        this.hrs = hrs;
        return this;
    }

    public DateBuilder withMin(int min) {
        this.min = min;
        return this;
    }

    public DateBuilder withSec(int sec) {
        this.sec = sec;
        return this;
    }

    public DateBuilder withTimeZone(ZoneId zone) {
        this.zone = zone;
        return this;
    }
}
