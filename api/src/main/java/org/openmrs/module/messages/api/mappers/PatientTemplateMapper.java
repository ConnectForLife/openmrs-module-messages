package org.openmrs.module.messages.api.mappers;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;

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
            .setServiceQuery(dao.getServiceQuery())
            .setServiceQueryType(dao.getServiceQueryType())
            .setActorId(dao.getActor().getId())
            .setActorTypeId(dao.getActorType().getId());
    }

    @Override
    public PatientTemplate fromDto(PatientTemplateDTO dto) {
        PatientTemplate dao = new PatientTemplate();
        dao.setId(dto.getId());
        dao.setTemplateFieldValues(valueMapper.fromDtos(dto.getTemplateFieldValues()));

        Person actor = new Person();
        actor.setId(dto.getActorId());
        dao.setActor(actor);

        Relationship actorType = new Relationship();
        actorType.setId(dto.getActorTypeId());
        dao.setActorType(actorType);

        Patient patient = new Patient();
        patient.setId(dto.getPatientId());
        dao.setPatient(patient);

        Template template = new Template();
        template.setId(dto.getTemplateId());
        dao.setTemplate(template);

        dao.setServiceQuery(dto.getServiceQuery());
        dao.setServiceQueryType(dto.getServiceQueryType());

        return dao;
    }
}
