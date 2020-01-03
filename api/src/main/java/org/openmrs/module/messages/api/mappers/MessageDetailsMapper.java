package org.openmrs.module.messages.api.mappers;

import org.apache.commons.lang.NotImplementedException;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDetailsMapper implements ListMapper<MessageDetailsDTO, PatientTemplate> {

    private MessageMapper messageMapper;

    public void setMessageMapper(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @Override
    public MessageDetailsDTO toDto(List<PatientTemplate> patientTemplates) {
        Map<Template, List<PatientTemplate>> groups = groupByTemplates(patientTemplates);
        return new MessageDetailsDTO(mapToMessages(groups));
    }

    @Override
    public List<PatientTemplate> fromDto(MessageDetailsDTO dao) {
        throw new NotImplementedException("mapping from MessageDetailsDTO to List<PatientTemplate> " +
                "is not implemented yet");
    }

    private List<MessageDTO> mapToMessages(Map<Template, List<PatientTemplate>> map) {
        List<MessageDTO> result = new ArrayList<>();

        for (List<PatientTemplate> list : map.values()) {
            result.add(messageMapper.toDto(list));
        }

        return result;
    }

    private Map<Template, List<PatientTemplate>> groupByTemplates(List<PatientTemplate> daos) {
        Map<Template, List<PatientTemplate>> map = new HashMap<>();

        for (PatientTemplate patientTemplate : daos) {
            Template template = patientTemplate.getTemplate();
            if (map.containsKey(template)) {
                map.get(template).add(patientTemplate);
            } else {
                map.put(template, new ArrayList<>());
                map.get(template).add(patientTemplate);
            }
        }

        return map;
    }
}
