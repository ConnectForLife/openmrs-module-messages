package org.openmrs.module.messages.api.mappers;

import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.dto.TemplateFieldDefaultValueDTO;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;

public class TemplateFieldDefaultValueMapper extends AbstractMapper<TemplateFieldDefaultValueDTO,
        TemplateFieldDefaultValue> implements UpdateMapper<TemplateFieldDefaultValueDTO, TemplateFieldDefaultValue> {

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
    public void updateFromDto(TemplateFieldDefaultValueDTO source, TemplateFieldDefaultValue target) {
        target.setRelationshipType(new RelationshipType(source.getRelationshipTypeId()));
        target.setDirection(source.getDirection());
        target.setTemplateField(new TemplateField(source.getTemplateFieldId()));
        target.setDefaultValue(source.getDefaultValue());
    }
}
