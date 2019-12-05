package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public class TemplateFieldValueMapper extends AbstractMapper<TemplateFieldValueDTO, TemplateFieldValue> {

    @Override
    public TemplateFieldValueDTO toDto(TemplateFieldValue dao) {
        return new TemplateFieldValueDTO()
            .setId(dao.getId())
            .setTemplateFieldId(dao.getTemplateField().getId())
            .setValue(dao.getValue());
    }

    @Override
    public TemplateFieldValue fromDto(TemplateFieldValueDTO dto) {
            throw new UnsupportedOperationException();
    }
}
