package org.openmrs.module.messages.api.config.impl;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.config.ConfigService;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.util.ConfigConstants;

public class ConfigServiceImpl implements ConfigService {

    @Override
    public String getActorTypesConfiguration() {
        return Context.getAdministrationService().getGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY);
    }

    @Override
    public String getDefaultActorRelationDirection() {
        return RelationshipTypeDirection.A.name();
    }
}
