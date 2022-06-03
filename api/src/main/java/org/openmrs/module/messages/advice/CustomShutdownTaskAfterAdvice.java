/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.advice;

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.scheduler.TaskDefinition;
import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * The AOP advice executed after {@link org.openmrs.scheduler.SchedulerService#shutdownTask(TaskDefinition)} to make sure
 * the task is also shutdown in internal messages scheduler.
 * <p>
 * This class is configured in omod/config.xml.
 * </p>
 */
public class CustomShutdownTaskAfterAdvice implements AfterReturningAdvice {
    private static final String SHUTDOWN_TASK_METHOD_NAME = "shutdownTask";

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        if (isShutdownTaskMethod(method)) {
            ensureShutdownInMessagesScheduler(args);
        }
    }

    private boolean isShutdownTaskMethod(Method method) {
        return method.getName().equals(SHUTDOWN_TASK_METHOD_NAME);
    }

    private void ensureShutdownInMessagesScheduler(Object[] args) {
        if (args[0] instanceof TaskDefinition) {
            Context.getService(MessagesSchedulerService.class).shutdownTask((TaskDefinition) args[0]);
        }
    }
}
