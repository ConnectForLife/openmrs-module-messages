package org.openmrs.module.messages.api.model.types;

/**
 * The enum which represents possible ScheduledService's statuses.
 * @see org.openmrs.module.messages.api.model.ScheduledService
 */
public enum ServiceStatus {
    /**
     * DELIVERY describes ScheduledServices which are already persisted in DB and the system got registerAttempt
     * response with success.
     */
    DELIVERED,

    /**
     * FAILED describes ScheduledServices which are already persisted in DB and the system got registerAttempt
     * response with no success.
     */
    FAILED,

    /**
     * FUTURE describes ScheduledServices which are not persisted in DB - it is part of
     * {@link org.openmrs.module.messages.api.execution.ExecutionEngine} results with not existence
     * as ScheduledService in DB.
     */
    FUTURE,

    /**
     * PENDING describes ScheduledServices which are already persisted in DB, but the system got no registerAttempt
     * response for it - it means that it must be scheduled or already processed by service.
     */
    PENDING
}
