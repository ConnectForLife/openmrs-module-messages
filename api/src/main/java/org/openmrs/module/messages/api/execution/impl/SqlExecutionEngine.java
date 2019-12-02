package org.openmrs.module.messages.api.execution.impl;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.SQLQuery;
import org.openmrs.api.APIException;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.messages.api.execution.ExecutionContext;
import org.openmrs.module.messages.api.execution.ExecutionEngine;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * The SQL execution engine is responsible for executing messages/services through the means of SQL queries.
 * Any SQL query can be executed. The query needs to return the data in proper order in order to be correctly
 * interpreted.
 */
public class SqlExecutionEngine implements ExecutionEngine {

    private static final Logger LOG = LoggerFactory.getLogger(SqlExecutionEngine.class);

    private DbSessionFactory dbSessionFactory;

    public SqlExecutionEngine(DbSessionFactory dbSessionFactory) {
        this.dbSessionFactory = dbSessionFactory;
    }

    @Override
    public ServiceResultList execute(ExecutionContext executionContext) throws ExecutionException {
        try {
            return executeQuery(executionContext);
        } catch (APIException e) {
            throw new ExecutionException("Error while executing the SQL template", e);
        }
    }

    private ServiceResultList executeQuery(ExecutionContext executionContext) {
        SQLQuery sqlQuery = dbSessionFactory.getCurrentSession().createSQLQuery(executionContext.getQuery());
        setParams(sqlQuery, executionContext.getParams());

        List<Object[]> result = sqlQuery.list();

        return ServiceResultList.createList(
                result,
                executionContext.getPatientTemplate(),
                executionContext.getDateRange()
        );
    }

    private void setParams(SQLQuery sqlQuery, Map<String, Object> params) {
        String[] namedParameters = sqlQuery.getNamedParameters();

        for (Map.Entry<String, Object> paramEntry : params.entrySet()) {
            String paramName = paramEntry.getKey();
            Object paramValue = paramEntry.getValue();

            if (ArrayUtils.contains(namedParameters, paramName)) {
                LOG.debug("Replacing parameter: {}", paramName);
                sqlQuery.setParameter(paramName, paramValue);
            } else {
                LOG.debug("Omitting not defined parameter: {}", paramName);
            }
        }
    }
}
