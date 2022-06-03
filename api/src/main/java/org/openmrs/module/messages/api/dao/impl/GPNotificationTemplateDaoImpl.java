/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.GlobalProperty;
import org.openmrs.api.AdministrationService;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dao.NotificationTemplateDao;
import org.openmrs.module.messages.api.model.NotificationTemplate;
import org.openmrs.module.messages.api.model.Template;

import java.util.List;
import java.util.stream.Collectors;

public class GPNotificationTemplateDaoImpl implements NotificationTemplateDao {

    private static final Log LOGGER = LogFactory.getLog(GPNotificationTemplateDaoImpl.class);

    private static final String NOTIFICATION_TEMPLATE_PROPERTY_PREFIX = "messages.notificationTemplate.";

    private static final String WHITESPACE_REGEX = "\\s";

    private static final String DASH_SIGN = "-";

    private AdministrationService administrationService;

    @Override
    public NotificationTemplate getTemplate(String templateName) {
        if (StringUtils.isBlank(templateName)) {
            LOGGER.debug("getTemplate(...) noticed blank template name. Null NotificationTemplate returned.");
            return null;
        }
        String globalPropertyName = this.getGlobalPropertyName(templateName);
        GlobalProperty globalProperty = this.administrationService.getGlobalPropertyObject(globalPropertyName);
        return this.convertToNotificationTemplate(globalProperty);
    }

    @Override
    public String getInjectedServicesMap() {
        return this.administrationService.getGlobalProperty(ConfigConstants.NOTIFICATION_TEMPLATE_INJECTED_SERVICES,
                StringUtils.EMPTY);
    }

    @Override
    public NotificationTemplate convertToNotificationTemplate(GlobalProperty globalProperty) {
        NotificationTemplate notificationTemplate = null;
        if (null != globalProperty) {
            notificationTemplate = new NotificationTemplate();
            notificationTemplate.setTemplateName(globalProperty.getProperty());
            notificationTemplate.setValue(globalProperty.getPropertyValue());
        }
        return notificationTemplate;
    }

    @Override
    public List<String> getRequiredGlobalPropertyNames(List<Template> templates) {
        return templates
                .stream()
                .map(BaseOpenmrsMetadata::getName)
                .map(this::getGlobalPropertyName)
                .collect(Collectors.toList());
    }

    /**
     * Sets a administration service bean value
     *
     * @param administrationService - administration service impl
     */
    public void setAdministrationService(AdministrationService administrationService) {
        this.administrationService = administrationService;
    }

    /**
     * Creates the notification global property name. The template name is transform to lower case.
     *
     * @param templateName - not null value of template name
     * @return - the global property name
     */
    private String getGlobalPropertyName(String templateName) {
        String globalPropertyName =
                NOTIFICATION_TEMPLATE_PROPERTY_PREFIX + templateName.trim().replaceAll(WHITESPACE_REGEX, DASH_SIGN);
        globalPropertyName = globalPropertyName.toLowerCase();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(
                    String.format("Global property name `%s` was created based on `%s` template name", globalPropertyName,
                            templateName));
        }
        return globalPropertyName;
    }
}
