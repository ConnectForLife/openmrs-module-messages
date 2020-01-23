package org.openmrs.module.messages.api.util.end.date.after.times;

import java.util.Date;
import java.util.Set;

import org.joda.time.LocalDate;
import org.openmrs.module.messages.api.util.FrequencyType;

public class AfterTimes {

    private FrequencyType frequency;
    private final Set<Integer> days;
    private int occurrences;
    private DatePointer pointer;

    public AfterTimes(FrequencyType frequency, Set<Integer> days, int occurrences,
                      Date startDate) {
        validateDaysAndFrequency(frequency, days, occurrences);
        this.frequency = frequency;
        this.days = days;
        this.occurrences = occurrences;
        this.pointer = new DatePointer(0, startDate);
    }

    public Date get() {
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
                throw new IllegalArgumentException(String.format("Frequency %s is not supported",
                    frequency.getName()));
        }
    }

    private Date getEndDateForDaily() {
        while (pointer.getCounter() < occurrences) {
            int currentWeekday = pointer.getDate().getDayOfWeek();
            if (days.contains(currentWeekday)) {
                pointer.incCounter(1);
            }
            if (pointer.getCounter() != occurrences) {
                pointer.incDateByDay(1);
            }
        }
        return pointer.getDate().toDate();
    }

    private Date getEndDateForWeekly() {
        setNearestWeekDay();
        int futureOccurrences = occurrences > 1 ? occurrences - 1 : 0;
        pointer.incDateByWeek(futureOccurrences);
        return pointer.getDate().toDate();
    }

    private Date getEndDateForMonthly() {
        final int lastOccurrence = 1;
        int occurrencesBetween = occurrences - pointer.getCounter() - lastOccurrence;
        if (occurrencesBetween > 0) {
            pointer.incDateByMonth(occurrencesBetween);
        }
        setNearestWeekdayOfMonth();
        return pointer.getDate().toDate();
    }

    private void setNearestWeekDay() {
        while (!days.contains(pointer.getDate().getDayOfWeek())) {
            pointer.incDateByDay(1);
        }
    }

    private void setNearestWeekdayOfMonth() {
        LocalDate startMonthWeekday = getFirstWeekdayOfMonth(pointer.getDate());
        if (pointer.getDate().isBefore(startMonthWeekday)) {
            pointer = new DatePointer(pointer.getCounter() + 1, startMonthWeekday);
        } else {
            setNextMonthWeekday();
        }
    }

    private void setNextMonthWeekday() {
        pointer.incDateToBeginningOfNextMonth();
        LocalDate nextMonthWeekday = getFirstWeekdayOfMonth(pointer.getDate());
        pointer = new DatePointer(pointer.getCounter() + 1, nextMonthWeekday);
    }

    private LocalDate getFirstWeekdayOfMonth(LocalDate date) {
        LocalDate monthDate = new LocalDate(date.getYear(), date.getMonthOfYear(), 1);
        while (!days.contains(monthDate.getDayOfWeek())) {
            monthDate = monthDate.plusDays(1);
        }
        return monthDate;
    }

    private void validateDaysAndFrequency(FrequencyType frequency, Set<Integer> days, int occurrences) {
        if (frequency != FrequencyType.DAILY
            && days.size() > 1) {
            throw new IllegalArgumentException("For weekly and monthly frequencies, only one day " +
                "may be specified.");
        }

        if (occurrences <= 0) {
            throw new IllegalArgumentException("Date pointer should have at least one occurrence planned.");
        }
    }
}
