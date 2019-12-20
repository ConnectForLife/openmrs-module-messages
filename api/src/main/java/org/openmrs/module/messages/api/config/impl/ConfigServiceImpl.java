package org.openmrs.module.messages.api.config.impl;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.util.ConfigConstants;

public class ConfigServiceImpl implements ConfigService {

    @Override
    public String getReschedulingStrategy() {
        return Context.getAdministrationService().getGlobalProperty(
                ConfigConstants.RESCHEDULING_STRATEGY_KEY);
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
