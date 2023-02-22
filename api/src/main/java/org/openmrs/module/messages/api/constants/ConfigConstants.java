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

public final class ConfigConstants {

  public static final String MODULE_ID = "messages";

  public static final String PERSON_PHONE_ATTR = "Telephone Number";

  public static final String ACTOR_TYPES_KEY = "messages.actor.types";
  public static final String ACTOR_TYPES_DEFAULT_VALUE = "";
  public static final String ACTOR_TYPES_DESCRIPTION =
      "Comma separated list of relationship types used to control the "
          + "list of possible targets of message (not including the patient as a target). Example of usage: "
          + "'relationshipTypeUuid:directionOfRelationship,secondRelationshipTypeUuid:directionOfRelationship'";

  public static final String PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME = "Best contact time";
  public static final String PERSON_CONTACT_TIME_TYPE_DESCRIPTION =
      "Attribute stores the value of time which best "
          + "suitable for person to perform the contact";
  public static final String PERSON_CONTACT_TIME_TYPE_FORMAT = "java.util.Date";
  public static final String PERSON_CONTACT_TIME_TYPE_UUID = "7a8ad9aa-bd0e-48c0-826d-fa628f1db644";

  public static final String PERSON_STATUS_ATTRIBUTE_TYPE_NAME = "Person status";
  public static final String PERSON_STATUS_ATTRIBUTE_TYPE_DESCRIPTION = "Person status attribute";
  public static final String PERSON_STATUS_ATTRIBUTE_TYPE_FORMAT = "java.lang.String";
  public static final String PERSON_STATUS_ATTRIBUTE_TYPE_UUID =
      "dda246c6-c806-402a-9b7c-e2c1574a6441";

  public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_NAME = "Person status - reason";
  public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_DESCRIPTION =
      "Reason of change the person status";
  public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_FORMAT = "java.lang.String";
  public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_UUID =
      "da7a9976-c681-4182-a7f4-f9f6b31a9058";

  public static final String PERSON_STATUS_POSSIBLE_REASONS_KEY =
      "message.personStatus.reason.values";
  public static final String PERSON_STATUS_POSSIBLE_REASONS_DEFAULT_VALUE =
      "Deceased,Lost to Follow Up," + "Temporary pause,On vacation,Transferred,Other";
  public static final String PERSON_STATUS_POSSIBLE_REASONS_DESCRIPTION =
      "Comma separated list of possible values for "
          + "changing the person status. Example: 'Deceased,Temporary pause,On vacation,Transferred,Other'";

  public static final String PERSON_STATUS_CONFIGURATION_KEY = "message.personStatus.configuration";
  public static final String PERSON_STATUS_CONFIGURATION_DEFAULT_VALUE =
      "[\n"
          + "  {\n"
          + "    \"name\": \"NO_CONSENT\",\n"
          + "    \"style\": \"background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;\""
          + "  },\n"
          + "  {\n"
          + "    \"name\": \"ACTIVATED\",\n"
          + "    \"style\": \"background-color: #51a351; border-color: #51a351; color: #f5f5f5;\""
          + "  },\n"
          + "  {\n"
          + "    \"name\": \"DEACTIVATED\",\n"
          + "    \"style\": \"background-color: #f23722; border-color: #f23722; color: #f5f5f5;\""
          + "  },\n"
          + "  {\n"
          + "    \"name\": \"MISSING_VALUE\",\n"
          + "    \"style\": \"background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;\""
          + "  }\n"
          + "]";
  public static final String PERSON_STATUS_CONFIGURATION_DESCRIPTION =
      "Person status configuration in JSON-like format."
          + "Example: "
          + PERSON_STATUS_CONFIGURATION_DEFAULT_VALUE;

  public static final String CONSENT_CONTROL_KEY = "message.consent.validation";
  public static final String CONSENT_CONTROL_DEFAULT_VALUE = "true";
  public static final String CONSENT_CONTROL_DESCRIPTION =
      "Used to determine if Person Consent (Person Status) "
          + "should be used or not. Possible values: true / false.";

