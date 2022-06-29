/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
