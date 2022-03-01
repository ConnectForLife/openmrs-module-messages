package org.openmrs.module.messages.api.model.impl;

import org.openmrs.module.messages.api.model.AdherenceFeedback;

public class CommonAdherenceFeedback implements AdherenceFeedback {
  private final String serviceName;
  private final int currentAdherence;
  private final int numberOfDays;
  private final int numberOfDaysWithPositiveAnswer;
  private final String adherenceTrend;
  private final String adherenceLevel;

  public CommonAdherenceFeedback(
      String serviceName,
      int currentAdherence,
      int numberOfDays,
      int numberOfDaysWithPositiveAnswer,
      String adherenceTrend,
      String adherenceLevel) {
    this.serviceName = serviceName;
    this.currentAdherence = currentAdherence;
    this.numberOfDays = numberOfDays;
    this.numberOfDaysWithPositiveAnswer = numberOfDaysWithPositiveAnswer;
    this.adherenceTrend = adherenceTrend;
    this.adherenceLevel = adherenceLevel;
  }

  @Override
  public String getServiceName() {
    return serviceName;
  }

  @Override
  public int getCurrentAdherence() {
    return currentAdherence;
  }

  @Override
  public int getNumberOfDays() {
    return numberOfDays;
  }

  @Override
  public int getNumberOfDaysWithPositiveAnswer() {
    return numberOfDaysWithPositiveAnswer;
  }

  @Override
  public String getAdherenceTrend() {
    return adherenceTrend;
  }

  @Override
  public String getAdherenceLevel() {
    return adherenceLevel;
  }
}
