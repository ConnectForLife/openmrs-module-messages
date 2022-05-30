package org.openmrs.module.messages.api.dto;

/**
 * HealthTipDTO class that contains information about health tip id, related health tip text,
 * category and category id.
 */
public class HealthTipDTO {

  private Integer healthTipCategoryId;

  private String healthTipCategoryName;

  private Integer healthTipId;

  private String healthTipText;

  public HealthTipDTO() {
  }

  public HealthTipDTO(Object[] results) {
    this.healthTipCategoryId = (Integer) results[0];
    this.healthTipCategoryName = (String) results[1];
    this.healthTipId = (Integer) results[2];
    this.healthTipText = (String) results[3];
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
