package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.service.GraphService;

import java.util.List;
import java.util.Map;

public class GraphServiceImpl implements GraphService {

    private static final Log LOGGER = LogFactory.getLog(GraphServiceImpl.class);

    private DbSessionFactory dbSessionFactory;

    public GraphServiceImpl(DbSessionFactory dbSessionFactory) {
        this.dbSessionFactory = dbSessionFactory;
    }

    @Override
    public List<Object> processQuery(String sqlQuery, Map<String, Object> queryParams) throws ExecutionException {
        try {
            return prepareAndExecuteQuery(sqlQuery, queryParams);
        } catch (Exception ex) {
            throw new ExecutionException("Error during executing SQL query", ex);
        }
    }

    private List<Object> prepareAndExecuteQuery(String query, Map<String, Object> queryParams) {
        SQLQuery sqlQuery = dbSessionFactory.getCurrentSession()
                .createSQLQuery(query);
        sqlQuery.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        setParams(sqlQuery, queryParams);

        return sqlQuery.list();
    }

    private void setParams(SQLQuery sqlQuery, Map<String, Object> queryParams) {
        String[] namedParameters = sqlQuery.getNamedParameters();

        for (Map.Entry<String, Object> param : queryParams.entrySet()) {
            String paramName = param.getKey();
            Object paramValue = param.getValue();

            if (ArrayUtils.contains(namedParameters, paramName)) {
                sqlQuery.setParameter(paramName, paramValue);
                logProperParamSetting(paramName, paramValue);
            } else {
                logNotProperParamSetting(paramName, paramValue);
            }
        }
    }

    private void logProperParamSetting(String paramName, Object paramValue) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace(String.format("Parameter %s has been set correctly with the value %s", paramName, paramValue));
        }
    }

    private void logNotProperParamSetting(String paramName, Object paramValue) {
        if (LOGGER.isWarnEnabled()) {
            LOGGER.warn(String.format("Parameter %s has not been set correctly with the value %s because no such " +
                    "parameter was found", paramName, paramValue));
        }
    }

}
