package org.openmrs.module.messages.handler.impl;

import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.ActorResponse;

public class DailyBasedFeedbackCalculationHandlerImpl
    extends AbstractAdherenceFeedbackCalculationHandler {

  private static final String DAILY_ADHERENCE_SERVICE_NAME = "Adherence report daily";

  public DailyBasedFeedbackCalculationHandlerImpl(ActorResponseDao actorResponseDao) {
    super(DailyResponseStatistics::new, DAILY_ADHERENCE_SERVICE_NAME, actorResponseDao);
  }

  private static class DailyResponseStatistics extends ResponseStatistics {
    @Override
    public DailyResponseStatistics add(ActorResponse actorResponse) {
      if (POSITIVE_ANSWER_TEXT.equalsIgnoreCase(actorResponse.getTextResponse())) {
        ++positiveAnswers;
      }

      ++totalAnswers;
      return this;
    }
  }
}
