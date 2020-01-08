/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.config.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;
import org.openmrs.module.messages.api.util.ConfigConstants;

public class ConfigServiceImpl implements ConfigService {

    private static final String TRUE = "true";

    @Override
    public ReschedulingStrategy getReschedulingStrategy() {
        String beanName = Context.getAdministrationService().getGlobalProperty(
                ConfigConstants.RESCHEDULING_STRATEGY_KEY);
        return Context.getRegisteredComponent(beanName, ReschedulingStrategy.class);
    }

    @Override
    public int getMaxNumberOfRescheduling() {
        return getIntGp(ConfigConstants.MAX_NUMBER_OF_RESCHEDULING_KEY);
    }

    @Override
    public int getTimeIntervalToNextReschedule() {
        return getIntGp(ConfigConstants.TIME_INTERVAL_TO_NEXT_RESCHEDULE_KEY);
    }

    @Override
    public boolean isConsentControlEnabled() {
        return StringUtils.equalsIgnoreCase(
                TRUE,
                Context.getAdministrationService().getGlobalProperty(ConfigConstants.CONSENT_CONTROL_KEY));
    }

    @Override
    public String getActorTypesConfiguration() {
        return Context.getAdministrationService().getGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY);
    }

    @Override
    public String getDefaultActorRelationDirection() {
        return RelationshipTypeDirection.A.name();
    }

    private int getIntGp(String propertyName) {
        String value = Context.getAdministrationService().getGlobalProperty(propertyName);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            throw new MessagesRuntimeException(
                    String.format("Cannot parse the global property %s=%s. Expected int value.", propertyName, value),
                    ex
            );
        }
    }
}
