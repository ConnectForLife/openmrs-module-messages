/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.openmrs.module.messages.api.util.DateUtil;

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

    private Map<String, Object> params;
    private PatientTemplate patientTemplate;
    private Range<Date> dateRange;
    private String bestContactTime;

    public ExecutionContext(PatientTemplate patientTemplate, Range<Date> dateTimeRange, String bestContactTime) {
        this.patientTemplate = patientTemplate;
        this.bestContactTime = bestContactTime;
        setRange(dateTimeRange);

        putParam(PATIENT_ID_PARAM, patientTemplate.getPatient().getPatientId());
        putParam(ACTOR_ID_PARAM, patientTemplate.getActor().getPersonId());
        putParam(BEST_CONTACT_TIME_PARAM, bestContactTime);

        for (TemplateFieldValue param : patientTemplate.getTemplateFieldValues()) {
            putParam(param.getTemplateField().getName().replace(' ', '_'), param.getValue());
        }
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public PatientTemplate getPatientTemplate() {
        return patientTemplate;
    }

    public Range<Date> getDateRange() {
        return dateRange;
    }

    public String getBestContactTime() {
        return bestContactTime;
    }

    public String getQuery() {
        return patientTemplate.getServiceQuery();
    }

    private void setRange(Range<Date> dateTimeRange) {
        this.dateRange = new Range<>(getStartDateTime(dateTimeRange), getEndDateTime(dateTimeRange));
        putParam(START_DATE_TIME_PARAM, DateUtil.convertToServerSideDateTime(this.dateRange.getStart()));
        putParam(END_DATE_TIME_PARAM, DateUtil.convertToServerSideDateTime(this.dateRange.getEnd()));
    }

    private void putParam(String key, Object value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
    }

    private Date getStartDateTime(Range<Date> dateTimeRange) {
        Date startDate = patientTemplate.getStartOfMessages();
        if (startDate == null || startDate.before(dateTimeRange.getStart())) {
            return dateTimeRange.getStart();
        } else {
            return startDate;
        }
    }

    private Date getEndDateTime(Range<Date> dateTimeRange) {
        Date endDate = patientTemplate.getEndOfMessages();
        if (endDate == null || endDate.after(dateTimeRange.getEnd())) {
            return dateTimeRange.getEnd();
        } else {
            return endDate;
        }
    }
}
