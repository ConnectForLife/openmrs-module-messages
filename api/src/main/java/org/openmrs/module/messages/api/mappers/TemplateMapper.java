package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.Template}
 * and {@link org.openmrs.module.messages.api.dto.TemplateDTO} resources in both ways.
 */
public class TemplateMapper extends AbstractMapper<TemplateDTO, Template> implements UpdateMapper<TemplateDTO, Template> {

    private TemplateFieldMapper templateFieldMapper;

    @Override
    public TemplateDTO toDto(Template dao) {
        List<TemplateFieldDTO> templateFields = templateFieldMapper.toDtos(dao.getTemplateFields());
        return new TemplateDTO()
                .setId(dao.getId())
                .setServiceQuery(dao.getServiceQuery())
                .setCalendarServiceQuery(dao.getCalendarServiceQuery())
                .setServiceQueryType(dao.getServiceQueryType())
                .setTemplateFields(templateFields)
                .setName(dao.getName())
                .setCreatedAt(dao.getDateCreated())
                .setUuid(dao.getUuid());
    }

    @Override
    public Template fromDto(TemplateDTO dto) {
        Template template = new Template();
        template.setName(dto.getName());
        template.setId(dto.getId());
        template.setServiceQuery(dto.getServiceQuery());
        template.setCalendarServiceQuery(dto.getCalendarServiceQuery());
        template.setServiceQueryType(dto.getServiceQueryType());
        mapTemplateFieldsFromDto(dto, template);
        if (dto.getUuid() != null) {
            template.setUuid(dto.getUuid());
        }
        return template;
    }

    @Override
    public void updateFromDto(TemplateDTO newTemplate, Template existingTemplate) {
        existingTemplate.setName(newTemplate.getName());
        templateFieldMapper.updateFromDtos(newTemplate.getTemplateFields(), existingTemplate.getTemplateFields());
    }

    public TemplateMapper setTemplateFieldMapper(TemplateFieldMapper templateFieldMapper) {
        this.templateFieldMapper = templateFieldMapper;
        return this;
    }

    @Override
    protected void doSafeDelete(Template target) {
        target.setRetired(Boolean.TRUE);
    }

    private void mapTemplateFieldsFromDto(TemplateDTO dto, Template template) {
        final Set<TemplateField> templateFields = new HashSet<>(templateFieldMapper.fromDtos(dto.getTemplateFields()));
        for (TemplateField templateField : templateFields) {
            templateField.setTemplate(template);
        }
        template.setTemplateFields(templateFields);
    }
}
