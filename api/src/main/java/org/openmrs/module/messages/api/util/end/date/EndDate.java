package org.openmrs.module.messages.api.util.end.date;

import java.time.ZonedDateTime;

public interface EndDate {
    /**
     * @return end date time or null if not present or could not parse an end date
     */
    ZonedDateTime getDate();
}
