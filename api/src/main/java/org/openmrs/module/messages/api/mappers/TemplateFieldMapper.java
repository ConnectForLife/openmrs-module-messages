package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;

import java.util.ArrayList;
import java.util.List;

/**
 * Convert between {@link org.openmrs.module.messages.api.model.TemplateField}
 * and {@link org.openmrs.module.messages.api.dto.TemplateFieldDTO} resources in both ways.
 */
public class TemplateFieldMapper {

    public List<TemplateFieldDTO> toDtos(List<TemplateField> daos) {
        List<TemplateFieldDTO> dtos = new ArrayList<TemplateFieldDTO>();
        for (TemplateField dao : daos) {
            dtos.add(toDto(dao));
        }
        return dtos;
    }

    public List<TemplateField> fromDtos(List<TemplateFieldDTO> dtos) {
        List<TemplateField> daos = new ArrayList<TemplateField>();
        for (TemplateFieldDTO dto : dtos) {
            daos.add(fromDto(dto));
        }
        return daos;
    }

    public TemplateFieldDTO toDto(TemplateField dao) {
        String fieldType = dao.getTemplateFieldType() == null ? null :
                dao.getTemplateFieldType().name();
        return new TemplateFieldDTO()
                .setId(dao.getId())
                .setName(dao.getName())
                .setMandatory(dao.getMandatory())
                .setDefaultValue(dao.getDefaultValue())
                .setTemplateFieldType(fieldType)
                .setUuid(dao.getUuid());
    }

    public TemplateField fromDto(TemplateFieldDTO dto) {
        throw new UnsupportedOperationException(); //TODO add the convertion between DTO to DAO
    }
}
