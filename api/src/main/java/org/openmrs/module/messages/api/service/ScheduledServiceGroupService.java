package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.ScheduledService;
import org.openmrs.module.messages.api.model.ScheduledServiceGroup;

import java.util.Date;

/**
 * The ScheduledServiceGroupService Class.
 * <p>
 * This service provides general-purpose functionalities related to {@link ScheduledServiceGroup}.
 * </p>
 */
public interface ScheduledServiceGroupService {
    /**
     * Create new instance of {@link ScheduledServiceGroup} with single {@link ScheduledService} for the given {@code
     * deliveryTime}, {@code
     * channelType} and {@code
     * patientTemplate}.
     *
     * @param deliveryTime    the deliver time to schedule the group for, not null
     * @param channelType     the channel type to sue when delivering the service, not null
     * @param patientTemplate the Patient Template to use, not null
     * @return the new instance of ScheduledServiceGroup, never null
     */
    ScheduledServiceGroup createSingletonGroup(final Date deliveryTime, final String channelType,
                                               final PatientTemplate patientTemplate);
}
