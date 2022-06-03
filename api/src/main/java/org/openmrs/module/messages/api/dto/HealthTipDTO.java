/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.dto;

/**
 * HealthTipDTO class that contains information about health tip id, related health tip text,
 * category and category id.
 */
public class HealthTipDTO {

  private static final Integer HT_CATEGORY_ID_RESULT_INDEX = 0;

  private static final Integer HT_CATEGORY_NAME_RESULT_INDEX = 1;

  private static final Integer HT_ID_RESULT_INDEX = 2;

  private static final Integer HT_TEXT_RESULT_INDEX = 3;

  private Integer healthTipCategoryId;

  private String healthTipCategoryName;

  private Integer healthTipId;

  private String healthTipText;

  public HealthTipDTO() {
  }

  public HealthTipDTO(Object[] results) {
    this.healthTipCategoryId = (Integer) results[HT_CATEGORY_ID_RESULT_INDEX];
    this.healthTipCategoryName = (String) results[HT_CATEGORY_NAME_RESULT_INDEX];
    this.healthTipId = (Integer) results[HT_ID_RESULT_INDEX];
    this.healthTipText = (String) results[HT_TEXT_RESULT_INDEX];
  }

  public Integer getHealthTipCategoryId() {
    return healthTipCategoryId;
  }

  public void setHealthTipCategoryId(Integer healthTipCategoryId) {
    this.healthTipCategoryId = healthTipCategoryId;
  }

  public String getHealthTipCategoryName() {
    return healthTipCategoryName;
  }

  public void setHealthTipCategoryName(String healthTipCategoryName) {
    this.healthTipCategoryName = healthTipCategoryName;
  }

  public Integer getHealthTipId() {
    return healthTipId;
  }

  public void setHealthTipId(Integer healthTipId) {
    this.healthTipId = healthTipId;
  }

  public String getHealthTipText() {
    return healthTipText;
  }

  public void setHealthTipText(String healthTipText) {
    this.healthTipText = healthTipText;
  }
}
