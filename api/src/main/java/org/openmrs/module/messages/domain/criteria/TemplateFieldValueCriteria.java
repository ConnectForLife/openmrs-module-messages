/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.domain.criteria;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.module.messages.api.model.PatientTemplate;

import java.io.Serializable;

public class TemplateFieldValueCriteria extends BaseOpenmrsDataCriteria implements Serializable {

  private static final long serialVersionUID = 1L;

  private transient PatientTemplate patientTemplate;

  private String fieldTypeName;

  @Override
  public void loadHibernateCriteria(Criteria hibernateCriteria) {
    if (patientTemplate != null) {
      hibernateCriteria.add(Restrictions.eq("patientTemplate", patientTemplate));
    }

    if (fieldTypeName != null) {
      hibernateCriteria
          .createAlias("templateField", "templateField")
          .add(Restrictions.eq("templateField.name", fieldTypeName));
    }
  }

  public TemplateFieldValueCriteria setPatientTemplate(PatientTemplate patientTemplate) {
    this.patientTemplate = patientTemplate;
    return this;
  }

  public TemplateFieldValueCriteria setFieldTypeName(String fieldTypeName) {
    this.fieldTypeName = fieldTypeName;
    return this;
  }
}
