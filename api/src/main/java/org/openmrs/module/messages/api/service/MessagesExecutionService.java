/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

/**
 * Provides methods to handle completed messages executions
 */
public interface MessagesExecutionService {
    /**
     * Method allows to perform the all desired actions after message execution
     * (attempt registration, status setting etc.)
     *
     * @param groupId id of messages execution group
     * @param executionId id of messages execution
     * @param channelType name of specific channel type (e.g. SMS, Call)
     */
    void executionCompleted(Integer groupId, String executionId, String channelType);
}
