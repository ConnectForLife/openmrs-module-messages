package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.List;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.Template}
 * and {@link org.openmrs.module.messages.api.dto.TemplateDTO} resources in both ways.
 */
public class TemplateMapper extends AbstractMapper<TemplateDTO, Template> {

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
        List<TemplateField> templateFields = templateFieldMapper.fromDtos(dto.getTemplateFields());
        Template template = new Template();
        template.setId(dto.getId());
        template.setServiceQuery(dto.getServiceQuery());
        template.setServiceQueryType(dto.getServiceQueryType());
        template.setTemplateFields(templateFields);
        template.setName(dto.getName());
        if (dto.getUuid() != null) {
            template.setUuid(dto.getUuid());
        }
        return template;
    }

    public TemplateMapper setTemplateFieldMapper(TemplateFieldMapper templateFieldMapper) {
        this.templateFieldMapper = templateFieldMapper;
        return this;
    }
}