  public static final String GLOBAL_BEST_CONTACT_TIME_KEY = "global";
  public static final String GLOBAL_BEST_CONTACT_TIME_VALUE = "10:00";

  public static final String BEST_CONTACT_TIME_KEY = "message.bestContactTime.default";
  public static final String BEST_CONTACT_TIME_DEFAULT_VALUE =
      "{\n"
          + "  \""
          + GLOBAL_BEST_CONTACT_TIME_KEY
          + "\": \""
          + GLOBAL_BEST_CONTACT_TIME_VALUE
          + "\",\n"
          + "  \"acec590b-825e-45d2-876a-0028f174903d\": \"10:00\"\n"
          + "}";
  public static final String BEST_CONTACT_TIME_DESCRIPTION =
      "Map of actor types and their default contact times"
          + " for services for different actor types. Example of usage: "
          + BEST_CONTACT_TIME_DEFAULT_VALUE;

  public static final String DEFAULT_RESCHEDULING_STRATEGY =
      "messages.failedAndPendingMessagesReschedulingStrategy";

  public static final String RESCHEDULING_STRATEGY_KEY = "messages.reschedulingStrategy";
  public static final String RESCHEDULING_STRATEGY_DEFAULT_VALUE =
      "SMS:messages.failedAndPendingMessagesReschedulingStrategy,"
          + "Call:messages.failedAndPendingMessagesReschedulingStrategy";
  public static final String RESCHEDULING_STRATEGY_DESCRIPTION =
      "Used to determine the name of the Spring Bean which should be used to handle the reschedule logic. The "
          + "value could represent multiple strategies. Each of them is dedicated to a specific channel type. The "
          + "particular bean is represented by channel name and bean name separated by a colon. If the system "
          + "supports multiple channels next entries must be separated by a comma. Example of usage: "
          + "'channelType1:beanName1,channelType2:beanName2'.";

  public static final String DEFAULT_SERVICE_RESULT_HANDLER =
      "messages.defaultServiceResultHandlerService";

  public static final String SERVICE_RESULT_HANDLERS = "messages.serviceResultsHandlers";
  public static final String SERVICE_RESULT_HANDLERS_DEFAULT_VALUE =
      "SMS:messages.smsServiceResultHandlerService,"
          + "Call:messages.callFlowServiceResultHandlerService,"
          + "WhatsApp:messages.whatsAppServiceResultHandlerService";
  public static final String SERVICE_RESULT_HANDLERS_DESCRIPTION =
      "Used to determine the name of the Spring Bean which should be used to handle the service results. The "
          + "value could represent multiple handlers. Each of them is dedicated to a specific channel type. The "
          + "particular bean is represented by channel name and bean name separated by a colon. If the system "
          + "supports multiple channels next entries must be separated by a comma. Example of usage: "
          + "'channelType1:beanName1,channelType2:beanName2'.";

  public static final String MAX_NUMBER_OF_ATTEMPTS_KEY = "messages.maxNumberOfAttempts";
  public static final String MAX_NUMBER_OF_ATTEMPTS_DEFAULT_VALUE = "3";
  public static final String MAX_NUMBER_OF_ATTEMPTS_DESCRIPTION =
      "Used to determine the number of maximum attempts that can be taken for failing ScheduledServices. After "
          + "exceeding the number of retries, services will be denied rescheduling next task. Changing this value "
          + "will not influence on tasks which already exceeded the counter. In the primary approach, it is designed "
          + "to be used by the strategy FailedMessageReschedulingStrategy.";

  public static final String TIME_INTERVAL_TO_NEXT_RESCHEDULE_KEY =
      "messages.timeIntervalToNextReschedule";
  public static final String TIME_INTERVAL_TO_NEXT_RESCHEDULE_DEFAULT_VALUE = "900";
  public static final String TIME_INTERVAL_TO_NEXT_RESCHEDULE_DESCRIPTION =
      "Used to determine the time (in seconds) when the task for retry will be scheduled. The retry time will be "
          + "set according to the time when the system gets registerAttempt response from the service plus the "
          + "number of second expressed in the value of this property.";

