/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.scheduler.job;

import java.util.HashMap;
import java.util.Map;
import org.openmrs.scheduler.tasks.AbstractTask;

public abstract class JobDefinition extends AbstractTask {

    public abstract boolean shouldStartAtFirstCreation();

    public abstract String getTaskName();

    public abstract Class getTaskClass();

    public Map<String, String> getProperties() {
        return new HashMap<>();
    }
}
