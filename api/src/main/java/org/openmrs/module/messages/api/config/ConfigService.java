package org.openmrs.module.messages.api.config;

public interface ConfigService {

    String getReschedulingStrategy();

    int getMaxNumberOfRescheduling();

    int getTimeIntervalToNextReschedule();

    String getActorTypesConfiguration();

    String getDefaultActorRelationDirection();
}
