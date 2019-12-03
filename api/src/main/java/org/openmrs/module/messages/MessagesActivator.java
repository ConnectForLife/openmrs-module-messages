/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.BaseModuleActivator;
import org.openmrs.module.DaemonToken;
import org.openmrs.module.DaemonTokenAware;
import org.openmrs.module.Module;
import org.openmrs.module.ModuleFactory;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.exception.MessagesRuntimeException;
import org.openmrs.module.messages.api.scheduler.job.JobRepeatInterval;
import org.openmrs.module.messages.api.scheduler.job.MessageDeliveriesJobDefinition;
import org.openmrs.module.messages.api.service.MessagesSchedulerService;
import org.openmrs.module.messages.api.util.ConfigConstants;

/**
 * This class contains the logic that is run every time this module is either started or shutdown
 */
public class MessagesActivator extends BaseModuleActivator implements DaemonTokenAware {

    private static final Log LOGGER = LogFactory.getLog(MessagesActivator.class);

    private MessagesSchedulerService schedulerService;

    /**
     * @see #started()
     */
    @Override
    public void started() {
        LOGGER.info("Started Messages");
        try {
            createGlobalSettingIfNotExists(ConfigConstants.ACTOR_TYPES_KEY, ConfigConstants.ACTOR_TYPES_KEY_DEFAULT_VALUE);
            scheduleMessageDeliveries();
        } catch (APIException e) {
            safeShutdownModule();
            LOGGER.error("Failed to setup the required modules");
            throw new MessagesRuntimeException("Failed to setup the required modules", e);
        }
    }

    /**
     * @see #shutdown()
     */
    public void shutdown() {
        LOGGER.info("Shutdown Messages");
    }

    /**
     * @see #stopped()
     */
    @Override
    public void stopped() {
        LOGGER.info("Stopped Messages");
    }

    @Override
    public void setDaemonToken(DaemonToken token) {
        getSchedulerService().setDaemonToken(token);
    }

    private void scheduleMessageDeliveries() {
        getSchedulerService().rescheduleOrCreateNewTask(new MessageDeliveriesJobDefinition(), JobRepeatInterval.DAILY);
    }

    private MessagesSchedulerService getSchedulerService() {
        if (schedulerService == null) {
            schedulerService = Context.getRegisteredComponent(
                    MessagesConstants.SCHEDULER_SERVICE, MessagesSchedulerService.class);
        }
        return schedulerService;
    }

    private void createGlobalSettingIfNotExists(String key, String value) {
        String existSetting = Context.getAdministrationService().getGlobalProperty(key);
        if (StringUtils.isBlank(existSetting)) {
            Context.getAdministrationService().setGlobalProperty(key, value);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(String.format("Message Module created '%s' global property with value - %s", key, value));
            }
        }
    }

    private void safeShutdownModule() {
        Module mod = ModuleFactory.getModuleById(ConfigConstants.MODULE_ID);
        ModuleFactory.stopModule(mod);
    }
}
