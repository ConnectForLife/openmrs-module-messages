/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.constants;

public final class ConfigConstants {

    public static final String MODULE_ID = "messages";

    public static final String PERSON_PHONE_ATTR = "Telephone Number";

    public static final String ACTOR_TYPES_KEY = "messages.actor.types";
    public static final String ACTOR_TYPES_DEFAULT_VALUE = "";
    public static final String ACTOR_TYPES_DESCRIPTION = "Comma separated list of relationship types used to control the "
            + "list of possible targets of message (not including the patient as a target). Example of usage: "
            + "'relationshipTypeUuid:directionOfRelationship,secondRelationshipTypeUuid:directionOfRelationship'";

    public static final String PERSON_CONTACT_TIME_ATTRIBUTE_TYPE_NAME = "Best contact time";
    public static final String PERSON_CONTACT_TIME_TYPE_DESCRIPTION = "Attribute stores the value of time which best "
            + "suitable for person to perform the contact";
    public static final String PERSON_CONTACT_TIME_TYPE_FORMAT = "java.util.Date";
    public static final String PERSON_CONTACT_TIME_TYPE_UUID = "7a8ad9aa-bd0e-48c0-826d-fa628f1db644";

    public static final String PERSON_STATUS_ATTRIBUTE_TYPE_NAME = "Person status";
    public static final String PERSON_STATUS_ATTRIBUTE_TYPE_DESCRIPTION = "Person status attribute";
    public static final String PERSON_STATUS_ATTRIBUTE_TYPE_FORMAT = "java.lang.String";
    public static final String PERSON_STATUS_ATTRIBUTE_TYPE_UUID = "dda246c6-c806-402a-9b7c-e2c1574a6441";

    public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_NAME = "Person status - reason";
    public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_DESCRIPTION = "Reason of change the person status";
    public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_FORMAT = "java.lang.String";
    public static final String PERSON_STATUS_REASON_ATTRIBUTE_TYPE_UUID = "da7a9976-c681-4182-a7f4-f9f6b31a9058";

    public static final String PERSON_STATUS_POSSIBLE_REASONS_KEY = "message.personStatus.reason.values";
    public static final String PERSON_STATUS_POSSIBLE_REASONS_DEFAULT_VALUE = "Deceased,Lost to Follow Up,"
            + "Temporary pause,On vacation,Transferred,Other";
    public static final String PERSON_STATUS_POSSIBLE_REASONS_DESCRIPTION = "Comma separated list of possible values for "
            + "changing the person status. Example: 'Deceased,Temporary pause,On vacation,Transferred,Other'";

    public static final String PERSON_STATUS_CONFIGURATION_KEY = "message.personStatus.configuration";
    public static final String PERSON_STATUS_CONFIGURATION_DEFAULT_VALUE = "[\n"
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
    public static final String PERSON_STATUS_CONFIGURATION_DESCRIPTION = "Person status configuration in JSON-like format."
            + "Example: [\n"
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

    public static final String CONSENT_CONTROL_KEY = "message.consent.validation";
    public static final String CONSENT_CONTROL_DEFAULT_VALUE = "true";
    public static final String CONSENT_CONTROL_DESCRIPTION = "Used to determine if Person Consent (Person Status) "
            + "should be used or not. Possible values: true / false.";

    public static final String GLOBAL_BEST_CONTACT_TIME_KEY = "global";
    public static final String GLOBAL_BEST_CONTACT_TIME_VALUE = "10:00";
    public static final String BEST_CONTACT_TIME_KEY = "message.bestContactTime.default";
    public static final String BEST_CONTACT_TIME_DEFAULT_VALUE =
            "{\n"
            + "  \"" + GLOBAL_BEST_CONTACT_TIME_KEY + "\": \"" + GLOBAL_BEST_CONTACT_TIME_VALUE + "\",\n"
            + "  \"acec590b-825e-45d2-876a-0028f174903d\": \"10:00\"\n"
            + "}";
    public static final String BEST_CONTACT_TIME_DESCRIPTION = "Map of actor types and their default contact times" +
            " for services for different actor types. Example of usage: " + BEST_CONTACT_TIME_DEFAULT_VALUE;

