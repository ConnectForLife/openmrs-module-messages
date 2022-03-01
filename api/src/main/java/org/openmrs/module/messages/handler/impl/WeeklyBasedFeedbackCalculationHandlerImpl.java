package org.openmrs.module.messages.handler.impl;

import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.ActorResponse;

import static java.util.Optional.of;

public class WeeklyBasedFeedbackCalculationHandlerImpl
    extends AbstractAdherenceFeedbackCalculationHandler {

  private static final String WEEKLY_ADHERENCE_SERVICE_NAME = "Adherence report weekly";

  public WeeklyBasedFeedbackCalculationHandlerImpl(ActorResponseDao actorResponseDao) {
    super(WeeklyResponseStatistics::new, WEEKLY_ADHERENCE_SERVICE_NAME, actorResponseDao);
  }

  private static class WeeklyResponseStatistics extends ResponseStatistics {
    @Override
    public WeeklyResponseStatistics add(ActorResponse actorResponse) {
      positiveAnswers += of(actorResponse.getTextResponse()).map(Integer::parseInt).orElse(0);
      totalAnswers += 7;
      return this;
    }
  }
}
