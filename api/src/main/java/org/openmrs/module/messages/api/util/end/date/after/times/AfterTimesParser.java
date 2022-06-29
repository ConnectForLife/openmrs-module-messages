/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util.end.date.after.times;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AfterTimesParser {

    public Set<Integer> parseWeekDays(List<String> days) throws AfterTimesParseFrequencyException {
        Set<Integer> frequency = new HashSet<>();
        if (days != null) {
            for (String day : days) {
                Integer dayOfWeek = DayOfWeek.valueOf(day.toUpperCase()).getValue();
                frequency.add(dayOfWeek);
            }
        }
        if (frequency.size() < 1) {
            throw new AfterTimesParseFrequencyException(
                    "Error frequency cannot be an empty set:" + days);
        }
        return frequency;
    }

    public Integer parseOccurrence(String occurrence) throws AfterTimesParseOccurrenceException {
        try {
            return Integer.parseInt(occurrence);
        } catch (NumberFormatException e) {
            throw new AfterTimesParseOccurrenceException(
                    "Error while parsing after times occurrence value: " + occurrence, e);
        }
    }
}
