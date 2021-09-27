/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;

import java.util.List;

/**
 * Provides the module configuration set
 */
public interface ConfigService {

    /**
     * Provides execution strategy {@link ReschedulingStrategy} for specific channel type
     *
     * @param channelType - name of specific channel type (case insensitive)
     * @return - the specified strategy or default
     *      {@link ConfigConstants#DEFAULT_RESCHEDULING_STRATEGY} if missing
     *      value for specified channel type
     */
    ReschedulingStrategy getReschedulingStrategy(String channelType);

    /**
     * Provides results handler {@link ServiceResultsHandlerService} for specific channel type
     *
     * @param channelType - name of specific channel type (case insensitive)
     * @return - the specified handler or default
     * {@link ConfigConstants#DEFAULT_SERVICE_RESULT_HANDLER} if missing
     * value for specified channel type
     */
    ServiceResultsHandlerService getResultsHandlerOrDefault(String channelType);

    /**
     * Provides the maximum number of attempts that can be taken for failing ScheduledServices.
     * After exceeding the number of retries, services will be denied rescheduling next task.
     *
     * @return - maximum number of attempts
     */
    int getMaxNumberOfAttempts();

    /**
     * Provides the interval (the time - in seconds) between next scheduling task for retry specific message.
     *
     * @return - interval in seconds
     */
    int getTimeIntervalToNextReschedule();

    /**
     * Provides information if the consent control is enabled. If control is enabled then module should verify if person
     * has consent before the message will be execute. Additionally this value should has impact for
     * the default value of person status attribute.
     *
     * @return - status of consent control
     */
    boolean isConsentControlEnabled();

    /**
     * Provides the coma separated list of relationship types ({@link org.openmrs.module.messages.api.model.ActorType})
     * used to control the list of possible targets of message (not including the patient as a target).
     *
     * @return coma separated list of relationship types
     */
    String getActorTypesConfiguration();

    /**
     * Provides the default value of actor relationship direction
     *
     * @return - the default value of direction
     */
    String getDefaultActorRelationDirection();

    /**
     * Provides the list of PersonStatus configuration {@link PersonStatusConfigDTO}
     *
     * @return - actual value of configuration for person statuses
     */
    List<PersonStatusConfigDTO> getPersonStatusConfigurations();

    /**
     * Provides the {@link PersonStatusConfigDTO} for specific person status
     *
     * @param personStatus - the looking person status
     * @return - the actual value of configuration or null if missing
     */
    PersonStatusConfigDTO getPersonStatusConfiguration(PersonStatus personStatus);

    /**
     * Provides the list of possible reason o changing status
     *
     * @return - the possible reasons
     */
    List<String> getPersonStatusPossibleChangeReasons();

    /**
     * Provides the list of the Callflow module statuses which describes ended flows.
     *
     * @return - statuses ending callflow
     */
    List<String> getStatusesEndingCallflow();
}
