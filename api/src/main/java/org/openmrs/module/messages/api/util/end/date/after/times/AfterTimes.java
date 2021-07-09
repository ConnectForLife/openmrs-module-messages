package org.openmrs.module.messages.api.util.end.date.after.times;

import org.openmrs.module.messages.api.util.FrequencyType;

import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Set;

public class AfterTimes {

    private final Set<Integer> days;
    private FrequencyType frequency;
    private int occurrences;
    private DatePointer pointer;

    public AfterTimes(FrequencyType frequency, Set<Integer> days, int occurrences, ZonedDateTime startDate) {
        validateDaysAndFrequency(frequency, days, occurrences);
        this.frequency = frequency;
        this.days = days;
        this.occurrences = occurrences;
        this.pointer = new DatePointer(0, startDate);
    }

    public ZonedDateTime get() {
        if (days.size() == 0) {
            return null;
        }
        switch (frequency) {
            case DAILY:
                return getEndDateForDaily();
            case WEEKLY:
                return getEndDateForWeekly();
            case MONTHLY:
                return getEndDateForMonthly();
            default:
                throw new IllegalArgumentException(String.format("Frequency %s is not supported", frequency.getName()));
        }
    }

    private ZonedDateTime getEndDateForDaily() {
        while (pointer.getCounter() < occurrences) {
            int currentWeekday = pointer.getDate().getDayOfWeek().getValue();
            if (days.contains(currentWeekday)) {
                pointer.incCounter(1);
            }
            if (pointer.getCounter() != occurrences) {
                pointer.incDateByDay(1);
            }
        }
        return pointer.getDate();
    }

    private ZonedDateTime getEndDateForWeekly() {
        setNearestWeekDay();
        int futureOccurrences = occurrences > 1 ? occurrences - 1 : 0;
        pointer.incDateByWeek(futureOccurrences);
        return pointer.getDate();
    }

    private ZonedDateTime getEndDateForMonthly() {
        final int lastOccurrence = 1;
        int occurrencesBetween = occurrences - pointer.getCounter() - lastOccurrence;
        if (occurrencesBetween > 0) {
            pointer.incDateByMonth(occurrencesBetween);
        }
        setNearestWeekdayOfMonth();
        return pointer.getDate();
    }

    private void setNearestWeekDay() {
        while (!days.contains(pointer.getDate().getDayOfWeek().getValue())) {
            pointer.incDateByDay(1);
        }
    }

    private void setNearestWeekdayOfMonth() {
        final ZonedDateTime startMonthWeekday = getFirstWeekdayOfMonth(pointer.getDate());

        if (pointer.getDate().isBefore(startMonthWeekday)) {
            pointer = new DatePointer(pointer.getCounter() + 1, startMonthWeekday);
        } else {
            setNextMonthWeekday();
        }
    }

    private void setNextMonthWeekday() {
        pointer.incDateToBeginningOfNextMonth();

        ZonedDateTime nextMonthWeekday = getFirstWeekdayOfMonth(pointer.getDate());
        pointer = new DatePointer(pointer.getCounter() + 1, nextMonthWeekday);
    }

    private ZonedDateTime getFirstWeekdayOfMonth(ZonedDateTime date) {
        ZonedDateTime monthDate = date.with(TemporalAdjusters.firstDayOfMonth());

        while (!days.contains(monthDate.getDayOfWeek().getValue())) {
            monthDate = monthDate.plusDays(1);
        }

        return monthDate;
    }

    private void validateDaysAndFrequency(FrequencyType frequency, Set<Integer> days, int occurrences) {
        if (frequency != FrequencyType.DAILY && days.size() > 1) {
            throw new IllegalArgumentException("For weekly and monthly frequencies, only one day " + "may be specified.");
        }

        if (occurrences <= 0) {
            throw new IllegalArgumentException("Date pointer should have at least one occurrence planned.");
        }
    }
}
