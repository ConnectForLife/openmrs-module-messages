package org.openmrs.module.messages.api.execution.impl;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.execution.ExecutionEngine;

import java.util.List;

/**
 * Class responsible for selecting the proper execution engine based on the execution type.
 */
public class ExecutionEngineManager {

    public ExecutionEngine getEngine(String engineName) {
        // Don't cache it, the new modules can be added/removed during runtime
        final List<ExecutionEngine> executionEngines = Context.getRegisteredComponents(ExecutionEngine.class);

        for (final ExecutionEngine executionEngine : executionEngines) {
            if (engineName.equals(executionEngine.getName())) {
                return executionEngine;
            }
        }

        return null;
    }
}