    public static final String ACTOR_BEST_CONTACT_TIME_KEY = "message.bestContactTime.actor";

    public static final String ACTOR_BEST_CONTACT_TIME_DEFAULT_VALUE =
            "{\"acec590b-825e-45d2-876a-0028f174903d\": \"10:00\"}";

    public static final String ACTOR_BEST_CONTACT_TIME_DESCRIPTION = "Map of actor types and their default contact times" +
            " for services for different actor types. Example of usage: "
            + "{\"relationshipTypeUuid\": \"10:00\", \"secondRelationshipTypeUuid\": \"12:20\"}";

    public static final String DAYS_NUMBER_BEFORE_VISIT_REMINDER_KEY = "message.daysToCallBeforeVisit.default";
    public static final String DAYS_NUMBER_BEFORE_VISIT_REMINDER_DEFAULT_VALUE = "1,7";
    public static final String DAYS_NUMBER_BEFORE_VISIT_REMINDER_DESCRIPTION =
            "Used to determine the how many days before visit reminder should be schedule";

    public static final String DEFAULT_RESCHEDULING_STRATEGY = "messages.failedMessageReschedulingStrategy";

    public static final String RESCHEDULING_STRATEGY_KEY = "messages.reschedulingStrategy";
    public static final String RESCHEDULING_STRATEGY_DEFAULT_VALUE =
            "SMS:messages.failedMessageReschedulingStrategy,"
            + "Call:messages.failedAndItsPendingMessagesReschedulingStrategy";
    public static final String RESCHEDULING_STRATEGY_DESCRIPTION =
            "Used to determine the name of the Spring Bean which should be used to handle the reschedule logic. The "
            + "value could represent multiple strategies. Each of them is dedicated to a specific channel type. The "
            + "particular bean is represented by channel name and bean name separated by a colon. If the system "
            + "supports multiple channels next entries must be separated by a comma. Example of usage: "
            + "'channelType1:beanName1,channelType2:beanName2'.";

    public static final String MAX_NUMBER_OF_RESCHEDULING_KEY = "messages.maxNumberOfRescheduling";
    public static final String MAX_NUMBER_OF_RESCHEDULING_DEFAULT_VALUE = "3";
    public static final String MAX_NUMBER_OF_RESCHEDULING_DESCRIPTION =
            "Used to determine the number of maximum attempts that can be taken for failing ScheduledServices. After "
            + "exceeding the number of retries, services will be denied rescheduling next task. Changing this value "
            + "will not influence on tasks which already exceeded the counter. In the primary approach, it is designed "
            + "to be used by the strategy FailedMessageReschedulingStrategy.";

    public static final String TIME_INTERVAL_TO_NEXT_RESCHEDULE_KEY = "messages.timeIntervalToNextReschedule";
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
    
    public static final String CUT_OFF_SCORE_FOR_ADHERENCE_TREND_KEY = "messages.cutOffScoreForAdherenceTrend";
    public static final String CUT_OFF_SCORE_FOR_ADHERENCE_TREND_DEFAULT_VALUE = "5";
    public static final String CUT_OFF_SCORE_FOR_ADHERENCE_TREND_DESCRIPTION =
            "Used to specify a percentage cut-off score for adherence trend";
    
    public static final String BENCHMARK_PERIOD_KEY = "messages.benchmarkPeriod";
    public static final String BENCHMARK_PERIOD_DEFAULT_VALUE = "7";
    public static final String BENCHMARK_PERIOD_DESCRIPTION =
            "Used to specify the number of days from which adherence trend is calculated";
    
    private ConfigConstants() {
    }
}
