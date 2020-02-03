package org.openmrs.module.messages.api.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openmrs.Patient;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.Actor;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.service.ActorService;
import org.openmrs.module.messages.api.service.DefaultPatientTemplateService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.api.util.DefaultPatientTemplateWrapper;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

public class DefaultPatientTemplateServiceImpl extends BaseOpenmrsService implements DefaultPatientTemplateService {

    private PatientTemplateService patientTemplateService;

    private TemplateService templateService;

    private ActorService actorService;

    private MessageDetailsMapper messageDetailsMapper;

    @Override
    public List<PatientTemplate> generateDefaultPatientTemplates(Patient patient) {
        List<PatientTemplate> lacking = findLackingPatientTemplates(patient);
        if (lacking.size() == 0) {
            throw new ValidationException("The given patient has already saved all the patient " +
                "templates");
        }
        return patientTemplateService.saveOrUpdate(lacking);
    }

    @Override
    public List<PatientTemplate> findLackingPatientTemplates(Patient patient) {
        List<PatientTemplate> existing =
            patientTemplateService.findAllByCriteria(PatientTemplateCriteria.forPatientId(patient.getId()));

        return findLackingPatientTemplates(patient, existing);
    }

    @Override
    public List<PatientTemplate> findLackingPatientTemplates(Patient patient,
                                                             List<PatientTemplate> existing) {
        List<DefaultPatientTemplateWrapper> actual =
            DefaultPatientTemplateWrapper.wrapToList(existing);

        List<DefaultPatientTemplateWrapper> expected =
            DefaultPatientTemplateWrapper.wrapToList(getPatientTemplatesWithDefaultValues(patient));

        Set<DefaultPatientTemplateWrapper> diff = new HashSet<>();
        diff.addAll(expected);
        diff.removeAll(actual);
        return DefaultPatientTemplateWrapper.unwrapToList(diff);
    }

    @Override
    public MessageDetailsDTO getDetailsForRealAndDefault(Patient patient) {
        return getDetailsForRealAndDefault(patient, findLackingPatientTemplates(patient));
    }

    @Override
    public MessageDetailsDTO getDetailsForRealAndDefault(Patient patient,
                                                         List<PatientTemplate> lacking) {
        List<PatientTemplate> patientTemplates = patientTemplateService
            .findAllByCriteria(PatientTemplateCriteria.forPatientId(patient.getId()));
        patientTemplates.addAll(lacking);
        return messageDetailsMapper.toDto(patientTemplates).withPatientId(patient.getId());
    }

    public DefaultPatientTemplateServiceImpl setPatientTemplateService(PatientTemplateService patientTemplateService) {
        this.patientTemplateService = patientTemplateService;
        return this;
    }

    public DefaultPatientTemplateServiceImpl setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
        return this;
    }

    public DefaultPatientTemplateServiceImpl setActorService(ActorService actorService) {
        this.actorService = actorService;
        return this;
    }

    public DefaultPatientTemplateServiceImpl setMessageDetailsMapper(MessageDetailsMapper messageDetailsMapper) {
        this.messageDetailsMapper = messageDetailsMapper;
        return this;
    }

    private List<PatientTemplate> getPatientTemplatesWithDefaultValues(Patient patient) {
        List<PatientTemplate> patientTemplates = new ArrayList<>();
        List<Template> templates =
            templateService.getAll(false);
        List<Actor> actors = actorService.getAllActorsForPatientId(patient.getId());

        for (Template template : templates) {
            patientTemplates.add(
                new PatientTemplateBuilder(template, patient)
                    .build()
            );
        }
        for (Actor actor : actors) {
            for (Template template : templates) {
                patientTemplates.add(
                    new PatientTemplateBuilder(template, actor, patient)
                        .build()
                );
            }
        }
        return patientTemplates;
    }
}
