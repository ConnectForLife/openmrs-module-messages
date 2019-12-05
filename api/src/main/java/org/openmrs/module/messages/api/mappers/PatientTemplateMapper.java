package org.openmrs.module.messages.api.mappers;

import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;

public final class PatientTemplateMapper extends AbstractMapper<PatientTemplateDTO, PatientTemplate> {

    private TemplateFieldValueMapper valueMapper;

    public PatientTemplateMapper setValueMapper(TemplateFieldValueMapper valueMapper) {
        this.valueMapper = valueMapper;
        return this;
    }

    @Override
    public PatientTemplateDTO toDto(PatientTemplate dao) {
        return new PatientTemplateDTO()
            .setId(dao.getId())
            .setTemplateFieldValues(valueMapper.toDtos(dao.getTemplateFieldValues()))
            .setPatientId(dao.getPatient().getId())
            .setTemplateId(dao.getTemplate().getId())
            .setActorId(dao.getActor().getId())
            .setActorTypeId(dao.getActorType().getId());
    }

    @Override
    public PatientTemplate fromDto(PatientTemplateDTO dto) {
        throw new UnsupportedOperationException();
    }
}
