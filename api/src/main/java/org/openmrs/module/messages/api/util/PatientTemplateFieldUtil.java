/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public final class PatientTemplateFieldUtil {

    public static boolean isDeactivated(PatientTemplate patientTemplate) {
        String serviceType = getTemplateFieldValue(patientTemplate, TemplateFieldType.SERVICE_TYPE, true);
        return StringUtils.isBlank(serviceType)
                || MessagesConstants.DEACTIVATED_SERVICE.equalsIgnoreCase(serviceType);
    }

    public static String getTemplateFieldValue(PatientTemplate patientTemplate,
                                               TemplateFieldType fieldType,
                                               boolean setDefaultIfBlank) {
        String value = null;
        for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
            if (templateFieldValue.getTemplateField().getTemplateFieldType().equals(fieldType)) {
                value = templateFieldValue.getValue();
                if (setDefaultIfBlank && StringUtils.isBlank(value)) {
                    value = templateFieldValue.getTemplateField().getDefaultValue();
                }
                break;
            }
        }
        return value;
    }

    private PatientTemplateFieldUtil() {
    }
}
