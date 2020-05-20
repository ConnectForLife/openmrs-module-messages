package org.openmrs.module.messages.api.util.end.date;

import java.util.Date;

public class NoEndDate implements EndDate {

    @Override
    public Date getDate() {
        return null;
    }
}
