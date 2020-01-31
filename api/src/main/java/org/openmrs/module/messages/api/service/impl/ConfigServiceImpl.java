/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.PersonStatusConfigDTO;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.util.GlobalPropertyUtil;
import org.openmrs.module.messages.api.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides the default implementation of module configuration set
 */
public class ConfigServiceImpl implements ConfigService {

    private static final Log LOGGER = LogFactory.getLog(ConfigServiceImpl.class);

    private static final String COMMA_DELIMITER = ",";

    /**
     * Provides execution strategy {@link ReschedulingStrategy} for specific channel type
     * @param channelType - name of specific channel type (case insensitive)
     * @return - the specified strategy or default
     *      {@link ConfigConstants#DEFAULT_RESCHEDULING_STRATEGY} if missing
     *      value for specified channel type
     */
    @Override
    public ReschedulingStrategy getReschedulingStrategy(String channelType) {
        String gpName = ConfigConstants.RESCHEDULING_STRATEGY_KEY;
        Map<String, String> strategyByChannel = GlobalPropertyUtil.parseMap(gpName, getGp(gpName));

        String beanName = strategyByChannel.get(channelType.toUpperCase());
        if (StringUtils.isBlank(beanName)) {
            LOGGER.warn(String.format("Rescheduling strategy for channelType=%s is not defined in GP. " +
                    "The default strategy will be used: %s", channelType, gpName));
            beanName = ConfigConstants.DEFAULT_RESCHEDULING_STRATEGY;
        }
        return Context.getRegisteredComponent(beanName, ReschedulingStrategy.class);
    }

    /**
     * Provides the maximum number of attempts that can be taken for failing ScheduledServices.
     * After exceeding the number of retries, services will be denied rescheduling next task.
     * @return - maximum number of attempts
     */
    @Override
    public int getMaxNumberOfRescheduling() {
        String gpName = ConfigConstants.MAX_NUMBER_OF_RESCHEDULING_KEY;
        return GlobalPropertyUtil.parseInt(gpName, getGp(gpName));
    }

    /**
     * Provides the interval (the time - in seconds) between next scheduling task for retry specific message.
     * @return - interval in seconds
     */
    @Override
    public int getTimeIntervalToNextReschedule() {
        String gpName = ConfigConstants.TIME_INTERVAL_TO_NEXT_RESCHEDULE_KEY;
        return GlobalPropertyUtil.parseInt(gpName, getGp(gpName));
    }

    /**
     * Provides information if the consent control is enabled. If control is enabled then module should verify if person
     * has consent before the message will be execute. Additionally this value should has impact for
     * the default value of person status attribute.
     * @return - status of consent control
     */
    @Override
    public boolean isConsentControlEnabled() {
        return GlobalPropertyUtil.parseBool(getGp(ConfigConstants.CONSENT_CONTROL_KEY));
    }

    /**
     * Provides the coma separated list of relationship types ({@link org.openmrs.module.messages.api.model.ActorType})
     * used to control the list of possible targets of message (not including the patient as a target).
     * @return coma separated list of relationship types
     */
    @Override
    public String getActorTypesConfiguration() {
        return getGp(ConfigConstants.ACTOR_TYPES_KEY);
    }

    /**
     * Provides the default value of actor relationship direction
     * @return - the default value of direction
     */
    @Override
    public String getDefaultActorRelationDirection() {
        return RelationshipTypeDirection.A.name();
    }

    /**
     * Provides the list of PersonStatus configuration {@link PersonStatusConfigDTO}
     * @return - actual value of configuration for person statuses
     */
    @Override
    public List<PersonStatusConfigDTO> getPersonStatusConfigurations() {
        String statusConfigs = getGp(ConfigConstants.PERSON_STATUS_CONFIGURATION_KEY);
        return JsonUtil.toList(statusConfigs, new TypeToken<ArrayList<PersonStatusConfigDTO>>() { });
    }

    /**
     * Provides the {@link PersonStatusConfigDTO} for specific person status
     * @param personStatus - the looking person status
     * @return - the actual value of configuration or null if missing
     */
    @Override
    public PersonStatusConfigDTO getPersonStatusConfiguration(PersonStatus personStatus) {
        List<PersonStatusConfigDTO> configs = getPersonStatusConfigurations();
        for (PersonStatusConfigDTO statusConfig : configs) {
            if (statusConfig.getName().equalsIgnoreCase(personStatus.name())) {
                return statusConfig;
            }
        }
        return null;
    }

    /**
     * Provides the list of possible reason for changing status
     * @return - the possible reasons
     */
    @Override
    public List<String> getPersonStatusPossibleChangeReasons() {
        return GlobalPropertyUtil.parseList(getGp(ConfigConstants.PERSON_STATUS_POSSIBLE_REASONS_KEY), COMMA_DELIMITER);
    }

    private String getGp(String propertyName) {
        return Context.getAdministrationService().getGlobalProperty(propertyName);
    }
}
