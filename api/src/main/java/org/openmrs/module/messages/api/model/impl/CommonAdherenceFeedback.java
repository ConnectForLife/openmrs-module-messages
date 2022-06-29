/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model.impl;

import org.openmrs.module.messages.api.model.AdherenceFeedback;

public class CommonAdherenceFeedback implements AdherenceFeedback {
  private final String serviceName;
  private final int currentAdherence;
  private final int benchmarkAdherence;
  private final int numberOfDays;
  private final int numberOfDaysWithPositiveAnswer;
  private final String adherenceTrend;
  private final String adherenceLevel;
  private final boolean calculationFailed;
  private final String errorText;

  public CommonAdherenceFeedback(
      String serviceName,
      int currentAdherence,
      int benchmarkAdherence,
      int numberOfDays,
      int numberOfDaysWithPositiveAnswer,
      String adherenceTrend,
      String adherenceLevel,
      boolean calculationFailed,
      String errorText) {
    this.serviceName = serviceName;
    this.currentAdherence = currentAdherence;
    this.benchmarkAdherence = benchmarkAdherence;
    this.numberOfDays = numberOfDays;
    this.numberOfDaysWithPositiveAnswer = numberOfDaysWithPositiveAnswer;
    this.adherenceTrend = adherenceTrend;
    this.adherenceLevel = adherenceLevel;
    this.calculationFailed = calculationFailed;
    this.errorText = errorText;
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
  public int getBenchmarkAdherence() {
    return benchmarkAdherence;
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

  @Override
  public boolean isCalculationFailed() {
    return calculationFailed;
  }

  @Override
  public String getErrorText() {
    return errorText;
  }
}
