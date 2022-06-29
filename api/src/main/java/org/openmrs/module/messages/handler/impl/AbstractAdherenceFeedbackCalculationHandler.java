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

import org.openmrs.Concept;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.ActorResponseDao;
import org.openmrs.module.messages.api.model.ActorResponse;
import org.openmrs.module.messages.api.model.AdherenceFeedback;
import org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.domain.criteria.LastResponseCriteria;
import org.openmrs.module.messages.handler.AdherenceFeedbackCalculationHandler;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.Integer.parseInt;
import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;
import static org.openmrs.module.messages.api.constants.ConfigConstants.BENCHMARK_PERIOD_KEY;
import static org.openmrs.module.messages.api.constants.ConfigConstants.CUT_OFF_SCORE_FOR_ADHERENCE_TREND_KEY;
import static org.openmrs.module.messages.api.constants.ConfigConstants.CUT_OFF_SCORE_FOR_HIGH_MEDIUM_ADHERENCE_LEVEL_KEY;
import static org.openmrs.module.messages.api.constants.ConfigConstants.CUT_OFF_SCORE_FOR_MEDIUM_LOW_ADHERENCE_LEVEL_KEY;
import static org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder.ADHERENCE_LEVEL_HIGH;
import static org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder.ADHERENCE_LEVEL_LOW;
import static org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder.ADHERENCE_LEVEL_MEDIUM;
import static org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder.ADHERENCE_TREND_FALLING;
import static org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder.ADHERENCE_TREND_RISING;
import static org.openmrs.module.messages.api.model.AdherenceFeedbackBuilder.ADHERENCE_TREND_STABLE;

