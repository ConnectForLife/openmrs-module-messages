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
