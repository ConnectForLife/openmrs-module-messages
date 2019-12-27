package org.openmrs.module.messages.api.config;

import org.openmrs.module.messages.api.strategy.ReschedulingStrategy;

public interface ConfigService {

    ReschedulingStrategy getReschedulingStrategy();

    int getMaxNumberOfRescheduling();

    int getTimeIntervalToNextReschedule();

    String getActorTypesConfiguration();

    String getDefaultActorRelationDirection();
}
