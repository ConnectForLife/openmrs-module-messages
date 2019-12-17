package org.openmrs.module.messages.api.mappers;

import java.util.ArrayList;
import java.util.List;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

public final class PatientTemplateMapper extends AbstractMapper<PatientTemplateDTO, PatientTemplate> {

    private TemplateFieldValueMapper valueMapper;

    public PatientTemplateMapper setValueMapper(TemplateFieldValueMapper valueMapper) {
        this.valueMapper = valueMapper;
        return this;
    }

    @Override
    public PatientTemplateDTO toDto(PatientTemplate dao) {
        PatientTemplateDTO dto = new PatientTemplateDTO()
            .withId(dao.getId())
            .withTemplateFieldValues(valueMapper.toDtos(dao.getTemplateFieldValues()))
            .withPatientId(dao.getPatient().getId())
            .withTemplateId(dao.getTemplate().getId())
            .withServiceQuery(dao.getServiceQuery())
            .withServiceQueryType(dao.getServiceQueryType())
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
        dao.setUuid(dto.getUuid());

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

        dao.setServiceQuery(dto.getServiceQuery());
        dao.setServiceQueryType(dto.getServiceQueryType());

        return dao;
    }

    private List<TemplateFieldValue> fromDtos(PatientTemplate parent, List<TemplateFieldValueDTO> dtos) {
        List<TemplateFieldValue> daos = new ArrayList<TemplateFieldValue>();
        for (TemplateFieldValueDTO dto : dtos) {
            TemplateFieldValue value = valueMapper.fromDto(dto);
            value.setPatientTemplate(parent);
            daos.add(value);
        }
        return daos;
    }
}
