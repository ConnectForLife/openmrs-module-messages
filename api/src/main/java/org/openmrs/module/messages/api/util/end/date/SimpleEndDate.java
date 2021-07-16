package org.openmrs.module.messages.api.util.end.date;

import org.openmrs.module.messages.api.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_SERVER_SIDE_DATE_FORMAT;

public class SimpleEndDate implements EndDate {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEndDate.class);

    private EndDateParams params;

    public SimpleEndDate(EndDateParams params) {
        this.params = params;
    }

    @Override
    public ZonedDateTime getDate() {
        try {
            final LocalDate localDate =
                    LocalDate.parse(params.getValue(), DateTimeFormatter.ofPattern(DEFAULT_SERVER_SIDE_DATE_FORMAT));
            return ZonedDateTime.of(localDate, LocalTime.MIDNIGHT,
                    params.getStartDate() != null ? params.getStartDate().getZone() : DateUtil.getDefaultUserTimeZone());
        } catch (DateTimeParseException dtpe) {
            LOGGER.debug("Ignored invalid date time value: " + params.getValue(), dtpe);
            return null;
        }
    }
}
