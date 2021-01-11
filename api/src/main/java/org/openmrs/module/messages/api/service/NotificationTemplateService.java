/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

import org.openmrs.api.OpenmrsService;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.util.Map;

/**
 * Service used to build the notifications based on the {@link org.openmrs.module.messages.api.model.NotificationTemplate}
 */
public interface NotificationTemplateService extends OpenmrsService {

    /**
     * Builds a String for specific messaging service based on the stored notification template.
     *
     * @param patientTemplate - the {@link PatientTemplate} which represent specific service
     * @param serviceParam - the additional context parameters
     * @return - a parsed template
     * @should return null when template doesn't exists
     */
    String parseTemplate(PatientTemplate patientTemplate, Map<String, String> serviceParam);

    String buildMessageByGlobalProperty(Map<String, Object> param, String globalPropertyName);
}
