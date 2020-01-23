package org.openmrs.module.messages.api.util.end.date.after.times;

import org.joda.time.LocalDate;

import java.util.Date;

public class DatePointer {

    private int counter;
    private LocalDate date;

    public DatePointer(int counter, LocalDate date) {
        this.counter = counter;
        this.date = date;
    }

    public DatePointer(int counter, Date date) {
        this.counter = counter;
        this.date = new LocalDate(date);
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

    public LocalDate getDate() {
        return date;
    }
}
