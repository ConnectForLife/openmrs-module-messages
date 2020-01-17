package org.openmrs.module.messages.api.util.end.date.after.times;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AfterTimesParser {

    public AfterTimesParser() {
    }

    public Set<Integer> parseWeekDays(List<String> days) throws AfterTimesParseFrequencyException {
        Set<Integer> frequency = new HashSet<>();
        if (days != null) {
            for (String day : days) {
                Integer dayOfWeek = DayOfWeek.valueOf(day.toUpperCase()).getValue();
                frequency.add(dayOfWeek);
            }
        }
        if (frequency.size() < 1) {
            throw new AfterTimesParseFrequencyException(
                    "Error frequency cannot be an empty set:" + days);
        }
        return frequency;
    }

    public Integer parseOccurrence(String occurrence) throws AfterTimesParseOccurrenceException {
        try {
            return Integer.parseInt(occurrence);
        } catch (NumberFormatException e) {
            throw new AfterTimesParseOccurrenceException(
                    "Error while parsing after times occurrence value: " + occurrence, e);
        }
    }
}
