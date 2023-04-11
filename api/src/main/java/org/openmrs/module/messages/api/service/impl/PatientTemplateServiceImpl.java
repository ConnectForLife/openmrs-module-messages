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

import org.openmrs.Patient;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.dao.TemplateFieldDao;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.api.util.DateUtil;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.domain.criteria.TemplateCriteria;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * Implements methods related to creating, reading, updating and deleting patient templates entities
 */
public class PatientTemplateServiceImpl extends BaseOpenmrsDataService<PatientTemplate> implements PatientTemplateService {

  private static final String TEMPLATE_DAO_BEAN_NAME = "messages.TemplateDao";
  private static final String TEMPLATE_FIELD_DAO_BEAN_NAME = "messages.TemplateFieldDao";
  private static final String VISIT_REMINDER_TEMPLATE_UUID = "95573fe3-20b2-11ea-ac12-0242c0a82002";
  private static final String VISIT_REMINDER_CHANNEL_UUID = "95574976-20b2-11ea-ac12-0242c0a82002";
  private static final String VISIT_REMINDER_DATE_STARTED_UUID = "95575327-20b2-11ea-ac12-0242c0a82002";
  private static final String VISIT_REMINDER_DATE_END_UUID = "95575cbd-20b2-11ea-ac12-0242c0a82002";

  private PatientTemplateMapper patientTemplateMapper;
  private TemplateService templateService;

  @Override
  @Transactional
  public List<PatientTemplate> batchSave(List<PatientTemplateDTO> newDtos, int patientId) throws APIException {
    List<PatientTemplate> existingPt = findAllByCriteria(PatientTemplateCriteria.forPatientId(patientId));
    patientTemplateMapper.updateFromDtos(newDtos, existingPt);
    return saveOrUpdate(existingPt);
  }

  @Override
  public void voidForPerson(int personId, String reason) throws APIException {
    List<PatientTemplate> templatesForActor = findAllByCriteria(PatientTemplateCriteria.forActorId(personId));
    //Required to use AOP during voiding
    PatientTemplateService patientTemplateService = Context.getService(PatientTemplateService.class);
    for (PatientTemplate template : templatesForActor) {
      patientTemplateService.voidPatientTemplate(template, reason);
    }
    List<PatientTemplate> templatesForPatient = findAllByCriteria(PatientTemplateCriteria.forPatientId(personId));
    for (PatientTemplate template : templatesForPatient) {
      patientTemplateService.voidPatientTemplate(template, reason);
    }
  }

  @Override
  public void voidForRelationship(int relationshipId, String reason) {
    List<PatientTemplate> templates = findAllByCriteria(PatientTemplateCriteria.forActorType(relationshipId));
    //Required to use AOP during voiding
    PatientTemplateService patientTemplateService = Context.getService(PatientTemplateService.class);
    for (PatientTemplate template : templates) {
      patientTemplateService.voidPatientTemplate(template, reason);
    }
  }

  @Override
  public PatientTemplate voidPatientTemplate(PatientTemplate patientTemplate, String reason) throws APIException {
    if (patientTemplate == null) {
      return null;
    }
    //call the DAO layer directly to avoid any further AOP around save*
    return getDao().saveOrUpdate(patientTemplate);
  }

  @Override
  @Transactional
  public PatientTemplate createVisitReminder(String channel, String patientUuid) {
    final Patient patient = Context.getPatientService().getPatientByUuid(patientUuid);
    final Template savedTemplate =
        Context.getRegisteredComponent(TEMPLATE_DAO_BEAN_NAME, TemplateDao.class).getByUuid(VISIT_REMINDER_TEMPLATE_UUID);

    final PatientTemplate existingVisitReminder = getDao().findOneByCriteria(
        PatientTemplateCriteria.forPatientAndActorAndTemplate(patient.getId(), patient.getId(), savedTemplate.getId()));

    if (existingVisitReminder != null) {
      return existingVisitReminder;
    }

    PatientTemplate visitReminder = new PatientTemplate();

    TemplateFieldDao templateFieldDao = Context.getRegisteredComponent(TEMPLATE_FIELD_DAO_BEAN_NAME, TemplateFieldDao.class);
    TemplateField channelType = templateFieldDao.getByUuid(VISIT_REMINDER_CHANNEL_UUID);
    TemplateField dateStart = templateFieldDao.getByUuid(VISIT_REMINDER_DATE_STARTED_UUID);
    TemplateField dateEnd = templateFieldDao.getByUuid(VISIT_REMINDER_DATE_END_UUID);

    TemplateFieldValue channelTypeValue = new TemplateFieldValue();
    channelTypeValue.setTemplateField(channelType);
    channelTypeValue.setValue(channel);
    channelTypeValue.setPatientTemplate(visitReminder);

    TemplateFieldValue dateStartValue = new TemplateFieldValue();
    dateStartValue.setTemplateField(dateStart);
    dateStartValue.setValue(DateUtil.formatToServerSideDate(DateUtil.now()));
    dateStartValue.setPatientTemplate(visitReminder);

    TemplateFieldValue dateEndValue = new TemplateFieldValue();
    dateEndValue.setTemplateField(dateEnd);
    dateEndValue.setValue("NO_DATE|EMPTY");
    dateEndValue.setPatientTemplate(visitReminder);

    visitReminder.setActor(patient.getPerson());
    visitReminder.setPatient(patient);
    visitReminder.setTemplate(savedTemplate);
    visitReminder.setTemplateFieldValues(Arrays.asList(channelTypeValue, dateStartValue, dateEndValue));
    return getDao().saveOrUpdate(visitReminder);
  }

  @Override
  @Transactional
  public PatientTemplate getOrCreatePatientTemplate(Patient patient, String templateName) throws APIException {
    final Template template = getTemplateByName(templateName);

    final PatientTemplate existingTemplate = findOneByCriteria(
        PatientTemplateCriteria.forPatientAndActorAndTemplate(patient.getId(), patient.getId(), template.getId()));

    final PatientTemplate patientTemplate;

    if (existingTemplate == null) {
      final PatientTemplate newPatientTemplate = new PatientTemplateBuilder(template, patient).build();
      patientTemplate = saveOrUpdate(newPatientTemplate);
    } else {
      patientTemplate = existingTemplate;
    }

    return patientTemplate;
  }

  public void setPatientTemplateMapper(PatientTemplateMapper patientTemplateMapper) {
    this.patientTemplateMapper = patientTemplateMapper;
  }

  public void setTemplateService(TemplateService templateService) {
    this.templateService = templateService;
  }

  private Template getTemplateByName(final String templateName) {
    final Template template = templateService.findOneByCriteria(TemplateCriteria.forName(templateName));

    if (template == null) {
      throw new APIException("Could not find Template with name: " + templateName);
    }

    return template;
  }
}
