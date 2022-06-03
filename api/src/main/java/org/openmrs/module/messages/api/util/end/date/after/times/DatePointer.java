/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util.end.date.after.times;

import java.time.ZonedDateTime;

public class DatePointer {

    private int counter;
    private ZonedDateTime date;

    public DatePointer(int counter, ZonedDateTime date) {
        this.counter = counter;
        this.date = date;
    }

    public int getCounter() {
        return counter;
    }

    public void incCounter(int by) {
        counter += by;
    }

    public void incDateByDay(int by) {
        date = date.plusDays(by);
    }

    public void incDateByWeek(int by) {
        date = date.plusWeeks(by);
    }

    public void incDateByMonth(int by) {
        date = date.plusMonths(by);
    }

    public void incDateToBeginningOfNextMonth() {
        date = date.plusMonths(1);
        date = date.withDayOfMonth(1);
    }

    public ZonedDateTime getDate() {
        return date;
    }
}
