package org.openmrs.module.messages.api.execution;

import org.apache.commons.lang3.Range;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.Date;

/**
 * The service executor is responsible for executing a patient template, which means selecting the proper engine
 * and using it to run the query.
 */
public interface ServiceExecutor {

    ServiceResultList execute(PatientTemplate patientTemplate, Range<Date> dateRange) throws ExecutionException;
}
