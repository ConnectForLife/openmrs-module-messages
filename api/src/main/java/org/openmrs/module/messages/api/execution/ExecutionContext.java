/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.openmrs.Person;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.util.DateUtil;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * This context is passed to service execution engines. It contains the patient template, the date range
 * and is responsible for preparing the params that will be passed to the execution.
 */
public class ExecutionContext {

    public static final String START_DATE_TIME_PARAM = "startDateTime";
    public static final String END_DATE_TIME_PARAM = "endDateTime";
    public static final String PATIENT_ID_PARAM = "patientId";
    public static final String ACTOR_ID_PARAM = "actorId";
    public static final String BEST_CONTACT_TIME_PARAM = "bestContactTime";
    public static final String EXECUTION_START_DATE_TIME = "executionStartDateTime";

    private Map<String, Object> params;
    private PatientTemplate patientTemplate;
    private Range<ZonedDateTime> dateRange;
    private String bestContactTime;
    private ZonedDateTime executionStartDateTime;
    private Template template;

    public ExecutionContext(PatientTemplate patientTemplate, Range<ZonedDateTime> dateTimeRange, String bestContactTime,
                            ZonedDateTime executionStartDateTime) {
        this.patientTemplate = patientTemplate;
        this.bestContactTime = bestContactTime;
        setExecutionStartDateTime(executionStartDateTime, patientTemplate.getActor());
        setRange(dateTimeRange);

        putParam(PATIENT_ID_PARAM, patientTemplate.getPatient().getPatientId());
        putParam(ACTOR_ID_PARAM, patientTemplate.getActor().getPersonId());
        putParam(BEST_CONTACT_TIME_PARAM, bestContactTime);

        for (TemplateFieldValue param : patientTemplate.getTemplateFieldValues()) {
            putParam(param.getTemplateField().getName().replace(' ', '_'), param.getValue());
        }
    }

    public ExecutionContext(Template template, Range<ZonedDateTime> dateRange) {
        this.template = template;
        this.dateRange = dateRange;

        putParam(START_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(this.dateRange.getStart()));
        putParam(END_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(this.dateRange.getEnd()));
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public PatientTemplate getPatientTemplate() {
        return patientTemplate;
    }

    public Range<ZonedDateTime> getDateRange() {
        return dateRange;
    }

    public String getBestContactTime() {
        return bestContactTime;
    }

    public ZonedDateTime getExecutionStartDateTime() {
        return executionStartDateTime;
    }

    private void setExecutionStartDateTime(ZonedDateTime executionStartDateTime, Person actor) {
        if (executionStartDateTime == null) {
            this.executionStartDateTime = DateUtil.now();
        } else {
            this.executionStartDateTime = executionStartDateTime;
        }
    putParam(
        EXECUTION_START_DATE_TIME,
        this.executionStartDateTime
            .withZoneSameInstant(DateUtil.getPersonTimeZone(actor))
            .format(
                DateTimeFormatter.ofPattern(
                    MessagesConstants.DEFAULT_SERVER_SIDE_DATETIME_FORMAT)));
    }

    public Template getTemplate() {
        return template;
    }

    private void setRange(Range<ZonedDateTime> dateTimeRange) {
        this.dateRange = new Range<>(getStartDateTime(dateTimeRange), getEndDateTime(dateTimeRange));
        putParam(START_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(this.dateRange.getStart()));
        putParam(END_DATE_TIME_PARAM, DateUtil.formatToServerSideDateTime(this.dateRange.getEnd()));
    }

    private void putParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }

    private ZonedDateTime getStartDateTime(Range<ZonedDateTime> dateTimeRange) {
        final ZonedDateTime startDate = patientTemplate.getStartOfMessages();

        if (startDate == null || startDate.isBefore(dateTimeRange.getStart())) {
            return dateTimeRange.getStart();
        } else {
            return startDate;
        }
    }

    private ZonedDateTime getEndDateTime(Range<ZonedDateTime> dateTimeRange) {
        final ZonedDateTime endDate = patientTemplate.getEndOfMessages();

        if (endDate == null || endDate.isAfter(dateTimeRange.getEnd())) {
            return dateTimeRange.getEnd();
        } else {
            return endDate;
        }
    }
}
