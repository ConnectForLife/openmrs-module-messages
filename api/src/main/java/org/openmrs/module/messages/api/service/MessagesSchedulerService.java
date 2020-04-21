package org.openmrs.module.messages.api.service;

import java.util.Date;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.messages.api.scheduler.job.JobDefinition;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;

/**
 * Provides methods related to job scheduling
 */
public interface MessagesSchedulerService extends OpenmrsService {

    /**
     * Reschedules a task if already exists, otherwise creates a new scheduled task.
     * Start date is set to now.
     *
     * @param jobDefinition object containing the important data about job
     * @param repeatInterval interval between job executions
     */
    void rescheduleOrCreateNewTask(JobDefinition jobDefinition, JobRepeatInterval repeatInterval);

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
