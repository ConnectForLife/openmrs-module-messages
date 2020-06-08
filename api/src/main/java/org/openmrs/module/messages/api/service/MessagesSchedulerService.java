package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;

import java.util.Date;

/**
 * Provides methods related to job scheduling
 */
public interface MessagesSchedulerService extends OpenmrsService {

    /**
     * Reschedules a task if already exists, otherwise creates a new scheduled task.
     * Start date is set to now.
     *
     * @param jobDefinition object containing the important data about job
     * @param intervalInSecond interval between job executions (represented in seconds)
     */
    void rescheduleOrCreateNewTask(JobDefinition jobDefinition, Long intervalInSecond);

    /**
     * Creates a new scheduled task
     *
     * @param jobDefinition object containing the important data about job
     * @param startTime date when the scheduled task will start from
     * @param repeatInterval interval between job executions
     */
    void createNewTask(JobDefinition jobDefinition, Date startTime,
                       JobRepeatInterval repeatInterval);

    /**
     * Setting up a token for daemon execution
     *
     * @param daemonToken daemon token
     */
    void setDaemonToken(DaemonToken daemonToken);
}
