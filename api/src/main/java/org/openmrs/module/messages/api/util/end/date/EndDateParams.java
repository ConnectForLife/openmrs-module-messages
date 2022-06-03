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

import org.openmrs.module.messages.api.util.FrequencyType;

import java.time.ZonedDateTime;
import java.util.List;

public class EndDateParams {
    private String value;
    private ZonedDateTime startDate;
    private FrequencyType frequency;
    private List<String> daysOfWeek;

    public EndDateParams(String value) {
        this.value = value;
    }

    public EndDateParams(String value, ZonedDateTime startDate) {
        this.value = value;
        this.startDate = startDate;
    }

    public EndDateParams(String value, ZonedDateTime startDate, FrequencyType frequency,
                         List<String> daysOfWeek) {
        this.value = value;
        this.startDate = startDate;
        this.frequency = frequency;
        this.daysOfWeek = daysOfWeek;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public List<String> getDaysOfWeek() {
        return daysOfWeek;
    }

    public EndDateParams setDaysOfWeek(List<String> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
        return this;
    }

    public FrequencyType getFrequency() {
        return frequency;
    }

    public EndDateParams setFrequency(FrequencyType frequency) {
        this.frequency = frequency;
        return this;
    }

    @Override
    public String toString() {
        return "EndDateParams{" +
                "value='" + value + "'" +
                ", startDate=" + startDate +
                ", frequency=" + frequency +
                ", daysOfWeek=" + daysOfWeek +
                '}';
    }
}
