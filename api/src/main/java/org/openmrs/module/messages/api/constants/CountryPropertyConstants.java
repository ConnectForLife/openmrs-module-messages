/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.constants;

/**
 * The constants class with Country Properties.
 */
public final class CountryPropertyConstants {

  public static final String SMS_CONFIG_PROP_NAME = "messages.smsConfig";

  public static final String SMS_CONFIG_PROP_DESC =
      "The name of default SMS provider configuration.";

  public static final String SEND_SMS_ON_PATIENT_REGISTRATION_PROP_NAME =
      "messages.sendSmsOnPatientRegistration";

  public static final String SEND_SMS_ON_PATIENT_REGISTRATION_PROP_DESC =
      "The property configures whether new patients should receive Welcome Message via SMS after they are "
          + "register in the system. Value: true/false.";

  public static final String WHATSAPP_CONFIG_PROP_NAME = "messages.whatsAppConfig";

  public static final String WHATSAPP_CONFIG_PROP_DESC = "The name of default WhatsApp provider configuration.";

  public static final String SEND_WHATSAPP_ON_PATIENT_REGISTRATION_PROP_NAME = "messages.sendWhatsAppOnPatientRegistration";

  public static final String SEND_WHATSAPP_ON_PATIENT_REGISTRATION_PROP_DESC =
      "The property configures whether new patients should receive Welcome Message via WhatsApp after they are "
          + "register in the system. Value: true/false.";

  public static final String CALL_CONFIG_PROP_NAME = "messages.callConfig";

  public static final String CALL_CONFIG_PROP_DESC =
      "The name of default Call provider configuration.";

  public static final String PERFORM_CALL_ON_PATIENT_REGISTRATION_PROP_NAME =
      "messages.performCallOnPatientRegistration";

  public static final String PERFORM_CALL_ON_PATIENT_REGISTRATION_PROP_DESC =
      "The property configures whether new patients should receive Welcome Message via call after they are register in the"
          + " system. Value: true/false";

  public static final String PATIENT_NOTIFICATION_TIME_WINDOW_FROM_PROP_NAME =
    "messages.patientNotificationTimeWindowFrom";

  public static final String PATIENT_NOTIFICATION_TIME_WINDOW_FROM_DEFAULT_VALUE = "00:01";

  public static final String PATIENT_NOTIFICATION_TIME_WINDOW_FROM_PROP_DESC =
    "The property configures the beginning of the time window during which system is allowed to send notifications to "
      + "patients.";

  public static final String PATIENT_NOTIFICATION_TIME_WINDOW_TO_PROP_NAME =
    "messages.patientNotificationTimeWindowTo";

  public static final String PATIENT_NOTIFICATION_TIME_WINDOW_TO_DEFAULT_VALUE = "23:59";

  public static final String PATIENT_NOTIFICATION_TIME_WINDOW_TO_PROP_DESC =
    "The property configures the end of the time window during which system is allowed to send notifications to "
      + "patients.";

  public static final String EMPTY_CONFIG_DEFAULT_VALUE = "-";

  private CountryPropertyConstants() {
  }
}
