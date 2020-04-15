package org.openmrs.module.messages.api.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.Template;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public final class MessageDetailsUtil {

    public static MessageDetailsDTO attachDefaultMessageDetails(MessageDetailsDTO messageDetailsDTO,
                                                                List<Template> templates,
                                                                List<Actor> actors) {
        if (messageDetailsDTO.getMessages() == null) {
            throw new IllegalArgumentException("Messages collection cannot be null");
        }

        List<MessageDTO> messages = new ArrayList<>(messageDetailsDTO.getMessages());
        for (Template template : templates) {
            MessageDTO messageDTO = findMatchingMessage(messages, template);
            if (messageDTO == null) {
                messageDTO = new MessageDTO(template.getName());
                messages.add(messageDTO);
            }
            addSchedulesToMessage(messageDTO, actors);
        }

        return new MessageDetailsDTO(messages)
            .withPersonId(messageDetailsDTO.getPersonId());
    }

    private static MessageDTO findMatchingMessage(List<MessageDTO> messages, Template template) {
        for (MessageDTO message : messages) {
            if (StringUtils.equals(message.getType(), template.getName())) {
                return message;
            }
        }
        return null;
    }

    private static void addSchedulesToMessage(MessageDTO messageDTO, List<Actor> actors) {
        List<ActorScheduleDTO> actorSchedules = messageDTO.getActorSchedules();
        actorSchedules = attachPatientScheduleIfNeeded(actorSchedules);
        actorSchedules = attachActorSchedulesIfNeeded(actorSchedules, actors);
        Collections.sort(actorSchedules);
        messageDTO.setActorSchedules(actorSchedules);
    }

    private static List<ActorScheduleDTO> attachPatientScheduleIfNeeded(List<ActorScheduleDTO> actorSchedules) {
        List<ActorScheduleDTO> schedules = new ArrayList<ActorScheduleDTO>(actorSchedules);
        if (!containsScheduleForPatient(actorSchedules)) {
            schedules.add(new ActorScheduleDTO(MessagesConstants.PATIENT_DEFAULT_ACTOR_TYPE,
                    MessagesConstants.DEACTIVATED_SCHEDULE_MESSAGE));
        }
        return schedules;
    }

    private static List<ActorScheduleDTO> attachActorSchedulesIfNeeded(List<ActorScheduleDTO> actorSchedules,
                                                     List<Actor> actors) {
        Comparator<ActorScheduleDTO> schedulesComparator = new Comparator<ActorScheduleDTO>() {
            @Override
            public int compare(ActorScheduleDTO first, ActorScheduleDTO second) {
                return new CompareToBuilder()
                    .append(first.getActorId(), second.getActorId())
                    .append(first.getActorType(), second.getActorType())
                    .toComparison();
            }
        };
        SortedSet<ActorScheduleDTO> schedules = new TreeSet<ActorScheduleDTO>(schedulesComparator);
        schedules.addAll(actorSchedules);
        schedules.addAll(prepareDefaultSchedulesForActors(actors));
        return new ArrayList<ActorScheduleDTO>(schedules);
    }

    private static boolean containsScheduleForPatient(List<ActorScheduleDTO> schedules) {
        for (ActorScheduleDTO schedule : schedules) {
            if (MessagesConstants.PATIENT_DEFAULT_ACTOR_TYPE.equals(schedule.getActorType())) {
               return true;
            }
        }
        return false;
    }

    private static List<ActorScheduleDTO> prepareDefaultSchedulesForActors(List<Actor> actors) {
        List<ActorScheduleDTO> expectedSchedules = new ArrayList<ActorScheduleDTO>();

        if (actors != null) {
            for (Actor actor : actors) {
                expectedSchedules.add(new ActorScheduleDTO(actor.getTarget().getId(),
                    ActorUtil.getActorTypeName(actor), MessagesConstants.DEACTIVATED_SCHEDULE_MESSAGE));
            }
        }
        return expectedSchedules;
    }

    private MessageDetailsUtil() {
    }
}