  public static final String CUT_OFF_SCORE_FOR_HIGH_MEDIUM_ADHERENCE_LEVEL_KEY =
      "messages.cutOffScoreForHighMediumAdherenceLevel";
  public static final String CUT_OFF_SCORE_FOR_HIGH_MEDIUM_ADHERENCE_LEVEL_DEFAULT_VALUE = "90";
  public static final String CUT_OFF_SCORE_FOR_HIGH_MEDIUM_ADHERENCE_LEVEL_DESCRIPTION =
      "Used to specify a percentage cut-off score for High and Medium adherence level";

  public static final String CUT_OFF_SCORE_FOR_MEDIUM_LOW_ADHERENCE_LEVEL_KEY =
      "messages.cutOffScoreForMediumLowAdherenceLevel";
  public static final String CUT_OFF_SCORE_FOR_MEDIUM_LOW_ADHERENCE_LEVEL_DEFAULT_VALUE = "70";
  public static final String CUT_OFF_SCORE_FOR_MEDIUM_LOW_ADHERENCE_LEVEL_DESCRIPTION =
      "Used to specify a percentage cut-off score for Medium and Low adherence level";

  public static final String CUT_OFF_SCORE_FOR_ADHERENCE_TREND_KEY =
      "messages.cutOffScoreForAdherenceTrend";

  public static final String BENCHMARK_PERIOD_KEY = "messages.benchmarkPeriod";

  public static final String STATUSES_ENDING_CALLFLOW = "messages.statusesEndingCallflow";
  public static final String STATUSES_ENDING_CALLFLOW_DEFAULT_VALUE =
      "ANSWERED,UNANSWERED,MACHINE,BUSY,CANCELLED,FAILED,REJECTED,NO_ANSWER,TIMEOUT,COMPLETED,UNKNOWN";
  public static final String STATUSES_ENDING_CALLFLOW_DESCRIPTION =
      "Comma-separated values which points statuses describing "
          + "call which have ended flow in the Callflow module.";

  public static final String NOTIFICATION_TEMPLATE_INJECTED_SERVICES =
      "messages.notificationTemplate.injectedServices";
  public static final String NOTIFICATION_TEMPLATE_INJECTED_SERVICES_DEFAULT_VALUE =
      "patientService:patientService,cflPersonService:cflPersonService,messagesService:messages.messagingService,"
          + "personService:personService,personDAO:personDAO,conceptDAO:conceptDAO,"
          + "locationService:locationService,messagingGroupService:messages.messagingGroupService,"
          + "openmrsContext:context,patientTemplateService:messages.patientTemplateService";
  public static final String NOTIFICATION_TEMPLATE_INJECTED_SERVICES_DESCRIPTION =
      "Comma-separated values which represent the map of services which should be injected into "
          + "the messaging notification template.";

  public static final String DEFAULT_USER_TIMEZONE = "messages.defaultUserTimezone";
  public static final String DEFAULT_USER_TIMEZONE_DEFAULT_VALUE = "Europe/Brussels";
  public static final String DEFAULT_USER_TIMEZONE_DESCRIPTION =
      "The timezone which represents end user timezone."
          + " The message module events and best contact time are interpreted with this timezone.";

  public static final String CALL_CONFIG = "messages.callConfig";
  public static final String CALL_CONFIG_DEFAULT_VALUE = "";
  public static final String CALL_CONFIG_DESCRIPTION =
      "The string value representing the default value of "
          + "call configuration used to make calls.";

  public static final String CALL_DEFAULT_FLOW = "messages.defaultFlow";
  public static final String CALL_DEFAULT_FLOW_DEFAULT_VALUE = "";
  public static final String CALL_DEFAULT_FLOW_DESCRIPTION =
      "The string value representing the default value of call flow used to make calls.";

  public static final String MESSAGE_DELIVERY_JOB_INTERVAL = "messages.messageDeliveryJobInterval";
  public static final String MESSAGE_DELIVERY_JOB_INTERVAL_DEFAULT_VALUE = "86400";
  public static final String MESSAGE_DELIVERY_JOB_INTERVAL_DESCRIPTION =
      "The integer value represents the "
          + "interval (in second) of launching the job which will schedule tasks for the upcoming messages. "
          + "A server reboot is required to effect changes.";

