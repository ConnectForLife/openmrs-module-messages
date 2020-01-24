/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

public enum TemplateFieldType {
    SERVICE_TYPE,
    MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY,
    MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY,
    DAY_OF_WEEK,
    DAY_OF_WEEK_SINGLE,
    START_OF_MESSAGES,
    END_OF_MESSAGES,
    CATEGORY_OF_MESSAGE;
}
