/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages;

public final class Constant {

    public static final int NOT_EXISTING_ID = -1;

    public static final String DEACTIVATED_SCHEDULE_MESSAGE = "DEACTIVATED";

    public static final String CAREGIVER_RELATIONSHIP = "Caregiver";
    public static final String CARETAKER_RELATIONSHIP = "Caretaker";
    public static final String PATIENT_RELATIONSHIP = "Patient";

    public static final String UUID_KEY = "uuid";
    public static final String VALUE_KEY = "value";

    public static final String STATUS_REASON_DECEASED = "Deceased";
    public static final String STATUS_REASON_LOST_FOLLOW_UP = "Lost to Follow Up";
    public static final String STATUS_REASON_PAUSE = "Temporary pause";
    public static final String STATUS_REASON_VACATION = "On vacation";
    public static final String STATUS_REASON_TRANSFERRED = "Transferred";
    public static final String STATUS_REASON_OTHER = "Other";

    public static final String ACTOR_RESPONSE_TEXT_QUESTION = "Question?";
    public static final String ACTOR_RESPONSE_TEXT_RESPONSE = "Response";

    public static final String CHANNEL_TYPE_CALL = "Call";
    public static final String CHANNEL_TYPE_SMS = "SMS";

    public static final String EXAMPLE_TEMPLATE_SERVICE_QUERY = "example template service query";
    public static final String EXAMPLE_TEMPLATE_CALENDAR_SERVICE_QUERY = "example template calendar service query";
    public static final String EXAMPLE_PATIENT_TEMPLATE_CALENDAR_SERVICE_QUERY = "example patient template calendar service query";

    private Constant() {
    }
}
