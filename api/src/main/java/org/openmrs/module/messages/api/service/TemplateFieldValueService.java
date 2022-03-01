package org.openmrs.module.messages.api.service;

import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldValue;

/** Provides methods related to template field value stuff */
public interface TemplateFieldValueService {

  /**
   * Updates field value in particular patient template
   *
   * @param patientTemplateId id of patient template
   * @param fieldName name of the field whose value is to be updated
   * @param newValue new field value
   */
  void updateTemplateFieldValue(Integer patientTemplateId, String fieldName, String newValue);

  /**
   * Gets template field value by a patient template and field type
   *
   * @param patientTemplate patient template from which template field value is taken
   * @param fieldTypeName field type from which template field value is taken
   * @return template field value object
   */
  TemplateFieldValue getTemplateFieldByPatientTemplateAndFieldType(
      PatientTemplate patientTemplate, String fieldTypeName);
}
