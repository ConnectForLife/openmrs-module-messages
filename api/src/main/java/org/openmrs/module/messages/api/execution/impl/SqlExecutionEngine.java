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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.messages.api.execution.ExecutionContext;
import org.openmrs.module.messages.api.execution.ExecutionEngine;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.util.StopwatchUtil;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import static org.openmrs.module.messages.api.constants.MessagesConstants.PERFORMANCE_LOGGER;

/**
 * The SQL execution engine is responsible for executing messages/services through the means of SQL
 * queries. Any SQL query can be executed. The query needs to return the data in proper order in
 * order to be correctly interpreted.
 */
public class SqlExecutionEngine implements ExecutionEngine {
  public static final String NAME = "SQL";

  private static final Log LOG = LogFactory.getLog(SqlExecutionEngine.class);

  private DbSessionFactory dbSessionFactory;

  public SqlExecutionEngine(DbSessionFactory dbSessionFactory) {
    this.dbSessionFactory = dbSessionFactory;
  }

  @Override
  public List<ServiceResultList> executeTemplate(ExecutionContext executionContext)
      throws ExecutionException {
    try {
      return executeTemplateQuery(executionContext);
    } catch (Exception e) {
      throw new ExecutionException("Error while executing the SQL template", e);
    }
  }

  private List<ServiceResultList> executeTemplateQuery(ExecutionContext executionContext) {
    final StopwatchUtil queryStopwatch = new StopwatchUtil();
    SQLQuery sqlQuery =
        dbSessionFactory
            .getCurrentSession()
            .createSQLQuery(executionContext.getTemplate().getServiceQuery());
    sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    setParams(sqlQuery, executionContext.getParams());

    List<Map<String, Object>> result = sqlQuery.list();

    PERFORMANCE_LOGGER.info(
        MessageFormat.format(
            "SQL for {0} returned {1} results in: {2}ms",
            executionContext.getTemplate() != null
                ? executionContext.getTemplate().getName()
                : "<no template set>",
            result.size(),
            queryStopwatch.stop().toMillis()));

    return ServiceResultList.createList(
        result, executionContext.getTemplate(), executionContext.getDateRange());
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public ServiceResultList execute(ExecutionContext executionContext, boolean isCalendarQuery)
      throws ExecutionException {
    try {
      return executeQuery(executionContext, isCalendarQuery);
    } catch (Exception e) {
      throw new ExecutionException("Error while executing the SQL template", e);
    }
  }

  private ServiceResultList executeQuery(
      ExecutionContext executionContext, boolean isCalendarQuery) {
    SQLQuery sqlQuery =
        dbSessionFactory
            .getCurrentSession()
            .createSQLQuery(executionContext.getPatientTemplate().getQuery(isCalendarQuery));
    sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
    setParams(sqlQuery, executionContext.getParams());

    List<Map<String, Object>> result = sqlQuery.list();

    return ServiceResultList.createList(
        result, executionContext.getPatientTemplate(), executionContext.getDateRange());
  }

  private void setParams(SQLQuery sqlQuery, Map<String, Object> params) {
    String[] namedParameters = sqlQuery.getNamedParameters();

    for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
      String paramName = paramEntry.getKey();
      Object paramValue = paramEntry.getValue();

      if (ArrayUtils.contains(namedParameters, paramName)) {
        logConsumedParameterInfo(paramName, paramValue);
        sqlQuery.setParameter(paramName, paramValue);
      } else {
        logNotConsumedParameterInfo(paramName, paramValue);
      }
    }
  }

  private void logConsumedParameterInfo(String paramName, Object paramValue) {
    if (LOG.isTraceEnabled()) {
      LOG.trace(String.format("Replacing parameter: %s = %s", paramName, paramValue));
    }
  }

  private void logNotConsumedParameterInfo(String paramName, Object paramValue) {
    if (LOG.isTraceEnabled()) {
      LOG.trace(
          String.format("The SQL does not consume parameter: %s = %s", paramName, paramValue));
    }
  }
}
