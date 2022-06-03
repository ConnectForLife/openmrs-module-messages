/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.openmrs.module.messages.api.mappers.ActorScheduleMapper;
import org.openmrs.module.messages.api.mappers.ActorTypeMapper;
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

    public static ActorTypeMapper getActorTypeMapper() {
        return new ActorTypeMapper();
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
