/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.handler.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.ActorResponse;

import java.util.Optional;

import static java.util.Optional.of;
import static org.openmrs.module.messages.api.constants.ConfigConstants.ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID;
import static org.openmrs.module.messages.api.constants.ConfigConstants.ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID_DEFAULT_VALUE;

public class WeeklyBasedFeedbackCalculationHandlerImpl
    extends AbstractAdherenceFeedbackCalculationHandler {

  private static final String WEEKLY_ADHERENCE_SERVICE_NAME = "Adherence report weekly";

  public WeeklyBasedFeedbackCalculationHandlerImpl(ActorResponseDao actorResponseDao) {
    super(WeeklyResponseStatistics::new, WEEKLY_ADHERENCE_SERVICE_NAME, actorResponseDao);
  }

  @Override
  protected String getAdherenceFeedbackQuestionUuid() {
    return Context.getAdministrationService()
        .getGlobalProperty(
            ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID,
            ADHERENCE_FEEDBACK_WEEKLY_QUESTION_UUID_DEFAULT_VALUE);
  }

  private static class WeeklyResponseStatistics extends ResponseStatistics {
    @Override
    public WeeklyResponseStatistics add(ActorResponse actorResponse) {
      final Optional<Integer> weeklyAdherence =
          of(actorResponse.getTextResponse()).filter(StringUtils::isNumeric).map(Integer::parseInt);

      if (weeklyAdherence.isPresent()) {
        positiveAnswers += weeklyAdherence.get();
        totalAnswers += 7;
      }
      return this;
    }
  }
}
