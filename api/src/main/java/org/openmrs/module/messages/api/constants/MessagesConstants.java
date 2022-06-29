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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import static org.openmrs.module.messages.api.constants.ConfigConstants.MODULE_ID;

public final class MessagesConstants {

  public static final String SCHEDULER_SERVICE = "messages.schedulerService";

  public static final String DELIVERY_SERVICE = "messages.deliveryService";

  public static final String MESSAGING_GROUP_SERVICE = "messages.messagingGroupService";

  public static final String MESSAGING_SERVICE = "messages.messagingService";

  public static final String CONFIG_SERVICE = "messages.configService";

  public static final String ACTOR_SERVICE = "messages.actorService";

  public static final String PERSON_SERVICE = "personService";

  public static final String CALL_FLOW_INITIATE_CALL_EVENT = "callflows-call-initiate";

  public static final String SMS_INITIATE_EVENT = "send_sms";

  public static final String CALL_FLOW_SERVICE_RESULT_HANDLER_SERVICE =
      MODULE_ID + ".callFlowServiceResultHandlerService";
  public static final String SMS_SERVICE_RESULT_HANDLER_SERVICE =
      MODULE_ID + ".smsServiceResultHandlerService";

  public static final String DEFAULT_SERVER_SIDE_DATE_FORMAT = "yyyy-MM-dd[ HH:mm:ss]";

  /**
   * The datetime format which is used on server side.
   *
   * <p>Note: To be careful with changing it. Date parsed to this format are directly passed to
   * queries. DB engines are sensitive to the precision of dates. Eg MySQL's functions are mainly
   * based on the precision on the seconds level (the function DATE). To be sure that comparing
   * dates on the DB side works correctly we must use consistent format on the backend server side.
   */
  public static final String DEFAULT_SERVER_SIDE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

  public static final String DEFAULT_FRONT_END_DATE_FORMAT = "dd MMM yyyy";

  public static final String SCHEDULED_GROUP_MAPPER = "messages.scheduledGroupMapper";

  public static final String PATIENT_DEFAULT_ACTOR_TYPE = "Patient";

  public static final String DEACTIVATED_SCHEDULE_MESSAGE = "DEACTIVATED";

  public static final String UUID_KEY = "uuid";

  public static final String DEFAULT_ACTOR_RESPONSE_TYPE = "SCHEDULED_SERVICE_GROUP";

  public static final String DEACTIVATED_SERVICE = "Deactivate service";

  public static final int MYSQL_TEXT_DATATYPE_LENGTH = 65535;

  public static final String CHANNEL_TYPE_PARAM_NAME = "Service type";

  public static final String HOURS_MINUTES_SEPARATOR = ":";

  public static final String PATIENT_ID_PARAM = "patientId";

  public static final String ACTOR_ID_PARAM = "actorId";

  public static final String PERSON_ID_PARAM = "personId";

  /**
   * The logging category used for logging performance-related information across different
   * components of Messages module.
   *
   * @see #PERFORMANCE_LOGGER
   */
  public static final String PERFORMANCE_LOG_CAT = "org.openmrs.module.messages.performance";

  public static final Log PERFORMANCE_LOGGER = LogFactory.getLog(PERFORMANCE_LOG_CAT);

  /** The name of Concept Class for Health Tip Category Concepts. */
  public static final String HEALTH_TIP_CATEGORY_CLASS_NAME = "Health Tips Category";

  private MessagesConstants() {
    // private. So can't be initialized
  }
}
