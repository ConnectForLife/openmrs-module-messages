/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FailedAndPendingMessagesReschedulingStrategyITTest extends BaseReschedulingStrategyITTest {

    @Autowired
    @Qualifier("messages.failedAndPendingMessagesReschedulingStrategy")
    private ReschedulingStrategy reschedulingStrategy;

    @Test
    public void shouldRescheduleFailedDeliveryTogetherWithPendingMessagesOfTheSameChannel() throws Exception {
        addDeliveryAttempts(failedScheduledService, MAX_ATTEMPTS - 1);
        addDeliveryAttempts(pendingScheduledService, MAX_ATTEMPTS - 1);

        reschedulingStrategy.execute(failedScheduledService.getGroup(), failedScheduledService.getChannelType());

        TaskDefinition task = getCreatedTask();
        assertNotNull(task);
        assertEquals(NEVER_REPEAT, task.getRepeatInterval());

        ScheduledExecutionContext executionContext = getExecutionContext(task);
        assertEquals(failedScheduledService.getGroup().getActor().getId().intValue(), executionContext.getActorId());
        assertThat(executionContext.getServiceIdsToExecute(), hasItems(
                failedScheduledService.getId(),
                pendingScheduledService.getId()));
        assertThat(executionContext.getServiceIdsToExecute(),
                not(hasItems(pendingScheduledServiceInAnotherChannel.getId())));
    }

    @Override
    protected ReschedulingStrategy getStrategy() {
        return reschedulingStrategy;
    }
}
