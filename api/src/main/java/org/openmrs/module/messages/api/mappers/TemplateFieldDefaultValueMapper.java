package org.openmrs.module.messages.api.mappers;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.dto.TemplateFieldDefaultValueDTO;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;

import java.util.ArrayList;
import java.util.List;

public class TemplateFieldDefaultValueMapper extends AbstractMapper<TemplateFieldDefaultValueDTO,
        TemplateFieldDefaultValue> {
    @Override
    public TemplateFieldDefaultValueDTO toDto(TemplateFieldDefaultValue dao) {
        return new TemplateFieldDefaultValueDTO()
                .setId(dao.getId())
                .setRelationshipTypeId(dao.getRelationshipType().getId())
                .setDirection(dao.getDirection())
                .setTemplateFieldId(dao.getTemplateField().getId())
                .setDefaultValue(dao.getDefaultValue());
    }

    @Override
    public List<TemplateFieldDefaultValueDTO> toDtos(List<TemplateFieldDefaultValue> daos) {
        List<TemplateFieldDefaultValueDTO> dtos = new ArrayList<TemplateFieldDefaultValueDTO>();
        for (TemplateFieldDefaultValue dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    @Override
    public TemplateFieldDefaultValue fromDto(TemplateFieldDefaultValueDTO dto) {
        TemplateFieldDefaultValue templateFieldDefaultValue = new TemplateFieldDefaultValue();
        templateFieldDefaultValue.setId(dto.getId());
        templateFieldDefaultValue.setRelationshipType(new RelationshipType(dto.getRelationshipTypeId()));
        templateFieldDefaultValue.setDirection(dto.getDirection());
        templateFieldDefaultValue.setTemplateField(new TemplateField(dto.getTemplateFieldId()));
        templateFieldDefaultValue.setDefaultValue(dto.getDefaultValue());
        return templateFieldDefaultValue;
    }

    @Override
    public List<TemplateFieldDefaultValue> fromDtos(List<TemplateFieldDefaultValueDTO> dtos) {
        List<TemplateFieldDefaultValue> daos = new ArrayList<TemplateFieldDefaultValue>();
        for (TemplateFieldDefaultValueDTO dto : dtos) {
            daos.add(fromDto(dto));
        }
        return daos;
    }
}