  public static final String MESSAGE_DELIVERY_JOB_START_TIME_GP_KEY =
      "messages.messageDeliveryJobStartTime";
  public static final String MESSAGE_DELIVERY_JOB_START_TIME_DEFAULT_VALUE = "00:15";
  public static final String MESSAGE_DELIVERY_JOB_START_TIME_DESCRIPTION =
      "Time in format HH:mm (24-hour format, system timezone) that determines start time of Message Delivery job. "
          + "If there is no value configured then default value will be used: "
          + MESSAGE_DELIVERY_JOB_START_TIME_DEFAULT_VALUE
          + ". To apply the changes server restart is required.";

  public static final String SMS_CONFIG = "messages.smsConfig";
  public static final String SMS_CONFIG_DEFAULT_VALUE = "";
  public static final String SMS_CONFIG_DESCRIPTION =
      "The string value representing the default value of "
          + "sms configuration used to send sms. If empty, SMS module will use default config set in this module";

  public static final String WHATSAPP_CONFIG_GP_KEY = "messages.whatsAppConfig";
  public static final String WHATSAPP_CONFIG_GP_DEFAULT_VALUE = "";
  public static final String WHATSAPP_CONFIG_GP_DESCRIPTION =
      "The string value representing the default value of "
          + "WhatsApp configuration used to send WhatsApp message. If empty, SMS module will use default config set in this module";

  public static final String ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID =
      "messages.concept.attributeType.answerRegex.uuid";
  public static final String ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID_DESC =
      "The UUID of Concept Attribute Type with Answer Regex.";

  public static final String ITR_PROVIDER_TEMPLATE_NAME_CONCEPT_ATTR_TYPE_UUID =
      "messages.concept.attributeType" + ".providerTemplateName.uuid";
  public static final String ITR_PROVIDER_TEMPLATE_NAME_CONCEPT_ATTR_TYPE_UUID_DESC =
      "The UUID of Concept Attribute Type with "
          + "message provider name of with template of the message defined by Concept.";

  public static final String ITR_IMAGE_URL_CONCEPT_ATTR_TYPE_UUID =
      "messages.concept.attributeType" + ".imageUrl.uuid";
  public static final String ITR_IMAGE_URL_CONCEPT_ATTR_TYPE_UUID_DESC =
      "The UUID of Concept Attribute Type with " + "image URL to send as message.";

  public static final String ITR_MESSAGE_TEXT_ATTR_TYPE_UUID =
      "messages.concept.attributeType.messageText.uuid";
  public static final String ITR_MESSAGE_TEXT_ATTR_TYPE_UUID_DESC = "The text of the message.";

  public static final String ITR_DEFAULT_ITR_MESSAGE_CONCEPT_UUID =
      "messages.concept.defaultITRMessage.uuid";
  public static final String ITR_DEFAULT_ITR_MESSAGE_CONCEPT_UUID_DESC =
      "The UUID of Concept with default ITR message.";

  public static final String ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID =
      "messages.dailyAdherenceQuestion.uuid";
  public static final String ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID_DESC =
      "The UUID of Concept which represents a "
          + "question of actor responses with daily adherence data. The Actor Responses with this Concept are taken into "
          + "account during calculation of Adherence Feedback for an Adherence report daily. The textual answer is "
          + "expected, 'YES' for positive answer and 'NO' for negative, any other value is ignored";
  public static final String ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID_DEFAULT_VALUE =
      "e387f386-15fd-4ddd-8460-effa47cd4a11";

  public static final String ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID =
      "messages.weeklyAdherenceQuestion.uuid";
  public static final String ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID_DESC =
      "The UUID of Concept which represents a "
          + "question of actor responses with weekly adherence data. The Actor Responses with this Concept are taken into "
          + "account during calculation of Adherence Feedback for an Adherence report weekly. The numeric answer is "
          + "expected, it should be a number of days the patient took adhered to treatment.";
  public static final String ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID_DEFAULT_VALUE =
      "e1b9b42d-5901-4f34-b1c7-af53e238cba2";

  private ConfigConstants() {}
}
