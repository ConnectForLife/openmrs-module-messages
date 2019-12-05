package org.openmrs.module.messages.api.execution;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

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
    public static final String PATIENT_ID_PARAM = "patientId";
    public static final String ACTOR_ID_PARAM = "actorId";

    private Map<String, Object> params;
    private PatientTemplate patientTemplate;
    private Range<Date> dateRange;

    public ExecutionContext(PatientTemplate patientTemplate, Range<Date> dateRange) {
        this.patientTemplate = patientTemplate;
        this.dateRange = dateRange;

        params = new HashMap<>();

        params.put(START_DATE_PARAM, dateRange.getStart());
        params.put(END_DATE_PARAM, dateRange.getEnd());

        params.put(PATIENT_ID_PARAM, patientTemplate.getPatient().getPatientId());
        params.put(ACTOR_ID_PARAM, patientTemplate.getActor().getPersonId());

        for (TemplateFieldValue param : patientTemplate.getTemplateFieldValues()) {
            params.put(param.getTemplateField().getName(), param.getValue());
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

    public String getQuery() {
        return patientTemplate.getServiceQuery();
    }
}
