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
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.HealthTipService;
import org.openmrs.module.messages.api.service.MessagingService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class HealthTipServiceImpl implements HealthTipService {

  private static final String HEALTH_TIP_SERVICE_NAME = "Health tip";

  private static final String CATEGORIES_OF_THE_MESSAGE_FIELD_NAME = "Categories of the message";

  private static final String HEALTH_TIP_QUESTION_CONCEPT_UUID =
      "e7541654-c327-4fee-8e48-f729946c7791";

  private static final Log LOGGER = LogFactory.getLog(HealthTipServiceImpl.class);

  private PatientTemplateService patientTemplateService;

  private ConceptService conceptService;

  private MessagingService messagingService;

  @Override
  public Concept getNextHealthTipToPlay(Integer patientId, Integer actorId, String category) {
    requireNonNull(patientId, "The getNextHealthTipToPlay.patientId must not be null!");
    requireNonNull(patientId, "The getNextHealthTipToPlay.actorId must not be null!");

    PatientTemplate healthTipPatientTemplate =
        findPatientTemplateByPatientAndActorAndService(patientId, actorId, HEALTH_TIP_SERVICE_NAME);
    Integer nextHealthTipIdToPlay = null;
    if (healthTipPatientTemplate != null) {
      if (StringUtils.isNotBlank(category)) {
        nextHealthTipIdToPlay =
            getNextHealthTipIdFromParticularCategory(patientId, actorId, category);
      } else {
        List<String> healthTipCategories =
            Arrays.asList(
                getTemplateFieldValue(
                        healthTipPatientTemplate, CATEGORIES_OF_THE_MESSAGE_FIELD_NAME)
                    .split(","));
        nextHealthTipIdToPlay =
            getNextHealthTipIdFromAllCategories(patientId, actorId, healthTipCategories);
      }
    }

    return nextHealthTipIdToPlay != null
        ? Context.getConceptService().getConcept(nextHealthTipIdToPlay)
        : null;
  }

  public void setPatientTemplateService(PatientTemplateService patientTemplateService) {
    this.patientTemplateService = patientTemplateService;
  }

  public void setConceptService(ConceptService conceptService) {
    this.conceptService = conceptService;
  }

  public void setMessagingService(MessagingService messagingService) {
    this.messagingService = messagingService;
  }

  private Integer getNextHealthTipIdFromParticularCategory(
      Integer patientId, Integer actorId, String category) {
    Integer nextHealthTipId = null;
    Concept categoryConcept = conceptService.getConceptByName(category);
    if (categoryConcept != null) {
      List<ConceptSet> conceptSetsByCategory =
          conceptService.getConceptSetsByConcept(categoryConcept);
      sortConceptSetsBySortWeight(conceptSetsByCategory);
      List<Integer> possibleHealthTipIds = getAllConceptIdsByConceptSets(conceptSetsByCategory);
      List<Integer> healthTipsIdsAlreadyHeard =
          getHealthTipsIdsAlreadyHeard(patientId, actorId, possibleHealthTipIds);
      nextHealthTipId = getNextHealthTipIdToPlay(possibleHealthTipIds, healthTipsIdsAlreadyHeard);
    } else if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(String.format("Health tip category with name %s not found", category));
    }

    return nextHealthTipId;
  }

  private Integer getNextHealthTipIdFromAllCategories(
      Integer patientId, Integer actorId, List<String> healthTipCategories) {
    Integer nextHealthTipId = null;
    if (CollectionUtils.isNotEmpty(healthTipCategories)) {
      List<ConceptSet> allConceptSetsByCategories =
          getAllConceptSetsByCategories(healthTipCategories);
      sortConceptSetsBySortWeight(allConceptSetsByCategories);
      List<Integer> possibleHealthTipIds =
          getAllConceptIdsByConceptSets(allConceptSetsByCategories);
      List<Integer> healthTipsIdsAlreadyHeard =
          getHealthTipsIdsAlreadyHeard(patientId, actorId, possibleHealthTipIds);
      nextHealthTipId = getNextHealthTipIdToPlay(possibleHealthTipIds, healthTipsIdsAlreadyHeard);
    } else if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("No health tip category found");
    }

    return nextHealthTipId;
  }

  private Integer getNextHealthTipIdToPlay(
      List<Integer> possibleHealthTipIds, List<Integer> healthTipsIdsAlreadyHeard) {
    Integer healthTipIdToPlay = null;
    for (Integer conceptId : possibleHealthTipIds) {
      if (!healthTipsIdsAlreadyHeard.contains(conceptId)) {
        healthTipIdToPlay = conceptId;
        break;
      }
    }
    return healthTipIdToPlay;
  }

  private List<Integer> getHealthTipsIdsAlreadyHeard(
      Integer patientId, Integer actorId, List<Integer> possibleHealthTipIds) {
    List<Integer> healthTipsIdsAlreadyHeard = new ArrayList<>();
    int responseCount = 0;
    if (CollectionUtils.isNotEmpty(possibleHealthTipIds)) {
      responseCount = possibleHealthTipIds.size() - 1;
    }
    Concept healthTipQuestion = conceptService.getConceptByUuid(HEALTH_TIP_QUESTION_CONCEPT_UUID);
    List<ActorResponse> healthTipActorResponses =
        messagingService.getLastActorResponsesForConceptQuestion(
            patientId, actorId, healthTipQuestion.getConceptId(), responseCount);
    for (ActorResponse actorResponse : healthTipActorResponses) {
      healthTipsIdsAlreadyHeard.add(actorResponse.getResponse().getConceptId());
    }

    return healthTipsIdsAlreadyHeard;
  }

  private List<Integer> getAllConceptIdsByConceptSets(List<ConceptSet> conceptSets) {
    List<Integer> allConceptIdsByConceptSets = new ArrayList<>();
    for (ConceptSet conceptSet : conceptSets) {
      Concept concept = conceptSet.getConcept();
      if (concept != null) {
        allConceptIdsByConceptSets.add(concept.getConceptId());
      }
    }
    return allConceptIdsByConceptSets;
  }

  private void sortConceptSetsBySortWeight(List<ConceptSet> conceptSets) {
    Collections.sort(
        conceptSets,
        new Comparator<ConceptSet>() {
          @Override
          public int compare(ConceptSet o1, ConceptSet o2) {
            return o1.getSortWeight().compareTo(o2.getSortWeight());
          }
        });
  }

  private List<ConceptSet> getAllConceptSetsByCategories(List<String> healthTipCategoriesNames) {
    List<ConceptSet> allHealthTipCategoriesConceptClasses = new ArrayList<>();
    for (String categoryName : healthTipCategoriesNames) {
      Concept categoryConcept = conceptService.getConceptByName(categoryName);
      if (categoryConcept != null) {
        allHealthTipCategoriesConceptClasses.addAll(categoryConcept.getConceptSets());
      }
    }
    return allHealthTipCategoriesConceptClasses;
  }

  private static String getTemplateFieldValue(
      PatientTemplate patientTemplate, String templateFieldName) {
    String fieldValue = null;
    for (TemplateFieldValue templateFieldValue : patientTemplate.getTemplateFieldValues()) {
      if (StringUtils.equals(templateFieldValue.getTemplateField().getName(), templateFieldName)) {
        fieldValue = templateFieldValue.getValue();
        break;
      }
    }
    return fieldValue;
  }

  private PatientTemplate findPatientTemplateByPatientAndActorAndService(
      Integer patientId, Integer actorId, String serviceName) {
    PatientTemplateCriteria criteria =
        PatientTemplateCriteria.forPatientAndActor(patientId, actorId);
    List<PatientTemplate> patientTemplates = patientTemplateService.findAllByCriteria(criteria);
    PatientTemplate patientTemplate = null;
    for (PatientTemplate pt : patientTemplates) {
      if (StringUtils.equalsIgnoreCase(pt.getTemplate().getName(), serviceName)) {
        patientTemplate = pt;
        break;
      }
    }
    return patientTemplate;
  }
}
