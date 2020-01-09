/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.strategy;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openmrs.module.messages.api.model.ScheduledExecutionContext;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FailedMessageReschedulingStrategyITTest extends BaseReschedulingStrategyITTest {

    @Autowired
    @Qualifier("messages.failedMessageReschedulingStrategy")
    private ReschedulingStrategy reschedulingStrategy;

    @Test
    public void shouldRescheduleOnlyFailedDelivery() throws Exception {
        getStrategy().execute(failedScheduledService);

        TaskDefinition task = getCreatedTask();
        assertNotNull(task);

        ScheduledExecutionContext executionContext = getExecutionContext(task);
        assertEquals(failedScheduledService.getGroup().getActor().getId().intValue(), executionContext.getActorId());
        assertEquals(1, executionContext.getServiceIdsToExecute().size());
        assertThat(executionContext.getServiceIdsToExecute(), contains(failedScheduledService.getId()));
    }

    @Override
    protected ReschedulingStrategy getStrategy() {
        return reschedulingStrategy;
    }
}
