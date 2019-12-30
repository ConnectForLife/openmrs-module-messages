package org.openmrs.module.messages.api.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.Template;

public final class MessageDetailsUtil {

    private static final String DEFAULT_ACTOR_TYPE = "";
    private static final String DEFAULT_SCHEDULE_STATE = "DEACTIVATED";

    public static MessageDetailsDTO attachDefaultTemplates(MessageDetailsDTO messageDetailsDTO,
                                                           List<Template> templates) {
        if (messageDetailsDTO.getMessages() == null) {
            throw new IllegalArgumentException("Messages collection cannot be null");
        }

        List<MessageDTO> messages = new ArrayList<>(messageDetailsDTO.getMessages());

        for (Template template : templates) {
            if (shouldTemplateBeAdded(messages, template)) {
                messages.add(new MessageDTO(template.getName(), null, null,
                    Collections.singletonList(new ActorScheduleDTO(DEFAULT_ACTOR_TYPE, DEFAULT_SCHEDULE_STATE))));
            }
        }
        return new MessageDetailsDTO(messages)
            .withPatientId(messageDetailsDTO.getPatientId());
    }

    private static boolean shouldTemplateBeAdded(List<MessageDTO> messages, Template template) {
        boolean isTemplatePresent = false;
        for (MessageDTO message : messages) {
            if (StringUtils.equals(message.getType(), template.getName())) {
                isTemplatePresent = true;
                break;
            }
        }
        return !isTemplatePresent;
    }

    private MessageDetailsUtil() {
    }
}
