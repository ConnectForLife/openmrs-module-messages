/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.service.ConfigService;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.api.util.ConfigConstants;
import org.openmrs.module.messages.api.util.GlobalPropertyUtil;

public class ConfigServiceImpl implements ConfigService {

    private static final Log LOGGER = LogFactory.getLog(ConfigServiceImpl.class);

    @Override
    public ReschedulingStrategy getReschedulingStrategy(String channelType) {
        String gpName = ConfigConstants.RESCHEDULING_STRATEGY_KEY;
        Map<String, String> strategyByChannel = GlobalPropertyUtil.parseMap(gpName, getGp(gpName));

        String beanName = strategyByChannel.get(channelType);
        if (StringUtils.isBlank(beanName)) {
            LOGGER.warn(String.format("Rescheduling strategy for channelType=%s is not defined in GP. " +
                    "The default strategy will be used: %s", channelType, gpName));
            beanName = ConfigConstants.DEFAULT_RESCHEDULING_STRATEGY;
        }
        return Context.getRegisteredComponent(beanName, ReschedulingStrategy.class);
    }

    @Override
    public int getMaxNumberOfRescheduling() {
        String gpName = ConfigConstants.MAX_NUMBER_OF_RESCHEDULING_KEY;
        return GlobalPropertyUtil.parseInt(gpName, getGp(gpName));
    }

    @Override
    public int getTimeIntervalToNextReschedule() {
        String gpName = ConfigConstants.TIME_INTERVAL_TO_NEXT_RESCHEDULE_KEY;
        return GlobalPropertyUtil.parseInt(gpName, getGp(gpName));
    }

    @Override
    public boolean isConsentControlEnabled() {
        return GlobalPropertyUtil.parseBool(getGp(ConfigConstants.CONSENT_CONTROL_KEY));
    }

    @Override
    public String getActorTypesConfiguration() {
        return getGp(ConfigConstants.ACTOR_TYPES_KEY);
    }

    @Override
    public String getDefaultActorRelationDirection() {
        return RelationshipTypeDirection.A.name();
    }

    private String getGp(String propertyName) {
        return Context.getAdministrationService().getGlobalProperty(propertyName);
    }
}
