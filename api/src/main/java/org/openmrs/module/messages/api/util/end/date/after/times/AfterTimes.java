package org.openmrs.module.messages.api.util.end.date.after.times;

import java.util.Date;
import java.util.Set;
import org.openmrs.module.messages.api.util.FrequencyType;

public class AfterTimes {

    private FrequencyType frequency;
    private final Set<Integer> days;
    private int occurrences;
    private DatePointer pointer;

    public AfterTimes(FrequencyType frequency, Set<Integer> days, int occurrences,
                      Date startDate) {
        validateDaysAndFrequency(frequency, days);
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
        setNearestDayOfWeek();
        int futureOccurrences = occurrences > 1 ? occurrences - 1 : 0;
        pointer.incDateByWeek(futureOccurrences);
        return pointer.getDate().toDate();
    }

    private Date getEndDateForMonthly() {
        int futureOccurrences = occurrences > 1 ? occurrences - 1 : 0;
        pointer.incDateByMonth(futureOccurrences);
        setNearestDayOfWeek();
        return pointer.getDate().toDate();
    }

    private void setNearestDayOfWeek() {
        while (!days.contains(pointer.getDate().getDayOfWeek())) {
            pointer.incDateByDay(1);
        }
    }

    private void validateDaysAndFrequency(FrequencyType frequency, Set<Integer> days) {
        if (frequency != FrequencyType.DAILY
            && days.size() > 1) {
            throw new IllegalArgumentException("For weekly and monthly frequencies, only one day " +
                "may be specified.");
        }
    }
}
