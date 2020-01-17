package org.openmrs.module.messages.api.util.end.date;

import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimes;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimesParseFrequencyException;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimesParseOccurrenceException;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimesParser;

import java.util.Date;

public class AfterTimesEndDate implements EndDate {

    private EndDateParams params;

    public AfterTimesEndDate(EndDateParams params) {
        this.params = params;
    }

    @Override
    public Date getDate() {
        try {
            AfterTimesParser parser = new AfterTimesParser();
            AfterTimes afterTimes = new AfterTimes(
                    parser.parseWeekDays(params.getFrequency()),
                    parser.parseOccurrence(params.getValue()),
                    params.getStartDate());
            return afterTimes.get();
        } catch (AfterTimesParseFrequencyException | AfterTimesParseOccurrenceException e) {
            return null;
        }
    }
}
