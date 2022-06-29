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
