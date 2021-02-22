package org.openmrs.module.messages.api.execution;

/**
 * An execution engine is responsible for providing a list of executions for a given service/message.
 * This list of executions needs to contain the dates on which the service/messages will fire.
 */
public interface ExecutionEngine {

    ServiceResultList execute(ExecutionContext executionContext, boolean isCalendarQuery) throws ExecutionException;
}
