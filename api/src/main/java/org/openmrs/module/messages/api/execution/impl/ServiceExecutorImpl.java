package org.openmrs.module.messages.api.execution.impl;

import org.openmrs.module.messages.api.execution.ExecutionContext;
import org.openmrs.module.messages.api.execution.ExecutionEngine;
import org.openmrs.module.messages.api.execution.ExecutionException;
import org.openmrs.module.messages.api.execution.ServiceExecutor;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

public class ServiceExecutorImpl implements ServiceExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceExecutorImpl.class);

    private ExecutionEngineManager executionEngineManager;

    public ServiceExecutorImpl(ExecutionEngineManager executionEngineManager) {
        this.executionEngineManager = executionEngineManager;
    }

    @Transactional
    @Override
    public ServiceResultList execute(PatientTemplate patientTemplate, Range<Date> dateRange) throws ExecutionException {
        ExecutionEngine executionEngine = getEngine(patientTemplate);

        ExecutionContext executionContext = new ExecutionContext(patientTemplate, dateRange);

        LOG.debug("Executing template for service {} using query engine {}",
                patientTemplate.getServiceId(), executionContext.getClass().getName());

        return executionEngine.execute(executionContext);
    }

    private ExecutionEngine getEngine(PatientTemplate patientTemplate) throws ExecutionException {
        String engineKey = patientTemplate.getServiceQueryType();
        ExecutionEngine engine = executionEngineManager.getEngine(engineKey);
        if (engine == null) {
            throw new ExecutionException("Unknown query type: " + engineKey);
        }
        return engine;
    }
}
