package org.openmrs.module.messages.api.service.impl;

import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.messages.api.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.api.constants.MessagesConstants;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements methods related to the creating of default patient templates
 */
public class DefaultPatientTemplateServiceImpl extends BaseOpenmrsService implements DefaultPatientTemplateService {

    private PatientTemplateService patientTemplateService;

    private TemplateService templateService;

    private ActorService actorService;

    private MessageDetailsMapper messageDetailsMapper;

    @Override
    public List<PatientTemplate> generateDefaultPatientTemplates(Patient patient) {
        List<PatientTemplate> lacking = findLackingPatientTemplates(patient);
        if (lacking.size() == 0) {
            throw new ValidationException("The given patient has already saved all the patient templates");
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
    public List<PatientTemplate> findLackingPatientTemplates(Patient patient, List<PatientTemplate> existing) {
        List<DefaultPatientTemplateWrapper> actual = DefaultPatientTemplateWrapper.wrapToList(existing);

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
    public MessageDetailsDTO getDetailsForRealAndDefault(Patient patient, List<PatientTemplate> lacking) {
        List<PatientTemplate> patientTemplates =
                patientTemplateService.findAllByCriteria(PatientTemplateCriteria.forPatientId(patient.getId()));
        patientTemplates.addAll(lacking);
        return messageDetailsMapper.toDto(patientTemplates).withPersonId(patient.getId());
    }

    @Override
    public List<Concept> getHealthTipCategoryConcepts() {
        final ConceptService conceptService = Context.getConceptService();

        final ConceptClass conceptClass =
                conceptService.getConceptClassByName(MessagesConstants.HEALTH_TIP_CATEGORY_CLASS_NAME);

        if (conceptClass == null) {
            throw new IllegalStateException(new StringBuilder("Could not get Health Tip Categories. The Concept Class '")
                    .append(MessagesConstants.HEALTH_TIP_CATEGORY_CLASS_NAME)
                    .append("' was not found!")
                    .toString());
        }

        return conceptService.getConceptsByClass(conceptClass);
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
        List<Template> templates = templateService.getAll(false);
        List<Actor> actors = actorService.getAllActorsForPatientId(patient.getId());

        List<PatientTemplate> patientTemplates = new ArrayList<>(templates.size() + templates.size() * actors.size());
        for (Template template : templates) {
            patientTemplates.add(new PatientTemplateBuilder(template, patient).build());
            for (Actor actor : actors) {
                patientTemplates.add(new PatientTemplateBuilder(template, actor, patient).build());
            }
        }
        return patientTemplates;
    }
}
