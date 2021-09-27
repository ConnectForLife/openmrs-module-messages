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
