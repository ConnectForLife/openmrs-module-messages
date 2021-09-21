package org.openmrs.module.messages.api.dao;

import org.openmrs.scheduler.TaskDefinition;

import java.util.List;

/**
 * The ExtendedSchedulerDao class.
 * <p>
 * This DAO provides an extension to {@link org.openmrs.scheduler.db.SchedulerDAO} functions, including more advanced way
 * to read {@link org.openmrs.scheduler.TaskDefinition}s.
 * </p>
 */
public interface ExtendedSchedulerDao {
    /**
     * Gets all Tasks which ware not executed (the {@code lastExecutionTime} is null) and the task's class name is equal
     * to the {@code className}.
     *
     * @param className the task class to get tasks for, not null
     * @return the list of tasks, never null
     */
    List<TaskDefinition> getNotExecutedTasksByClassName(String className);
}
