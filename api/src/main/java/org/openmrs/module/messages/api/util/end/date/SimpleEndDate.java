package org.openmrs.module.messages.api.util.end.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;

public class SimpleEndDate implements EndDate {

    private EndDateParams params;

    public SimpleEndDate(EndDateParams params) {
        this.params = params;
    }

    @Override
    public Date getDate() {
        try {
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_SERVER_SIDE_DATE_FORMAT);
            return newDateFormat.parse(params.getValue());
        } catch (ParseException | NullPointerException e) {
            return null;
        }
    }
}
