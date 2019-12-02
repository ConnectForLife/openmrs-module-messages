package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.Range;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * This context is passed to service execution engines. It contains the patient template, the date range
 * and is responsible for preparing the params that will be passed to the execution.
 */
public class ExecutionContext {

    public static final String START_DATE_PARAM = "startDate";
    public static final String END_DATE_PARAM = "endDate";

    private Map<String, Object> params;
    private PatientTemplate patientTemplate;
    private Range<Date> dateRange;

    public ExecutionContext(PatientTemplate patientTemplate, Range<Date> dateRange) {
        this.patientTemplate = patientTemplate;
        this.dateRange = dateRange;

        // TODO: after relation to template field value is fixed
        params = new HashMap<>();

        params.put(START_DATE_PARAM, dateRange.getMinimum());
        params.put(END_DATE_PARAM, dateRange.getMaximum());
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

    public String getQuery() {
        return patientTemplate.getServiceQuery();
    }
}
