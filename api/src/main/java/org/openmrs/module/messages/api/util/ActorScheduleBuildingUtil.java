package org.openmrs.module.messages.api.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.impl.MessagesSchedulerServiceImpl;

public final class ActorScheduleBuildingUtil {

    private static final Log LOGGER = LogFactory.getLog(MessagesSchedulerServiceImpl.class);

    private static final String SCHEDULE_ELEMENTS_SEPARATOR = ", ";
    private static final String DEFAULT_INFORMATION = "Schedule data not specified";

    public static ActorScheduleDTO build(PatientTemplate patientTemplate) {
        String actorType = buildActorType(patientTemplate);
        String schedule = buildSchedule(patientTemplate);
        return new ActorScheduleDTO(actorType, schedule);
    }

    private static String buildActorType(PatientTemplate patientTemplate) {
        String actorType = patientTemplate.getActorType() == null ? null :
            patientTemplate.getActorType().getRelationshipType().getaIsToB();
        return StringUtils.defaultIfEmpty(actorType, StringUtils.EMPTY);
    }

    private static String buildSchedule(PatientTemplate patientTemplate) {
        List<String> scheduleElements = new ArrayList<>();

        addFrequencyElement(scheduleElements, patientTemplate);

        addStartDateElement(scheduleElements, patientTemplate);

        addEndDateElement(scheduleElements, patientTemplate);

        String schedule = StringUtils.join(scheduleElements, SCHEDULE_ELEMENTS_SEPARATOR);
        return StringUtils.defaultIfEmpty(schedule, DEFAULT_INFORMATION);
    }

    private static void addFrequencyElement(List<String> scheduleElements,
                                              PatientTemplate patientTemplate) {
        String frequency = getTemplateFieldValue(patientTemplate, TemplateFieldType.MESSAGING_FREQUENCY);
        if (frequency != null) {
            scheduleElements.add(String.format("%s", StringUtils.capitalize(frequency)));
        }
    }

    private static void addStartDateElement(List<String> scheduleElements,
                                              PatientTemplate patientTemplate) {
        String startDate = getTemplateFieldValue(patientTemplate, TemplateFieldType.START_OF_MESSAGES);
        startDate = getFormattedDateString(startDate);
        if (startDate != null) {
            scheduleElements.add(String.format("Starts: %s", startDate));
        }
    }

    private static void addEndDateElement(List<String> scheduleElements,
                                          PatientTemplate patientTemplate) {
        String endDate = getTemplateFieldValue(patientTemplate, TemplateFieldType.END_OF_MESSAGES);
        endDate = getFormattedDateString(endDate);
        if (endDate != null) {
            scheduleElements.add(String.format("Ends: %s", endDate));
        }
    }

    private static String getTemplateFieldValue(PatientTemplate patientTemplate,
                                                TemplateFieldType fieldType) {
        String value = null;
        for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
            if (templateFieldValue.getTemplateField().getTemplateFieldType().equals(fieldType)) {
                value = templateFieldValue.getValue();
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

    private ActorScheduleBuildingUtil() {

    }
}