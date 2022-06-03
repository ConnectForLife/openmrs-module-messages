/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.api.ConceptService;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.messages.api.dto.HealthTipDTO;
import org.openmrs.module.messages.api.service.HealthTipService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

public class HealthTipServiceImpl implements HealthTipService {

  private static final String PATIENT_ID_PARAM_NAME = "patientId";

  private static final String ACTOR_ID_PARAM_NAME = "actorId";

  private static final String HEALTH_TIP_QUESTION_CONCEPT_UUID =
          "e7541654-c327-4fee-8e48-f729946c7791";

  private static final String CATEGORIES_OF_THE_MESSAGE_FIELD_NAME = "Categories of the message";

  private DbSessionFactory dbSessionFactory;

  private ConceptService conceptService;

  @Override
  @Transactional(readOnly = true)
  public Concept getNextHealthTipToPlay(Integer patientId, Integer actorId, String category) {
    requireNonNull(patientId, "The getNextHealthTipToPlay.patientId must not be null!");
    requireNonNull(patientId, "The getNextHealthTipToPlay.actorId must not be null!");

    HealthTipDTO healthTipDTO = getNextHealthTipDTO(patientId, actorId, category);
    if (healthTipDTO != null) {
      return conceptService.getConcept(healthTipDTO.getHealthTipId());
    }

    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public String getNextHealthTipText(Integer patientId, Integer actorId, String category) {
    HealthTipDTO healthTipDTO = getNextHealthTipDTO(patientId, actorId, category);
    if (healthTipDTO != null) {
      return healthTipDTO.getHealthTipText();
    }

    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public HealthTipDTO getNextHealthTipDTO(Integer patientId, Integer actorId, String category) {
    List<HealthTipDTO> healthTipDTOS = getHealthTipDTOs(patientId, actorId, category);
    if (CollectionUtils.isNotEmpty(healthTipDTOS)) {
      return healthTipDTOS.get(0);
    }
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<HealthTipDTO> getHealthTipDTOs(Integer patientId, Integer actorId, String category) {
    String categoriesToSearch =
            StringUtils.isNotBlank(category) ? category : getHealthTipCategories(patientId, actorId);
    String query =
            "SELECT healthTipCategoryId, healthTipCategoryName, healthTipId, healthTipText from\n"
                    + "(\n"
                    + "SELECT cs.concept_set AS healthTipCategoryId, cn.name AS healthTipCategoryName, cs.concept_id AS healthTipId, cd.description AS healthTipText\n"
                    + "FROM concept_set cs\n"
                    + "INNER JOIN concept c ON cs.concept_set = c.concept_id\n"
                    + "INNER JOIN concept_name cn ON c.concept_id = cn.concept_id\n"
                    + "INNER JOIN concept_description cd ON cd.concept_id = cs.concept_id\n"
                    + "WHERE cn.name IN ("
                    + getHtCategoriesAsMultipleValuesInOneString(categoriesToSearch)
                    + ")\n"
                    + "GROUP BY cs.concept_id\n"
                    + "ORDER BY cs.sort_weight, FIND_IN_SET(cn.name, '"
                    + categoriesToSearch
                    + "')"
                    + ") htConcepts";

    String currentActorResponsesIds =
            getActorResponsesIdsSeparatedByComma(patientId, actorId, category);
    if (StringUtils.isNotBlank(currentActorResponsesIds)) {
      query = query.concat(" WHERE healthTipId NOT IN (" + currentActorResponsesIds + ");");
    }

    List<Object[]> queryResult = dbSessionFactory.getCurrentSession().createSQLQuery(query).list();

    return queryResult.stream().map(HealthTipDTO::new).collect(Collectors.toList());
  }

  @Override
  @Transactional(readOnly = true)
  public String getHealthTipCategories(Integer patientId, Integer actorId) {
    String query =
            "SELECT mtfv.value FROM messages_template_field_value mtfv\n"
                    + "INNER JOIN messages_patient_template mpt ON mtfv.patient_template_id = mpt.messages_patient_template_id\n"
                    + "INNER JOIN messages_template_field mtf ON mtfv.template_field_id = mtf.messages_template_field_id\n"
                    + "WHERE mpt.patient_id = :patientId\n"
                    + "AND mpt.actor_id = :actorId\n"
                    + "AND mtf.name = '"
                    + CATEGORIES_OF_THE_MESSAGE_FIELD_NAME
                    + "';";

    return (String)
            dbSessionFactory
                    .getCurrentSession()
                    .createSQLQuery(query)
                    .setParameter(PATIENT_ID_PARAM_NAME, patientId)
                    .setParameter(ACTOR_ID_PARAM_NAME, actorId)
                    .uniqueResult();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getHealthTipIdsAlreadyPlayed(
          Integer patientId, Integer actorId, String category) {
    String query =
            "SELECT mar.response FROM messages_actor_response mar\n"
                    + "JOIN concept c ON c.concept_id = mar.question\n"
                    + "WHERE c.uuid = '"
                    + HEALTH_TIP_QUESTION_CONCEPT_UUID
                    + "'\n"
                    + "AND mar.patient_id = :patientId\n"
                    + "AND mar.actor_id = :actorId\n"
                    + "AND mar.response IN ("
                    + getAllPossibleHealthTipIdsSeparatedByComma(patientId, actorId, category)
                    + ")\n"
                    + "ORDER BY mar.answered_time DESC\n"
                    + "LIMIT "
                    + getNumberOfPossibleHealthTipIds(patientId, actorId, category);

    return dbSessionFactory
            .getCurrentSession()
            .createSQLQuery(query)
            .setParameter(PATIENT_ID_PARAM_NAME, patientId)
            .setParameter(ACTOR_ID_PARAM_NAME, actorId)
            .list();
  }

  @Override
  @Transactional(readOnly = true)
  public List<Integer> getAllPossibleHealthTipIds(
          Integer patientId, Integer actorId, String category) {
    String categoriesToSearch =
            StringUtils.isNotBlank(category) ? category : getHealthTipCategories(patientId, actorId);
    String query =
            "SELECT cs.concept_id FROM concept_set cs\n"
                    + "JOIN concept c ON cs.concept_set = c.concept_id\n"
                    + "JOIN concept_name cn ON c.concept_id = cn.concept_id\n"
                    + "WHERE cn.name IN ("
                    + getHtCategoriesAsMultipleValuesInOneString(categoriesToSearch)
                    + ");\n";

    return dbSessionFactory.getCurrentSession().createSQLQuery(query).list();
  }

  @Override
  @Transactional(readOnly = true)
  public Integer getNumberOfPossibleHealthTipIds(
          Integer patientId, Integer actorId, String category) {
    return getAllPossibleHealthTipIds(patientId, actorId, category).size();
  }

  // Output format e.g. 'Category1', 'Category2', 'Category3', 'Category4', 'Category5'
  private String getHtCategoriesAsMultipleValuesInOneString(String categories) {
    return Arrays.stream(categories.split(",")).collect(Collectors.joining("','", "'", "'"));
  }

  private String getActorResponsesIdsSeparatedByComma(
          Integer patientId, Integer actorId, String category) {
    List<Integer> actorResponsesIds = getHealthTipIdsAlreadyPlayed(patientId, actorId, category);

    return actorResponsesIds.stream().map(String::valueOf).collect(Collectors.joining(","));
  }

  private String getAllPossibleHealthTipIdsSeparatedByComma(
          Integer patientId, Integer actorId, String category) {
    return getAllPossibleHealthTipIds(patientId, actorId, category).stream()
            .map(String::valueOf)
            .collect(Collectors.joining(","));
  }

  public void setDbSessionFactory(DbSessionFactory dbSessionFactory) {
    this.dbSessionFactory = dbSessionFactory;
  }

  public void setConceptService(ConceptService conceptService) {
    this.conceptService = conceptService;
  }
}
