/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

public final class TestUtil {

    private TestUtil() {
    }

    public static ZonedDateTime getMaxTimeForDate(int year, int month, int dayOfMonth, ZoneId zoneId) {
        return ZonedDateTime.of(LocalDate.of(year, month, dayOfMonth), LocalTime.MIDNIGHT.minus(1, ChronoUnit.MILLIS),
                zoneId);
    }

    public static ZonedDateTime getMinTimeForDate(int year, int month, int dayOfMonth, ZoneId zoneId) {
        return ZonedDateTime.of(LocalDate.of(year, month, dayOfMonth), LocalTime.MIDNIGHT, zoneId);
    }
}
