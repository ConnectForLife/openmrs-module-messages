package org.openmrs.module.messages.api.model;

import org.openmrs.module.messages.api.model.impl.CommonAdherenceFeedback;

public class AdherenceFeedbackBuilder {
  public static final String ADHERENCE_TREND_FALLING = "falling";
  public static final String ADHERENCE_TREND_RISING = "rising";
  public static final String ADHERENCE_TREND_STABLE = "stable";

  public static final String ADHERENCE_LEVEL_LOW = "low";
  public static final String ADHERENCE_LEVEL_MEDIUM = "medium";
  public static final String ADHERENCE_LEVEL_HIGH = "high";

  private String serviceName;
  private int currentAdherence;
  private int benchmarkAdherence;
  private int numberOfDays;
  private int numberOfDaysWithPositiveAnswer;
  private String adherenceTrend;
  private String adherenceLevel;

  public static AdherenceFeedback createFailed(String serviceName, String errorText) {
    return new CommonAdherenceFeedback(serviceName, 0, 0, 0, 0, "", "", true, errorText);
  }

  public AdherenceFeedback build() {
    return new CommonAdherenceFeedback(
        serviceName,
        currentAdherence,
        benchmarkAdherence,
        numberOfDays,
        numberOfDaysWithPositiveAnswer,
        adherenceTrend,
        adherenceLevel,
        false,
        null);
  }

  public AdherenceFeedbackBuilder withServiceName(String serviceName) {
    this.serviceName = serviceName;
    return this;
  }

  public AdherenceFeedbackBuilder withCurrentAdherence(int currentAdherence) {
    this.currentAdherence = currentAdherence;
    return this;
  }

  public AdherenceFeedbackBuilder withBenchmarkAdherence(int benchmarkAdherence) {
    this.benchmarkAdherence = benchmarkAdherence;
    return this;
  }

  public AdherenceFeedbackBuilder withNumberOfDays(int numberOfDays) {
    this.numberOfDays = numberOfDays;
    return this;
  }

  public AdherenceFeedbackBuilder withNumberOfDaysWithPositiveAnswer(
      int numberOfDaysWithPositiveAnswer) {
    this.numberOfDaysWithPositiveAnswer = numberOfDaysWithPositiveAnswer;
    return this;
  }

  public AdherenceFeedbackBuilder withAdherenceTrend(String adherenceTrend) {
    this.adherenceTrend = adherenceTrend;
    return this;
  }

  public AdherenceFeedbackBuilder withAdherenceLevel(String adherenceLevel) {
    this.adherenceLevel = adherenceLevel;
    return this;
  }
}
