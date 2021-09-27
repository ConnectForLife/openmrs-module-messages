package org.openmrs.module.messages.api.util.end.date;

import java.time.ZonedDateTime;

public class NoEndDate implements EndDate {

    @Override
    public ZonedDateTime getDate() {
        return null;
    }
}
