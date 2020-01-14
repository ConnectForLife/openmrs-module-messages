package org.openmrs.module.messages.api.util;

import static org.openmrs.module.messages.api.constants.MessagesConstants.PATIENT_DEFAULT_ACTOR_TYPE;
import static org.openmrs.module.messages.api.constants.MessagesConstants.DEFAULT_FRONT_END_DATE_FORMAT;
import static org.openmrs.module.messages.api.model.ChannelType.DEACTIVATED;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.openmrs.module.messages.api.service.impl.MessagesSchedulerServiceImpl;

public final class ActorScheduleBuildingUtil {

    private static final Log LOGGER = LogFactory.getLog(MessagesSchedulerServiceImpl.class);

    private static final String SCHEDULE_ELEMENTS_SEPARATOR = ", ";
    private static final String DEFAULT_INFORMATION = "Schedule data not specified";

    public static ActorScheduleDTO build(PatientTemplate patientTemplate) {
        Integer actorId = buildActorId(patientTemplate);
        String actorType = buildActorType(patientTemplate);
        String schedule = buildSchedule(patientTemplate);
        return new ActorScheduleDTO(actorId, actorType, schedule);
    }

    private static Integer buildActorId(PatientTemplate patientTemplate) {
        return patientTemplate.getActorType() == null ? null :
            getActorId(patientTemplate.getActorType(), patientTemplate.getPatient().getId());
    }

    private static String buildActorType(PatientTemplate patientTemplate) {
        return patientTemplate.getActorType() == null ? PATIENT_DEFAULT_ACTOR_TYPE :
            getActorType(patientTemplate.getActorType(), patientTemplate.getPatient().getId());
    }

    private static Integer getActorId(Relationship actorType, Integer patientId) {
        return patientId.equals(actorType.getPersonA().getId()) ?
            actorType.getPersonB().getId() :
            actorType.getPersonA().getId();
    }

    private static String getActorType(Relationship actorType, Integer patientId) {
        return patientId.equals(actorType.getPersonA().getId()) ?
            actorType.getRelationshipType().getbIsToA() :
            actorType.getRelationshipType().getaIsToB();
    }

    private static String buildSchedule(PatientTemplate patientTemplate) {
        String serviceType = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.SERVICE_TYPE, true);

        List<String> scheduleElements = new ArrayList<>();

        if (StringUtils.isBlank(serviceType)
            || DEACTIVATED.equals(ChannelType.fromName(serviceType))) {
            addDeactivatedElement(scheduleElements);
        } else {
            addFrequencyElement(scheduleElements, patientTemplate);

            addStartDateElement(scheduleElements, patientTemplate);

            addEndDateElement(scheduleElements, patientTemplate);
        }

        String schedule = StringUtils.join(scheduleElements, SCHEDULE_ELEMENTS_SEPARATOR);
        return StringUtils.defaultIfEmpty(schedule, DEFAULT_INFORMATION);
    }

    private static void addDeactivatedElement(List<String> scheduleElements) {
        scheduleElements.add(MessagesConstants.DEACTIVATED_SCHEDULE_MESSAGE);
    }

    private static void addFrequencyElement(List<String> scheduleElements,
                                              PatientTemplate patientTemplate) {
        String frequency = getTemplateFieldValue(patientTemplate,
            TemplateFieldType.MESSAGING_FREQUENCY, false);
        if (frequency != null) {
            scheduleElements.add(String.format("%s", StringUtils.capitalize(frequency)));
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
        Date endDate = patientTemplate.getEndOfMessages();

        if (endDate != null) {
            SimpleDateFormat newDateFormat = new SimpleDateFormat(DEFAULT_FRONT_END_DATE_FORMAT);
            scheduleElements.add(String.format("Ends: %s", newDateFormat.format(endDate)));
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

    private ActorScheduleBuildingUtil() {
    }
}
