package org.openmrs.module.messages.api.util;

import org.openmrs.module.messages.api.mappers.ActorScheduleMapper;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.mappers.MessageMapper;
import org.openmrs.module.messages.api.mappers.UserMapper;

public final class StaticMappersProvider {

    public static MessageDetailsMapper getMessageDetailsMapper() {
        MessageDetailsMapper result = new MessageDetailsMapper();
        result.setMessageMapper(getMessageMapper());
        return result;
    }

    public static MessageMapper getMessageMapper() {
        MessageMapper result = new MessageMapper();
        result.setActorScheduleMapper(getActorScheduleMapper());
        result.setUserMapper(getUserMapper());
        return result;
    }

    public static ActorScheduleMapper getActorScheduleMapper() {
        return new ActorScheduleMapper();
    }

    public static UserMapper getUserMapper() {
        return new UserMapper();
    }

    private StaticMappersProvider() {
    }
}
