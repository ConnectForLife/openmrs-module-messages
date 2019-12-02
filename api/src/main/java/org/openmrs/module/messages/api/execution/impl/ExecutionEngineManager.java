package org.openmrs.module.messages.api.execution.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.execution.ExecutionEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * Class responsible for selecting the proper execution engine based on the execution type.
 */
public class ExecutionEngineManager {

    public static final String SQL_KEY = "SQL";

    private Map<String, String> register;

    public ExecutionEngineManager() {
        this.register = new HashMap<>();

        // TODO: this should be converted to a global variable later so that additional engines can be defined
        this.register.put(SQL_KEY, "messages.SqlExecutionEngine");
    }

    public ExecutionEngine getEngine(String engineKey) {
        String engineBeanKey = register.get(engineKey);

        ExecutionEngine engine = null;
        if (StringUtils.isNotBlank(engineBeanKey)) {
            engine = Context.getRegisteredComponent(engineBeanKey, ExecutionEngine.class);
        }
        return engine;
    }
}
