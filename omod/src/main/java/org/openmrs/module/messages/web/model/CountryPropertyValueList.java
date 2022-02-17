package org.openmrs.module.messages.web.model;

import org.openmrs.module.messages.api.dto.CountryPropertyValueDTO;

import java.util.List;

public class CountryPropertyValueList {
  private List<CountryPropertyValueDTO> values;

  public List<CountryPropertyValueDTO> getValues() {
    return values;
  }

  public void setValues(List<CountryPropertyValueDTO> values) {
    this.values = values;
  }
}
