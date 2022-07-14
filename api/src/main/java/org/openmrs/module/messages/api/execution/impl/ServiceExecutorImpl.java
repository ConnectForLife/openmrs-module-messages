/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.execution.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.SQLGrammarException;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.execution.ExecutionContext;
import org.openmrs.module.messages.api.execution.ExecutionEngine;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.util.BestContactTimeHelper;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

public class ServiceExecutorImpl extends BaseOpenmrsService implements ServiceExecutor {

    private static final Log LOG = LogFactory.getLog(ServiceExecutorImpl.class);

    private ExecutionEngineManager executionEngineManager;

    public ServiceExecutorImpl(ExecutionEngineManager executionEngineManager) {
        this.executionEngineManager = executionEngineManager;
    }

    /**
     * Executes a patient template {@link PatientTemplate} and result result as a {@link ServiceResultList}.
     *
     * @param patientTemplate - provided patient template
     * @param dateTimeRange   - date time range for executed query
     * @return - list of results. The result list contains the Service results according to provided dateTime range.
     * Services which weren't executed should have the status set as FUTURE.
     * @throws ExecutionException - exception occurred during service execution
     */
    @Transactional(noRollbackFor = {RuntimeException.class, SQLGrammarException.class}, readOnly = true)
    @Override
    public ServiceResultList execute(PatientTemplate patientTemplate, Range<ZonedDateTime> dateTimeRange)
            throws ExecutionException {
        return execute(patientTemplate, dateTimeRange, null, false, null);
    }

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
  @Transactional(
      noRollbackFor = {RuntimeException.class, SQLGrammarException.class},
      readOnly = true)
  @Override
  public ServiceResultList execute(
      PatientTemplate patientTemplate,
      Range<ZonedDateTime> dateTimeRange,
      ZonedDateTime executionStartDateTime,
      boolean isCalendarQuery,
      String patientBestContactTime)
      throws ExecutionException {
    ExecutionEngine executionEngine = getEngine(patientTemplate, null);

    ExecutionContext executionContext =
        new ExecutionContext(
            patientTemplate,
            dateTimeRange,
            BestContactTimeHelper.getBestContactTime(patientTemplate.getActor()),
            executionStartDateTime);

    logExecutingInfo(patientTemplate, executionEngine);
    return executionEngine.execute(executionContext, isCalendarQuery);
  }

    @Transactional(noRollbackFor = {RuntimeException.class, SQLGrammarException.class}, readOnly = true)
    @Override
    public List<ServiceResultList> executeTemplate(Template template, Range<ZonedDateTime> dateTimeRange)
            throws ExecutionException {
        ExecutionEngine executionEngine = getEngine(null, template);

        ExecutionContext executionContext = new ExecutionContext(template, dateTimeRange);

        return executionEngine.executeTemplate(executionContext);

    }

    private ExecutionEngine getEngine(PatientTemplate patientTemplate, Template template) throws ExecutionException {
        String engineKey = null;
        if (patientTemplate == null) {
            engineKey = template.getServiceQueryType();
        }
        if (template == null) {
            engineKey = patientTemplate.getServiceQueryType();
        }

        ExecutionEngine engine = executionEngineManager.getEngine(engineKey);
        if (engine == null) {
            throw new ExecutionException("Unknown query type: " + engineKey);
        }
        return engine;
    }

    private void logExecutingInfo(PatientTemplate patientTemplate, ExecutionEngine executionEngine) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                    String.format("Executing template '%s' using query engine: %s", patientTemplate.getTemplate().getName(),
                            executionEngine.getClass().getName()));
        }
    }
}
