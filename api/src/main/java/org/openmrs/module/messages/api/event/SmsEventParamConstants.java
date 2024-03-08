/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.event;

public final class SmsEventParamConstants {

    /**
     * List of recipients (phone numbers)
     */
    public static final String RECIPIENTS = "recipients";

    /**
     * the text content of the SMS message
     */
    public static final String MESSAGE = "message";

    /**
     * map of custom parameters
     */
    public static final String CUSTOM_PARAMS = "custom_params";

    /**
     * Message id of the scheduled service
     */
    public static final String MESSAGE_ID = "message_id";

    /**
     * Message group id of the scheduled service
     */
    public static final String MESSAGE_GROUP_ID = "message_group_id";

    /**
     * Used service name
     */
    public static final String SERVICE_NAME = "service";

    /**
     * Config that is used for the SMS message
     */
    public static final String CONFIG_KEY = "config";

    private SmsEventParamConstants() { }
}
