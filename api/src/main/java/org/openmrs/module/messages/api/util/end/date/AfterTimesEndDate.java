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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimes;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimesParseFrequencyException;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimesParseOccurrenceException;
import org.openmrs.module.messages.api.util.end.date.after.times.AfterTimesParser;

import java.time.ZonedDateTime;

public class AfterTimesEndDate implements EndDate {
    private static final Log LOGGER = LogFactory.getLog(AfterTimesEndDate.class);

    private EndDateParams params;

    public AfterTimesEndDate(EndDateParams params) {
        this.params = params;
    }

    @Override
    public ZonedDateTime getDate() {
        try {
            AfterTimesParser parser = new AfterTimesParser();
            AfterTimes afterTimes = new AfterTimes(
                    params.getFrequency(),
                    parser.parseWeekDays(params.getDaysOfWeek()),
                    parser.parseOccurrence(params.getValue()),
                    params.getStartDate());
            return afterTimes.get();
        } catch (AfterTimesParseFrequencyException | AfterTimesParseOccurrenceException e) {
            LOGGER.error("AfterTimesEndDate failed!", e);
            return null;
        }
    }
}