public abstract class AbstractAdherenceFeedbackCalculationHandler
    implements AdherenceFeedbackCalculationHandler {

  private final Supplier<ResponseStatistics> responseStatisticsSupplier;
  private final String serviceName;
  private final ActorResponseDao actorResponseDao;

  AbstractAdherenceFeedbackCalculationHandler(
      Supplier<ResponseStatistics> responseStatisticsSupplier,
      String serviceName,
      ActorResponseDao actorResponseDao) {
    this.responseStatisticsSupplier = responseStatisticsSupplier;
    this.serviceName = serviceName;
    this.actorResponseDao = actorResponseDao;
  }

  @Override
  public String getServiceName() {
    return serviceName;
  }

  @Override
  public AdherenceFeedback getAdherenceFeedback(Patient patient, Person actor) {
    try {
      return calculateAdherenceFeedback(patient, actor);
    } catch (Exception e) {
      return AdherenceFeedbackBuilder.createFailed(serviceName, e.toString());
    }
  }

  protected abstract String getAdherenceFeedbackQuestionUuid();

  private AdherenceFeedback calculateAdherenceFeedback(Patient patient, Person actor) {
    final ActorIntermediateAdherence actorIntermediateAdherence =
        getActorIntermediateAdherence(patient, actor);

    return new AdherenceFeedbackBuilder()
        .withServiceName(serviceName)
        .withNumberOfDays(actorIntermediateAdherence.currentAdherence.totalAnswers)
        .withNumberOfDaysWithPositiveAnswer(
            actorIntermediateAdherence.currentAdherence.positiveAnswers)
        .withCurrentAdherence(actorIntermediateAdherence.currentAdherence.adherence)
        .withBenchmarkAdherence(actorIntermediateAdherence.benchmarkAdherence.adherence)
        .withAdherenceTrend(
            calculateTrend(
                actorIntermediateAdherence.benchmarkAdherence.adherence,
                actorIntermediateAdherence.currentAdherence.adherence))
        .withAdherenceLevel(calculateLevel(actorIntermediateAdherence.currentAdherence.adherence))
        .build();
  }

  private ActorIntermediateAdherence getActorIntermediateAdherence(Patient patient, Person actor) {
    final Concept questionConcept = getQuestionConcept();

    return new ActorIntermediateAdherence(
        getAdherenceForCurrentWeek(questionConcept, patient, actor),
        getAdherenceForBenchmark(questionConcept, patient, actor));
  }

  private Concept getQuestionConcept() {
    final String adherenceFeedbackQuestionUuid = getAdherenceFeedbackQuestionUuid();
    final Concept questionConcept =
        Context.getConceptService().getConceptByUuid(adherenceFeedbackQuestionUuid);

    if (questionConcept == null) {
      throw new IllegalStateException(
          "Could not find Adherence Question Concept, UUID: " + adherenceFeedbackQuestionUuid);
    }

    return questionConcept;
  }

  private String calculateTrend(int benchmarkAdherence, int currentAdherence) {
    final int trendScoreCutoff =
        parseInt(
            Context.getAdministrationService()
                .getGlobalProperty(CUT_OFF_SCORE_FOR_ADHERENCE_TREND_KEY));

    int scoreTrendValue = currentAdherence - benchmarkAdherence;

    if (Math.abs(scoreTrendValue) >= trendScoreCutoff) {
      return scoreTrendValue > 0 ? ADHERENCE_TREND_RISING : ADHERENCE_TREND_FALLING;
    }
    return ADHERENCE_TREND_STABLE;
  }

  private String calculateLevel(int adherence) {
    final AdministrationService administrationService = Context.getAdministrationService();
    final int lowCutoff =
        parseInt(
            administrationService.getGlobalProperty(
                CUT_OFF_SCORE_FOR_MEDIUM_LOW_ADHERENCE_LEVEL_KEY));
    final int mediumCutoff =
        parseInt(
            administrationService.getGlobalProperty(
                CUT_OFF_SCORE_FOR_HIGH_MEDIUM_ADHERENCE_LEVEL_KEY));

    if (adherence < lowCutoff) {
      return ADHERENCE_LEVEL_LOW;
    } else if (adherence < mediumCutoff) {
      return ADHERENCE_LEVEL_MEDIUM;
    } else {
      return ADHERENCE_LEVEL_HIGH;
    }
  }

  private AdherenceIntermediateResult getAdherenceForCurrentWeek(
      Concept questionConcept, Patient patient, Person actor) {
    final ZonedDateTime currentAdherenceStart = DateUtil.now().minus(1, WEEKS);

    final List<ActorResponse> currentWeekResponses =
        actorResponseDao.findAllByCriteria(
            new LastResponseCriteria()
                .setActorId(actor.getId())
                .setPatientId(patient.getPatientId())
                .setAnsweredTimeFrom(currentAdherenceStart)
                .setConceptQuestionId(questionConcept.getId()),
            null);

    return calculateAdherenceIntermediateResult(
        responseStatisticsSupplier.get(), currentWeekResponses);
  }

  private AdherenceIntermediateResult getAdherenceForBenchmark(
      Concept questionConcept, Patient patient, Person actor) {
    final int benchmarkPeriod =
        parseInt(Context.getAdministrationService().getGlobalProperty(BENCHMARK_PERIOD_KEY));
    final ZonedDateTime benchmarkPeriodEnd = DateUtil.now().minus(1, WEEKS);
    final ZonedDateTime benchmarkPeriodStart = benchmarkPeriodEnd.minus(benchmarkPeriod, DAYS);

    final List<ActorResponse> benchmarkResponses =
        actorResponseDao.findAllByCriteria(
            new LastResponseCriteria()
                .setActorId(actor.getId())
                .setPatientId(patient.getPatientId())
                .setAnsweredTimeFrom(benchmarkPeriodStart)
                .setAnsweredTimeTo(benchmarkPeriodEnd)
                .setConceptQuestionId(questionConcept.getId()),
            null);

    return calculateAdherenceIntermediateResult(
        responseStatisticsSupplier.get(), benchmarkResponses);
  }

  private AdherenceIntermediateResult calculateAdherenceIntermediateResult(
      ResponseStatistics statisticsIdentity, List<ActorResponse> actorResponses) {
    final ResponseStatistics responseStatistics =
        actorResponses.stream()
            .reduce(statisticsIdentity, ResponseStatistics::add, ResponseStatistics::sum);

    return new AdherenceIntermediateResult(
        responseStatistics.totalAnswers != 0
            ? Math.round(
                responseStatistics.positiveAnswers * 100.0f / responseStatistics.totalAnswers)
            : 0,
        responseStatistics.positiveAnswers,
        responseStatistics.totalAnswers);
  }

  static class AdherenceIntermediateResult {
    private final int adherence;
    private final int positiveAnswers;
    private final int totalAnswers;

    AdherenceIntermediateResult(int adherence, int positiveAnswers, int totalAnswers) {
      this.adherence = adherence;
      this.positiveAnswers = positiveAnswers;
      this.totalAnswers = totalAnswers;
    }
  }

  static class ActorIntermediateAdherence {
    final AdherenceIntermediateResult currentAdherence;
    final AdherenceIntermediateResult benchmarkAdherence;

    ActorIntermediateAdherence(
        AdherenceIntermediateResult currentAdherence,
        AdherenceIntermediateResult benchmarkAdherence) {
      this.currentAdherence = currentAdherence;
      this.benchmarkAdherence = benchmarkAdherence;
    }
  }

  abstract static class ResponseStatistics {
    int positiveAnswers = 0;
    int totalAnswers = 0;

    abstract ResponseStatistics add(ActorResponse actorResponse);

    ResponseStatistics sum(ResponseStatistics other) {
      this.positiveAnswers += other.positiveAnswers;
      this.totalAnswers += other.totalAnswers;
      return this;
    }
  }
}
