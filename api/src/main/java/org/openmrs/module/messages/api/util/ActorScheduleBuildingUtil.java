/* * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.util;

import static org.openmrs.module.messages.api.model.ChannelType.DEACTIVATED;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.ChannelType;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public final class ActorScheduleBuildingUtil {

    public static final String ERROR_INFORMATION = "Unable to display schedule";

    private static final Log LOGGER = LogFactory.getLog(ActorScheduleBuildingUtil.class);
    private static final String SCHEDULE_ELEMENTS_SEPARATOR = ", ";
    private static final String FIELD_CONTENT_SEPARATOR = ",";
    private static final String DEFAULT_INFORMATION = "Schedule data not specified";

    public static ActorScheduleDTO build(PatientTemplate patientTemplate) {
        Integer actorId = buildActorId(patientTemplate);
        String actorType = patientTemplate.getActorTypeAsString();
        String schedule = buildSchedule(patientTemplate);
        return new ActorScheduleDTO(actorId, actorType, schedule);
    }

    private static Integer buildActorId(PatientTemplate patientTemplate) {
        return patientTemplate.getActorType() == null ? null :
            getActorId(patientTemplate.getActorType(), patientTemplate.getPatient().getId());
    }

    private static String buildSchedule(PatientTemplate patientTemplate) {
        try {
            String serviceType = getTemplateFieldValue(patientTemplate,
                TemplateFieldType.SERVICE_TYPE, true);

            List<String> scheduleElements = new ArrayList<>();

            if (isDeactivated(serviceType)) {
                addDeactivatedElement(scheduleElements);
            } else {
                addFrequencyElement(scheduleElements, patientTemplate);

                addDayOfWeekElement(scheduleElements, patientTemplate);

                addStartDateElement(scheduleElements, patientTemplate);

                addEndDateElement(scheduleElements, patientTemplate);

                addCategoriesElement(scheduleElements, patientTemplate);
            }

            String schedule = StringUtils.join(scheduleElements, SCHEDULE_ELEMENTS_SEPARATOR);
            return StringUtils.defaultIfEmpty(schedule, DEFAULT_INFORMATION);
        } catch (Exception ex) {
            LOGGER.error(ERROR_INFORMATION, ex);
            return ERROR_INFORMATION;
        }
    }

    private static void addDeactivatedElement(List<String> scheduleElements) {
        scheduleElements.add(MessagesConstants.DEACTIVATED_SCHEDULE_MESSAGE);
    }

    private static void addFrequencyElement(List<String> scheduleElements,
                                              PatientTemplate patientTemplate) {
        String frequencyDailyWeeklyMonthly = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.MESSAGING_FREQUENCY_DAILY_OR_WEEKLY_OR_MONTHLY, false);
        String frequencyWeeklyMonthly = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.MESSAGING_FREQUENCY_WEEKLY_OR_MONTHLY, false);

        String frequency = StringUtils.defaultIfEmpty(frequencyDailyWeeklyMonthly,
            frequencyWeeklyMonthly);

        if (StringUtils.isNotBlank(frequency)) {
            scheduleElements.add(String.format("%s", StringUtils.capitalize(frequency)));
        }
    }

    private static void addDayOfWeekElement(List<String> scheduleElements,
                                            PatientTemplate patientTemplate) {
        String dayOfWeek = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.DAY_OF_WEEK, false);
        String dayOfWeekSingle = getTemplateFieldValue(patientTemplate,
        TemplateFieldType.DAY_OF_WEEK_SINGLE, false);

        dayOfWeek = StringUtils.defaultIfEmpty(dayOfWeekSingle, dayOfWeek);

        if (StringUtils.isNotBlank(dayOfWeek) && dayOfWeek.split(FIELD_CONTENT_SEPARATOR).length > 0) {
            String days = DaysOfWeekUtil.generateDayOfWeekText(dayOfWeek.split(FIELD_CONTENT_SEPARATOR));
            scheduleElements.add(String.format("%s", days));
        }
    }

    private static void addStartDateElement(List<String> scheduleElements,
                                              PatientTemplate patientTemplate) {
        String startDate = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.START_OF_MESSAGES, true);
        startDate = getFormattedDateString(startDate);
        if (startDate != null) {
            scheduleElements.add(String.format("Starts: %s", startDate));
        }
    }

    private static void addEndDateElement(List<String> scheduleElements,
                                          PatientTemplate patientTemplate) {
        String endDate = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.END_OF_MESSAGES, true);

        if (endDate != null) {
            scheduleElements.add(String.format("Ends: %s", FieldDateUtil.getEndDateText(endDate)));
        }
    }

    private static void addCategoriesElement(List<String> scheduleElements,
                                             PatientTemplate patientTemplate) {
        String categories = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.CATEGORY_OF_MESSAGE, false);

        if (categories != null) {
            scheduleElements.add(String.format("Categories: %s", getCategoriesText(categories)));
        }
    }

    private static String getTemplateFieldValue(PatientTemplate patientTemplate,
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

    private static String getFormattedDateString(String date) {
        try {
            return DateUtil.convertServerSideDateFormatToFrontend(date);
        } catch (Exception e) {
            LOGGER.debug(e);
            return date;
        }
    }

    private static Integer getActorId(Relationship actorType, Integer patientId) {
        return patientId.equals(actorType.getPersonA().getId()) ?
            actorType.getPersonB().getId() :
            actorType.getPersonA().getId();
    }

    private static boolean isDeactivated(String serviceType) {
        return StringUtils.isBlank(serviceType)
            || DEACTIVATED.equals(ChannelType.fromName(serviceType));
    }

    private static String getCategoriesText(String categories) {
        return StringUtils.replace(categories, FIELD_CONTENT_SEPARATOR, SCHEDULE_ELEMENTS_SEPARATOR);
    }

    private ActorScheduleBuildingUtil() {
    }
}
