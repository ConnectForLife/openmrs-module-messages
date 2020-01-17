package org.openmrs.module.messages.api.util.end.date.after.times;

import java.util.Date;
import java.util.Set;

public class AfterTimes {

    private Set<Integer> frequency;
    private int occurrences;
    private DatePointer pointer;

    public AfterTimes(Set<Integer> frequency, int occurrences, Date startDate) {
        this.frequency = frequency;
        this.occurrences = occurrences;
        this.pointer = new DatePointer(0, startDate);
    }

    public Date get() {
        while (pointer.getCounter() < occurrences) {
            int currentWeekday = pointer.getDate().getDayOfWeek();
            if (frequency.contains(currentWeekday)) {
                pointer.incCounter(1);
            }
            if (pointer.getCounter() != occurrences) {
                pointer.incDate(1);
            }
        }
        return pointer.getDate().toDate();
    }
}
