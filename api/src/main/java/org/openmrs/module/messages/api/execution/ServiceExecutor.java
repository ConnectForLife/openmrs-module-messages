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

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * The service executor is responsible for executing a patient template, which means selecting the proper engine
 * and using it to run the query.
 */
public interface ServiceExecutor extends OpenmrsService {

    /**
     * Executes a patient template {@link PatientTemplate} and result result as a {@link ServiceResultList}.
     *
     * @param patientTemplate - provided patient template
     * @param dateTimeRange   - date time range for executed query
     * @return - list of results. The result list contains the Service results according to provided dateTime range.
     * Services which weren't executed should have the status set as FUTURE.
     * @throws ExecutionException - exception occurred during service execution
     */
    ServiceResultList execute(PatientTemplate patientTemplate, Range<ZonedDateTime> dateTimeRange) throws ExecutionException;

  /**
   * Executes a patient template {@link PatientTemplate} and result result as a {@link
   * ServiceResultList}.
   *
   * @param patientTemplate - provided patient template
   * @param dateTimeRange - date time range for executed query
   * @param executionStartDateTime - date time of starting execution
   * @param isCalendarQuery - determines if calendar or scheduler job service query should be
   *     executed
   * @param patientBestContactTime - patient's best contact time
   * @return - list of results. The result list contains the Service results according to provided
   *     dateTime range. Services which weren't executed should have the status set as FUTURE.
   * @throws ExecutionException - exception occurred during service execution
   */
  ServiceResultList execute(
      PatientTemplate patientTemplate,
      Range<ZonedDateTime> dateTimeRange,
      ZonedDateTime executionStartDateTime,
      boolean isCalendarQuery,
      String patientBestContactTime)
      throws ExecutionException;

    /**
     * Executes a template {@link Template} if contains and enables optimized query.
     * Collects results into list of {@link ServiceResultList}
     *
     * @param template      - provided template
     * @param dateTimeRange - date time range for executed template service query
     * @return results retrieved from query parsed into list of {@link ServiceResultList}
     * @throws ExecutionException - exception occurred during service execution
     */
    List<ServiceResultList> executeTemplate(Template template, Range<ZonedDateTime> dateTimeRange) throws ExecutionException;
}
