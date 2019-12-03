package org.openmrs.module.messages.api.scheduler.job;

import org.openmrs.scheduler.tasks.AbstractTask;

public abstract class JobDefinition extends AbstractTask {

    public abstract boolean shouldStartAtFirstCreation();

    public abstract String getTaskName();

    public abstract Class getTaskClass();
}
