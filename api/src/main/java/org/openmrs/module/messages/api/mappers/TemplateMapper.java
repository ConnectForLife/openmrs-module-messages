package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.Iterator;
import java.util.List;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.Template}
 * and {@link org.openmrs.module.messages.api.dto.TemplateDTO} resources in both ways.
 */
public class TemplateMapper extends AbstractMapper<TemplateDTO, Template> implements UpdateMapper<Template> {

    private TemplateFieldMapper templateFieldMapper;

    @Override
    public TemplateDTO toDto(Template dao) {
        List<TemplateFieldDTO> templateFields = templateFieldMapper.toDtos(dao.getTemplateFields());
        return new TemplateDTO()
                .setId(dao.getId())
                .setServiceQuery(dao.getServiceQuery())
                .setServiceQueryType(dao.getServiceQueryType())
                .setTemplateFields(templateFields)
                .setName(dao.getName())
                .setUuid(dao.getUuid());
    }

    @Override
    public Template fromDto(TemplateDTO dto) {
        Template template = new Template();
        template.setName(dto.getName());
        template.setId(dto.getId());
        template.setServiceQuery(dto.getServiceQuery());
        template.setServiceQueryType(dto.getServiceQueryType());
        mapTemplateFieldsFromDTO(dto, template);
        if (dto.getUuid() != null) {
            template.setUuid(dto.getUuid());
        }
        return template;
    }

    private void mapTemplateFieldsFromDTO(TemplateDTO dto, Template template) {
        List<TemplateField> templateFields = templateFieldMapper.fromDtos(dto.getTemplateFields());
        for (TemplateField templateField : templateFields) {
            templateField.setTemplate(template);
        }
        template.setTemplateFields(templateFields);
    }

    @Override
    public Template update(Template existingTemplate, Template newTemplate) {
        existingTemplate.setName(newTemplate.getName());
        existingTemplate.setServiceQuery(newTemplate.getServiceQuery());
        existingTemplate.setServiceQueryType(newTemplate.getServiceQueryType());
        updateExistingTemplateFields(existingTemplate, newTemplate);
        addNewTemplateFields(existingTemplate, newTemplate);
        return existingTemplate;
    }

    public TemplateMapper setTemplateFieldMapper(TemplateFieldMapper templateFieldMapper) {
        this.templateFieldMapper = templateFieldMapper;
        return this;
    }

    private void updateExistingTemplateFields(Template existingTemplate, Template newTemplate) {
        Iterator<TemplateField> iterator = existingTemplate.getTemplateFields().iterator();
        while (iterator.hasNext()) {
            TemplateField field = iterator.next();
            TemplateField newField = getFieldById(field.getId(), newTemplate.getTemplateFields());
            if (newField == null) {
                existingTemplate.getTemplateFields().remove(field);
            } else {
                updateTemplateField(field, newField);
            }
        }
    }

    private TemplateField getFieldById(Integer id, List<TemplateField> templateFields) {
        TemplateField field = null;
        for (TemplateField newField : templateFields) {
            if (newField.getId() != null && newField.getId().equals(id)) {
                field = newField;
                break;
            }
        }
        return field;
    }

    private void updateTemplateField(TemplateField field, TemplateField newField) {
        if (newField != null) {
            field.setName(newField.getName());
            field.setMandatory(newField.getMandatory());
            field.setDefaultValue(newField.getDefaultValue());
            field.setTemplateFieldType(newField.getTemplateFieldType());
        }
    }

    private void addNewTemplateFields(Template existingTemplate, Template newTemplate) {
        List<TemplateField> fields = existingTemplate.getTemplateFields();
        for (TemplateField newField : newTemplate.getTemplateFields()) {
            if (newField.getId() == null) {
                fields.add(newField);
            }
        }
    }
}
