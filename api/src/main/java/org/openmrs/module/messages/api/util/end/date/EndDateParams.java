package org.openmrs.module.messages.api.util.end.date;

import java.util.Date;
import java.util.List;
import org.openmrs.module.messages.api.util.FrequencyType;

public class EndDateParams {
    private String value;
    private Date startDate;
    private FrequencyType frequency;
    private List<String> daysOfWeek;

    public EndDateParams(String value) {
        this.value = value;
    }

    public EndDateParams(String value, Date startDate) {
        this.value = value;
        this.startDate = startDate;
    }

    public EndDateParams(String value, Date startDate, FrequencyType frequency,
                         List<String> daysOfWeek) {
        this.value = value;
        this.startDate = startDate;
        this.frequency = frequency;
        this.daysOfWeek = daysOfWeek;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public EndDateParams setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        return this;
    }

    public FrequencyType getFrequency() {
        return frequency;
    }

    public EndDateParams setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
        return this;
    }
}
