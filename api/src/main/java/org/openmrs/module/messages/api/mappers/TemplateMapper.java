package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.Template}
 * and {@link org.openmrs.module.messages.api.dto.TemplateDTO} resources in both ways.
 */
public class TemplateMapper {

    private TemplateFieldMapper templateFieldMapper;

    public List<TemplateDTO> toDtos(List<Template> daos) {
        List<TemplateDTO> dtos = new ArrayList<TemplateDTO>();
        for (Template dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    public List<Template> fromDtos(List<TemplateDTO> dtos) {
        List<Template> daos = new ArrayList<Template>();
        for (TemplateDTO dto : dtos) {
            daos.add(fromDto(dto));
        }
        return daos;
    }

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

    public Template fromDto(TemplateDTO dto) {
        throw new UnsupportedOperationException();  //TODO add the convertion between DTO to DAO
    }

    public TemplateMapper setTemplateFieldMapper(TemplateFieldMapper templateFieldMapper) {
        this.templateFieldMapper = templateFieldMapper;
        return this;
    }
}
