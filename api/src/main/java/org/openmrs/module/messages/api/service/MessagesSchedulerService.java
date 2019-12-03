package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;

public interface MessagesSchedulerService extends OpenmrsService {

    void rescheduleOrCreateNewTask(JobDefinition jobDefinition, JobRepeatInterval repeatInterval);

    void setDaemonToken(DaemonToken daemonToken);
}
