/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao;

import org.openmrs.GlobalProperty;
import org.openmrs.module.messages.api.model.NotificationTemplate;
import org.openmrs.module.messages.api.model.Template;

import java.util.List;

public interface NotificationTemplateDao {

    /**
     * Gets a notification template that has the given <code>templateName</code>
     *
     * @param templateName - given service template name
     * @return - notification template object if exists
     * @should return null when no notification template matches given template name
     * @should return null when given null template name
     */
    NotificationTemplate getTemplate(String templateName);

    /**
     * Gets the string representation of services which will can be used during template processing.
     *
     * @return - the string representation of injected services
     * @should return empty string when services' configuration is missing
     */
    String getInjectedServicesMap();

    /**
     * Converts the {@link GlobalProperty} into the {@link NotificationTemplate} representation.
     *
     * @param globalProperty - given global property
     * @return - notification template representation
     * @should return null when null global property is given
     */
    NotificationTemplate convertToNotificationTemplate(GlobalProperty globalProperty);

    /**
     * Gets list of Global Property names required for the {@code templates}.
     * <p>
     * This method provides a list of required names only, it does NOT assure that such GPs exist.
     * </p>
     *
     * @param templates the list of Templates, not null
     * @return the list of Global Property names required by {@code templates}, never null
     */
    List<String> getRequiredGlobalPropertyNames(List<Template> templates);
}
