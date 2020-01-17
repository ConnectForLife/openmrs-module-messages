package org.openmrs.module.messages.api.util.end.date;

import java.util.Date;
import java.util.List;

public class EndDateParams {
    private String value;
    private Date startDate;
    private List<String> frequency;

    public EndDateParams(String value) {
        this.value = value;
    }

    public EndDateParams(String value, Date startDate) {
        this.value = value;
        this.startDate = startDate;
    }

    public EndDateParams(String value, Date startDate, List<String> frequency) {
        this.value = value;
        this.startDate = startDate;
        this.frequency = frequency;
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

    public List<String> getFrequency() {
        return frequency;
    }

    public void setFrequency(List<String> frequency) {
        this.frequency = frequency;
    }
}
