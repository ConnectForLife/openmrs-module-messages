/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.openmrs.module.messages.api.model.NotificationTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * Service used to build the notifications based on the {@link org.openmrs.module.messages.api.model.NotificationTemplate}.
 * Uses the velocity engine to build messages.
 */
public class VelocityNotificationTemplateServiceImpl extends NotificationTemplateServiceImpl {

    private static final Log LOGGER = LogFactory.getLog(VelocityNotificationTemplateServiceImpl.class);

    private static final String LOG_TAG = "VelocityNotificationTemplate";

    /**
     * Loads and eval the template and returns the outcome message as String.
     *
     * @param template - the notification template
     * @param templateData - input template's data
     * @return - the outcome message
     */
    @Override
    protected String loadTemplate(NotificationTemplate template, Map<String, Object> templateData) {
        VelocityContext context = new VelocityContext(templateData);
        return evalTemplate(template, context);
    }

    /**
     * Eval the template and inject the template properties
     *
     * @param template - related template
     * @param context - related velocity context
     * @return - outcome message or null if template missing
     */
    private String evalTemplate(NotificationTemplate template, VelocityContext context) {
        String outcomeMessage = null;
        if (null != template && !StringUtils.isEmpty(template.getValue())) {
            StringWriter writer = new StringWriter();
            try {
                Velocity.evaluate(context, writer, LOG_TAG, template.getValue());
                outcomeMessage = writer.toString();
            } catch (IOException ioe) {
                LOGGER.error("Error retrieving the contents : ", ioe);
            }
        }
        LOGGER.debug(String.format("Notification outcome message %s", outcomeMessage));
        return outcomeMessage;
    }
}
