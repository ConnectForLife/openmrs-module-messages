/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service;

public final class DatasetConstants {

    public static final String XML_DATA_SET_PATH = "datasets/";

    //Roman
    public static final int DEFAULT_PERSON_ID = 997;

    public static final int DEFAULT_PATIENT_ID = DEFAULT_PERSON_ID;
    public static final int DEFAULT_PATIENT_TEMPLATE_ID = 71;
    public static final int DEFAULT_TEMPLATE = 51;

    // Ben (deactivated patient)
    public static final int DEFAULT_INACTIVE_PERSON_ID = 111;

    public static final int DEFAULT_INACTIVE_PATIENT_ID = DEFAULT_INACTIVE_PERSON_ID;
    public static final int DEFAULT_INACTIVE_PATIENT_TEMPLATE_ID = 74;

    //Pablo Caregiver
    public static final int DEFAULT_CAREGIVER_ID = 999;
    public static final int DEFAULT_CAREGIVER_PATIENT_TEMPLATE_ID = 72;
    public static final int DEFAULT_CAREGIVER_TEMPLATE_ID = 51;

    //John Caregiver - no consent
    public static final int DEFAULT_NO_CONSENT_CAREGIVER_ID = 998;

    //Dummy Visit Reminder
    public static final String DEFAULT_TEMPLATE_NAME = "Dummy Visit Reminder";

    public static final int SCHEDULED_SERVICE_GROUP = 91;

    public static final int DELIVERED_SCHEDULED_SERVICE = 101;
    public static final int PENDING_SCHEDULED_SERVICE = 103;
    public static final int PENDING_SCHEDULED_SERVICE_IN_ANOTHER_CHANNEL = 104;
    public static final int FAILED_SCHEDULED_SERVICE = 102;

    private DatasetConstants() {
    }
}
