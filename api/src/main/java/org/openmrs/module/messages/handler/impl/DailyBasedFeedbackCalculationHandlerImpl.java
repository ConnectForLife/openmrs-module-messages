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

import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.ActorResponse;

import static org.openmrs.module.messages.api.constants.ConfigConstants.ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID;
import static org.openmrs.module.messages.api.constants.ConfigConstants.ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID_DEFAULT_VALUE;

public class DailyBasedFeedbackCalculationHandlerImpl
    extends AbstractAdherenceFeedbackCalculationHandler {

  private static final String DAILY_ADHERENCE_SERVICE_NAME = "Adherence report daily";
  private static final String POSITIVE_ANSWER_TEXT = "YES";
  private static final String NEGATIVE_ANSWER_TEXT = "NO";

  public DailyBasedFeedbackCalculationHandlerImpl(ActorResponseDao actorResponseDao) {
    super(DailyResponseStatistics::new, DAILY_ADHERENCE_SERVICE_NAME, actorResponseDao);
  }

  @Override
  protected String getAdherenceFeedbackQuestionUuid() {
    return Context.getAdministrationService()
        .getGlobalProperty(
            ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID,
            ADHERENCE_FEEDBACK_DAILY_QUESTION_UUID_DEFAULT_VALUE);
  }

  private static class DailyResponseStatistics extends ResponseStatistics {
    @Override
    public DailyResponseStatistics add(ActorResponse actorResponse) {
      if (POSITIVE_ANSWER_TEXT.equalsIgnoreCase(actorResponse.getTextResponse())) {
        ++positiveAnswers;
        ++totalAnswers;
      } else if (NEGATIVE_ANSWER_TEXT.equalsIgnoreCase(actorResponse.getTextResponse())) {
        ++totalAnswers;
      }

      return this;
    }
  }
}
