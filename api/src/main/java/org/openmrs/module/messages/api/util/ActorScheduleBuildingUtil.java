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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldType;

import java.util.ArrayList;
import java.util.List;

import static org.openmrs.module.messages.api.util.PatientTemplateFieldUtil.getTemplateFieldValue;

public final class ActorScheduleBuildingUtil {

    public static final String SCHEDULE_ERROR_INFORMATION = "Unable to display schedule";
    public static final String PATIENT_NAME_ERROR_INFORMATION = "Unable to obtain patient's name";

    private static final Log LOGGER = LogFactory.getLog(ActorScheduleBuildingUtil.class);
    private static final String SCHEDULE_ELEMENTS_SEPARATOR = ", ";
    private static final String FIELD_CONTENT_SEPARATOR = ",";
    private static final String DEFAULT_INFORMATION = "Schedule data not specified";

    public static ActorScheduleDTO build(PatientTemplate patientTemplate) {
        Integer actorId = buildActorId(patientTemplate);
        String actorType = patientTemplate.getActorTypeAsString();
        String schedule = buildSchedule(patientTemplate);
        Integer patientId = buildPatientId(patientTemplate);
        String patientName = buildPatientName(patientTemplate);
        return new ActorScheduleDTO(actorId, actorType, schedule, patientId, patientName);
    }

    private static String buildPatientName(PatientTemplate patientTemplate) {
        String fullName = StringUtils.EMPTY;
        try {
            fullName = patientTemplate.getPatient().getPersonName().getFullName();
        } catch (Exception ex) {
            LOGGER.error(SCHEDULE_ERROR_INFORMATION, ex);
        }
        return fullName;
    }

    private static Integer buildPatientId(PatientTemplate patientTemplate) {
        return patientTemplate.getPatient().getId();
    }

    private static Integer buildActorId(PatientTemplate patientTemplate) {
        return patientTemplate.getActorType() == null ? null :
            getActorId(patientTemplate.getActorType(), patientTemplate.getPatient().getId());
    }

    private static String buildSchedule(PatientTemplate patientTemplate) {
        try {
            List<String> scheduleElements = new ArrayList<>();

            if (patientTemplate.isDeactivated()) {
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
            LOGGER.error(SCHEDULE_ERROR_INFORMATION, ex);
            return SCHEDULE_ERROR_INFORMATION;
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

    private static void addStartDateElement(List<String> scheduleElements, PatientTemplate patientTemplate) {
        final String startDateFieldValue = getTemplateFieldValue(patientTemplate, TemplateFieldType.START_OF_MESSAGES, true);
        final String startDateFormatted = getFormattedDateString(startDateFieldValue);

        if (startDateFormatted != null) {
            scheduleElements.add(String.format("Starts: %s", startDateFormatted));
        }
    }

    private static void addEndDateElement(List<String> scheduleElements, PatientTemplate patientTemplate) {
        final String endDateFieldValue = getTemplateFieldValue(patientTemplate, TemplateFieldType.END_OF_MESSAGES, true);

        if (endDateFieldValue != null) {
            scheduleElements.add(String.format("Ends: %s", FieldDateUtil.getEndDateText(endDateFieldValue)));
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

    private static String getFormattedDateString(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }

        try {
            return DateUtil.convertServerSideDateFormatToFrontend(date);
        } catch (Exception e) {
            LOGGER.error(e);
            return date;
        }
    }

    private static Integer getActorId(Relationship actorType, Integer patientId) {
        return patientId.equals(actorType.getPersonA().getId()) ?
            actorType.getPersonB().getId() :
            actorType.getPersonA().getId();
    }

    private static String getCategoriesText(String categories) {
        return StringUtils.replace(categories, FIELD_CONTENT_SEPARATOR, SCHEDULE_ELEMENTS_SEPARATOR);
    }

    private ActorScheduleBuildingUtil() {
    }
}
