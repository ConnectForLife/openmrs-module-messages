package org.openmrs.module.messages.api.config.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.util.ConfigConstants;

import static org.openmrs.module.messages.api.util.ConfigConstants.GROUPING_PERIOD_IN_SECONDS_DEFAULT_VALUE;
import static org.openmrs.module.messages.api.util.ConfigConstants.GROUPING_PERIOD_IN_SECONDS;

public class ConfigServiceImpl implements ConfigService {

    private static final Log LOG = LogFactory.getLog(ConfigServiceImpl.class);

    @Override
    public String getActorTypesConfiguration() {
        return Context.getAdministrationService().getGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY);
    }

    @Override
    public String getDefaultActorRelationDirection() {
        return RelationshipTypeDirection.A.name();
    }

    @Override
    public int getGroupingPeriodInSeconds() {
        int result = GROUPING_PERIOD_IN_SECONDS_DEFAULT_VALUE;
        try {
            result = Integer.valueOf(Context.getAdministrationService().getGlobalProperty(GROUPING_PERIOD_IN_SECONDS));
        } catch (NumberFormatException e) {
            LOG.error("Invalid global property value: " + GROUPING_PERIOD_IN_SECONDS
                    + " - using default value: " + GROUPING_PERIOD_IN_SECONDS_DEFAULT_VALUE);
        }
        return result;
    }
}
