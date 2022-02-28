package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptSet;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.HealthTipService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.service.TemplateFieldValueService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.domain.criteria.TemplateCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class HealthTipServiceImpl implements HealthTipService {

  private static final String HEALTH_TIP_SERVICE_NAME = "Health tip";

  private static final String CATEGORIES_OF_THE_MESSAGE_FIELD_NAME = "Categories of the message";

  private static final String HEALTH_TIP_QUESTION_CONCEPT_UUID =
      "e7541654-c327-4fee-8e48-f729946c7791";

  private static final Log LOGGER = LogFactory.getLog(HealthTipServiceImpl.class);

  private ConceptService conceptService;

  @Override
  public Concept getNextHealthTipToPlay(Integer patientId, Integer actorId, String category) {
    requireNonNull(patientId, "The getNextHealthTipToPlay.patientId must not be null!");
    requireNonNull(patientId, "The getNextHealthTipToPlay.actorId must not be null!");

    Integer nextHealthTipIdToPlay = findNextHealthTipId(patientId, actorId, category);
    return nextHealthTipIdToPlay != null ? conceptService.getConcept(nextHealthTipIdToPlay) : null;
  }

  public void setConceptService(ConceptService conceptService) {
    this.conceptService = conceptService;
  }

  private Integer findNextHealthTipId(Integer patientId, Integer actorId, String category) {
    PatientTemplate healthTipPatientTemplate =
        getHealthTipPatientTemplate(patientId, actorId, HEALTH_TIP_SERVICE_NAME);
    Integer nextHealthTipIdToPlay = null;
    if (healthTipPatientTemplate != null) {
      if (StringUtils.isNotBlank(category)) {
        nextHealthTipIdToPlay =
            getNextHealthTipIdFromParticularCategory(patientId, actorId, category);
      } else {
        nextHealthTipIdToPlay =
            getNextHealthTipIdFromAllCategories(
                patientId, actorId, getPatientHealthTipCategories(healthTipPatientTemplate));
      }
    } else {
      LOGGER.warn(
          String.format(
              "Patient template for patient id: %d, actor id: %d and service name: %s not found",
              patientId, actorId, HEALTH_TIP_SERVICE_NAME));
    }
    return nextHealthTipIdToPlay;
  }

  private PatientTemplate getHealthTipPatientTemplate(
      Integer patientId, Integer actorId, String serviceName) {
    Template template =
        Context.getService(TemplateService.class)
            .findOneByCriteria(TemplateCriteria.forName(serviceName));
    PatientTemplateCriteria criteria =
        PatientTemplateCriteria.forPatientAndActorAndTemplate(patientId, actorId, template.getId());

    return Context.getService(PatientTemplateService.class).findOneByCriteria(criteria);
  }

  private Integer getNextHealthTipIdFromParticularCategory(
      Integer patientId, Integer actorId, String category) {
    Integer nextHealthTipId = null;
    Concept categoryConcept = conceptService.getConceptByName(category);
    if (categoryConcept != null) {
      List<Integer> possibleHealthTipIds =
          conceptService.getConceptSetsByConcept(categoryConcept).stream()
              .map(cs -> cs.getConcept().getConceptId())
              .collect(Collectors.toList());
      List<Integer> healthTipsIdsAlreadyHeard =
          getHealthTipsIdsAlreadyHeard(patientId, actorId, possibleHealthTipIds);
      nextHealthTipId = getNextHealthTipIdToPlay(possibleHealthTipIds, healthTipsIdsAlreadyHeard);
    } else {
      LOGGER.debug(String.format("Health tip category with name %s not found", category));
    }

    return nextHealthTipId;
  }

  private Integer getNextHealthTipIdFromAllCategories(
      Integer patientId, Integer actorId, List<String> healthTipCategories) {
    Integer nextHealthTipId = null;
    if (CollectionUtils.isNotEmpty(healthTipCategories)) {
      List<Integer> possibleHealthTipIds = getAllHealthTipIds(healthTipCategories);
      List<Integer> healthTipsIdsAlreadyHeard =
          getHealthTipsIdsAlreadyHeard(patientId, actorId, possibleHealthTipIds);
      nextHealthTipId = getNextHealthTipIdToPlay(possibleHealthTipIds, healthTipsIdsAlreadyHeard);
    } else {
      LOGGER.debug("No health tip category found");
    }

    return nextHealthTipId;
  }

  private List<String> getPatientHealthTipCategories(PatientTemplate healthTipPatientTemplate) {
    List<String> healthTipCategories = new ArrayList<>();
    TemplateFieldValue templateFieldValue =
        Context.getService(TemplateFieldValueService.class)
            .getTemplateFieldByPatientTemplateAndFieldType(
                healthTipPatientTemplate, CATEGORIES_OF_THE_MESSAGE_FIELD_NAME);
    if (templateFieldValue != null) {
      healthTipCategories = Arrays.asList(templateFieldValue.getValue().split(","));
    }
    return healthTipCategories;
  }

  private Integer getNextHealthTipIdToPlay(
      List<Integer> possibleHealthTipIds, List<Integer> healthTipsIdsAlreadyHeard) {
    return possibleHealthTipIds.stream()
        .filter(htId -> !healthTipsIdsAlreadyHeard.contains(htId))
        .findFirst()
        .orElse(null);
  }

  private List<Integer> getHealthTipsIdsAlreadyHeard(
      Integer patientId, Integer actorId, List<Integer> possibleHealthTipIds) {
    List<Integer> healthTipsIdsAlreadyHeard = new ArrayList<>();
    int responseCount = 0;
    if (CollectionUtils.isNotEmpty(possibleHealthTipIds)) {
      responseCount = possibleHealthTipIds.size() - 1;
    }

    Concept healthTipQuestion = conceptService.getConceptByUuid(HEALTH_TIP_QUESTION_CONCEPT_UUID);
    if (healthTipQuestion != null) {
      List<ActorResponse> healthTipActorResponses =
          Context.getService(MessagingService.class)
              .getLastActorResponsesForConceptQuestion(
                  patientId, actorId, healthTipQuestion.getConceptId(), responseCount);
      healthTipActorResponses.forEach(
          actorResponse ->
              healthTipsIdsAlreadyHeard.add(actorResponse.getResponse().getConceptId()));
    }

    return healthTipsIdsAlreadyHeard;
  }

  private List<Integer> getAllHealthTipIds(List<String> healthTipCategories) {
    List<ConceptSet> allHealthTipSets = getAllHealthTipSets(healthTipCategories);
    List<ConceptSet> resultConceptSets =
        getHealthTipIdsInCorrectOrder(allHealthTipSets, healthTipCategories);

    return resultConceptSets.stream()
        .map(cs -> cs.getConcept().getConceptId())
        .collect(Collectors.toList());
  }

  private List<ConceptSet> getAllHealthTipSets(List<String> healthTipCategories) {
    List<ConceptSet> allHealthTipSets = new ArrayList<>();
    for (String categoryName : healthTipCategories) {
      Concept categoryConcept = conceptService.getConceptByName(categoryName);
      if (categoryConcept != null) {
        allHealthTipSets.addAll(categoryConcept.getConceptSets());
      }
    }
    return allHealthTipSets;
  }

  private List<ConceptSet> getHealthTipIdsInCorrectOrder(
      List<ConceptSet> allHealthTipSets, List<String> healthTipCategories) {
    List<ConceptSet> resultConceptSets = new ArrayList<>();
    int index = 0;
    while (CollectionUtils.isNotEmpty(allHealthTipSets)) {
      Concept healthTipCategory = conceptService.getConceptByName(healthTipCategories.get(index));
      Optional<ConceptSet> foundHealthTipSet =
          allHealthTipSets.stream()
              .filter(htSet -> htSet.getConceptSet().equals(healthTipCategory))
              .min(Comparator.comparing(ConceptSet::getSortWeight));
      if (foundHealthTipSet.isPresent()) {
        ConceptSet healthTipSet = foundHealthTipSet.get();
        resultConceptSets.add(healthTipSet);
        allHealthTipSets.remove(healthTipSet);
      }

      index++;
      if (index >= healthTipCategories.size()) {
        index = 0;
      }
    }

    return resultConceptSets;
  }
}
