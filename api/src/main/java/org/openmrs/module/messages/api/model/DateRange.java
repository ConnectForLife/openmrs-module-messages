package org.openmrs.module.messages.api.model;

import java.util.Date;

public class DateRange {

    private final Date start;
    private final Date end;

    public DateRange(Date start, Date end) {
        this.start = createDate(start);
        this.end = createDate(end);
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    private Date createDate(Date d) {
        if (d == null) {
            return null;
        }
        return new Date(d.getYear(), d.getMonth(), d.getDate());
    }
}
