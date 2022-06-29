/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.api.service.impl;

import org.apache.commons.lang.StringUtils;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.TemplateFieldValueDao;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.TemplateFieldValueService;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.TemplateFieldValueCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public class TemplateFieldValueServiceImpl implements TemplateFieldValueService {

  private static final String TEMPLATE_FIELD_VALUE_DAO_BEAN_NAME = "messages.templateFieldValueDao";

  private PatientTemplateService patientTemplateService;

  private TemplateFieldValueDao templateFieldValueDao;

  @Override
  public void updateTemplateFieldValue(
      Integer patientTemplateId, String fieldName, String newValue) {
    PatientTemplate patientTemplate = patientTemplateService.getById(patientTemplateId);
    List<TemplateFieldValue> fieldValues = patientTemplate.getTemplateFieldValues();
    TemplateFieldValue fieldValueToUpdate = getFieldValueToUpdate(fieldValues, fieldName);
    if (fieldValueToUpdate != null) {
      fieldValueToUpdate.setValue(newValue);
      getTemplateFieldValueDao().saveOrUpdate(fieldValueToUpdate);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public TemplateFieldValue getTemplateFieldByPatientTemplateAndFieldType(
      PatientTemplate patientTemplate, String fieldTypeName) {
    TemplateFieldValueCriteria criteria =
        new TemplateFieldValueCriteria()
            .setPatientTemplate(patientTemplate)
            .setFieldTypeName(fieldTypeName);
    return getTemplateFieldValueDao().findOneByCriteria(criteria);
  }

  public void setPatientTemplateService(PatientTemplateService patientTemplateService) {
    this.patientTemplateService = patientTemplateService;
  }

  public void setTemplateFieldValueDao(TemplateFieldValueDao templateFieldValueDao) {
    this.templateFieldValueDao = templateFieldValueDao;
  }

  private TemplateFieldValue getFieldValueToUpdate(
      List<TemplateFieldValue> fieldValues, String fieldName) {
    TemplateFieldValue fieldValueToUpdate = null;
    for (TemplateFieldValue tfv : fieldValues) {
      if (StringUtils.equalsIgnoreCase(tfv.getTemplateField().getName(), fieldName)) {
        fieldValueToUpdate = tfv;
        break;
      }
    }
    return fieldValueToUpdate;
  }

  private TemplateFieldValueDao getTemplateFieldValueDao() {
    if (templateFieldValueDao == null) {
      templateFieldValueDao =
          Context.getRegisteredComponent(
              TEMPLATE_FIELD_VALUE_DAO_BEAN_NAME, TemplateFieldValueDao.class);
    }
    return templateFieldValueDao;
  }
}
