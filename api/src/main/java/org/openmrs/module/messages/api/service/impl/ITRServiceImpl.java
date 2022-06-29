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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Concept;
import org.openmrs.ConceptAttribute;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dao.ExtendedConceptDAO;
import org.openmrs.module.messages.api.model.itr.ITRMessage;
import org.openmrs.module.messages.api.model.itr.impl.ITRMessageConceptAdapterImpl;
import org.openmrs.module.messages.api.service.ITRService;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

/**
 * The default implementation of {@link ITRService}.
 */
public class ITRServiceImpl implements ITRService {
  private static final Log LOGGER = LogFactory.getLog(ITRService.class);

  private ExtendedConceptDAO extendedConceptDAO;

  @Override
  public Optional<ITRMessage> findResponse(String inputText) {
    return ofNullable(inputText).flatMap(this::findAnswerMatch).map(ITRMessageConceptAdapterImpl::new);
  }

  @Override
  public ITRMessage getDefaultResponse() {
    return new ITRMessageConceptAdapterImpl(getDefaultMessage());
  }

  @Override
  public ITRMessage getMessageByUuid(String uuid) {
    final Concept messageConcept = Context.getConceptService().getConceptByUuid(uuid);

    if (messageConcept == null) {
      throw new APIException(MessageFormat.format("Concept for message with UUID: {0} doesn't exist!", uuid));
    }

    return new ITRMessageConceptAdapterImpl(messageConcept);
  }

  public void setExtendedConceptDAO(ExtendedConceptDAO extendedConceptDAO) {
    this.extendedConceptDAO = extendedConceptDAO;
  }

  private Optional<Concept> findAnswerMatch(String inputText) {
    final String answerRegexAttrTypeUuid = getAnswerRegexAttrTypeUuid();
    final List<ConceptAttribute> candidates = extendedConceptDAO.getConceptAttributesByTypeUuid(answerRegexAttrTypeUuid);

    final List<ConceptAttribute> matches = new ArrayList<>();

    for (ConceptAttribute candidate : candidates) {
      if (Pattern.matches((String) candidate.getValue(), inputText)) {
        matches.add(candidate);
      }
    }

    if (matches.size() > 1) {
      printMultipleMatchesWarnMessage(inputText, matches);
    }

    return matches.isEmpty() ? Optional.empty() : Optional.of(matches.get(0).getConcept());
  }

  private String getAnswerRegexAttrTypeUuid() {
    final String uuid =
        Context.getAdministrationService().getGlobalProperty(ConfigConstants.ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID);

    if (StringUtils.isEmpty(uuid)) {
      throw new IllegalStateException(
          MessageFormat.format("UUID of Answer Regex Concept Attribute Type not set! Global Parameter: {0}",
              ConfigConstants.ITR_ANSWER_REGEX_CONCEPT_ATTR_TYPE_UUID));
    }

    return uuid;
  }

  private void printMultipleMatchesWarnMessage(String inputText, List<ConceptAttribute> matches) {
    final String matchesListText = matches
        .stream()
        .map(attribute -> attribute.getConcept().getName().getName() + ':' + attribute.getValueReference())
        .collect(Collectors.joining(", "));

    LOGGER.warn(MessageFormat.format(
        "There are more then one ITR Message Concept which fits the input text: {0}! The first will be used. Found " +
            "concepts: {1}", inputText, matchesListText));
  }

  private Concept getDefaultMessage() {
    final String uuid =
        Context.getAdministrationService().getGlobalProperty(ConfigConstants.ITR_DEFAULT_ITR_MESSAGE_CONCEPT_UUID);

    if (StringUtils.isEmpty(uuid)) {
      throw new IllegalStateException(MessageFormat.format("UUID of Default Message Concept not set! Global Parameter: {0}",
          ConfigConstants.ITR_DEFAULT_ITR_MESSAGE_CONCEPT_UUID));
    }

    return Context.getConceptService().getConceptByUuid(uuid);
  }
}
