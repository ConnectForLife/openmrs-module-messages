package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.TemplateField}
 * and {@link org.openmrs.module.messages.api.dto.TemplateFieldDTO} resources in both ways.
 */
public class TemplateFieldMapper extends AbstractMapper<TemplateFieldDTO, TemplateField> {

    private TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper;

    @Override
    public TemplateFieldDTO toDto(TemplateField dao) {
        String fieldType = dao.getTemplateFieldType() == null ? null :
                dao.getTemplateFieldType().name();
        return new TemplateFieldDTO()
                .setId(dao.getId())
                .setName(dao.getName())
                .setMandatory(dao.getMandatory())
                .setDefaultValue(dao.getDefaultValue())
                .setType(fieldType)
                .setUuid(dao.getUuid())
                .setDefaultValues(templateFieldDefaultValueMapper.toDtos(dao.getDefaultValues()));
    }

    @Override
    public TemplateField fromDto(TemplateFieldDTO dto) {
        TemplateField templateField = new TemplateField();
        templateField.setId(dto.getId());
        templateField.setName(dto.getName());
        templateField.setMandatory(dto.getMandatory());
        templateField.setDefaultValue(dto.getDefaultValue());
        TemplateFieldType type = TemplateFieldType.valueOf(dto.getType());
        templateField.setTemplateFieldType(type);
        if (dto.getUuid() != null) {
            templateField.setUuid(dto.getUuid());
        }
        templateField.setDefaultValues(templateFieldDefaultValueMapper.fromDtos(dto.getDefaultValues()));
        return templateField;
    }

    public TemplateFieldMapper setTemplateFieldDefaultValueMapper(
            TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper) {
        this.templateFieldDefaultValueMapper = templateFieldDefaultValueMapper;
        return this;
    }
}
