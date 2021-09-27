package org.openmrs.module.messages.api.util.end.date;

import org.openmrs.module.messages.api.util.EndDateType;

public class EndDateFactory {

    public EndDate create(EndDateType type, EndDateParams params) {
        switch (type) {
            case DATE_PICKER:
                return new SimpleEndDate(params);
            case AFTER_TIMES:
                return new AfterTimesEndDate(params);
            case OTHER:
                return new OtherEndDate(params);
            case NO_DATE:
            default:
                return new NoEndDate();
        }
    }
}
