package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public class TemplateFieldValueMapper extends AbstractMapper<TemplateFieldValueDTO, TemplateFieldValue> {

    @Override
    public TemplateFieldValueDTO toDto(TemplateFieldValue dao) {
        return new TemplateFieldValueDTO()
            .setId(dao.getId())
            .setValue(dao.getValue())
            .setTemplateFieldId(dao.getTemplateField().getId());
    }

    @Override
    public TemplateFieldValue fromDto(TemplateFieldValueDTO dto) {
        TemplateFieldValue dao = new TemplateFieldValue();
        dao.setId(dto.getId());
        dao.setValue(dto.getValue());

        PatientTemplate patientTemplate = new PatientTemplate();
        dao.setPatientTemplate(patientTemplate);

        TemplateField templateField = new TemplateField();
        templateField.setId(dto.getTemplateFieldId());
        dao.setTemplateField(templateField);

        return dao;
    }
}
