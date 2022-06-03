/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

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
