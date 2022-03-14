package org.openmrs.module.messages.api.mappers;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

import java.util.ArrayList;
import java.util.List;

public final class PatientTemplateMapper extends AbstractOpenMrsDataMapper<PatientTemplateDTO, PatientTemplate> {

    private TemplateFieldValueMapper valueMapper;

    @Override
    public PatientTemplateDTO toDto(PatientTemplate dao) {
        PatientTemplateDTO dto = new PatientTemplateDTO()
            .withId(dao.getId())
            .withTemplateFieldValues(valueMapper.toDtos(dao.getTemplateFieldValues()))
            .withPatientId(dao.getPatient().getId())
            .withTemplateId(dao.getTemplate().getId())
            .withActorId(dao.getActor().getId())
            .withUuid(dao.getUuid());

        if (dao.getActorType() != null) {
            dto.withActorTypeId(dao.getActorType().getId());
        }
        return dto;
    }

    @Override
    public PatientTemplate fromDto(PatientTemplateDTO dto) {
        PatientTemplate dao = new PatientTemplate();
        dao.setId(dto.getId());
        dao.setTemplateFieldValues(fromDtos(dao, dto.getTemplateFieldValues()));
        if (dto.getUuid() != null) {
            dao.setUuid(dto.getUuid());
        }

        Person actor = new Person();
        actor.setId(dto.getActorId());
        dao.setActor(actor);

        if (dto.getActorTypeId() != null) {
            Relationship actorType = new Relationship();
            actorType.setId(dto.getActorTypeId());
            dao.setActorType(actorType);
        }

        Patient patient = new Patient();
        patient.setId(dto.getPatientId());
        dao.setPatient(patient);

        Template template = new Template();
        template.setId(dto.getTemplateId());
        dao.setTemplate(template);

        return dao;
    }

    @Override
    public void updateFromDto(PatientTemplateDTO source, PatientTemplate target) {
        valueMapper.updateFromDtos(source.getTemplateFieldValues(), target.getTemplateFieldValues());

        target.setActor(new Person(source.getActorId()));
        target.setActorType(source.getActorTypeId() != null
                ? new Relationship(source.getActorTypeId())
                : null);
        target.setPatient(new Patient(source.getPatientId()));
        target.setTemplate(new Template(source.getTemplateId()));
    }

    public PatientTemplateMapper setValueMapper(TemplateFieldValueMapper valueMapper) {
        this.valueMapper = valueMapper;
        return this;
    }

    private List<TemplateFieldValue> fromDtos(PatientTemplate parent, List<TemplateFieldValueDTO> dtos) {
        List<TemplateFieldValue> daos = new ArrayList<>(dtos.size());
        for (TemplateFieldValueDTO dto : dtos) {
            TemplateFieldValue value = valueMapper.fromDto(dto);
            value.setPatientTemplate(parent);
            daos.add(value);
        }
        return daos;
    }
}
