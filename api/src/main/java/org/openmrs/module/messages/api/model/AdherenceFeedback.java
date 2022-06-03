/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.model;

/**
 * The interface of an data structure which contains the calculated Adherence Feedback information.
 *
 * @implNote The interface and implementation must be designed for usage in Velocity Template
 *     scripts.
 * @see org.openmrs.module.messages.api.service.AdherenceFeedbackService
 */
public interface AdherenceFeedback {
  /**
   * @return the name of service which provided the data used to calculate the Adherence Feedback.
   */
  String getServiceName();

  /** @return the current adherence in percents. */
  int getCurrentAdherence();

  /** @return the benchmark adherence in percents. */
  int getBenchmarkAdherence();

  /** @return the number of days that there was a data provided. */
  int getNumberOfDays();

  /**
   * @return the number of days for which a positive answer ware provided. Positive answer usually
   *     indicates that medication was taken.*
   */
  int getNumberOfDaysWithPositiveAnswer();

  /**
   * @implSpec the result should be limited to: 'falling', 'rising' and 'stable'
   * @return the calculated Adherence Trend
   */
  String getAdherenceTrend();

  /**
   * @implSpec the result should be limited to: 'low', 'medium' and 'high'
   * @return the calculated Adherence Level
   */
  String getAdherenceLevel();

  /** @return true if the calculation failed, false otherwise */
  boolean isCalculationFailed();

  /** @return an error message only if isCalculationFailed is true, null otherwise */
  String getErrorText();
}
