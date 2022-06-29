package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dao.TemplateFieldValueDao;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.api.service.impl.TemplateFieldValueServiceImpl;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class TemplateFieldValueServiceImplTest {

  @Mock private TemplateFieldValueDao templateFieldValueDao;

  @Mock private PatientTemplateService patientTemplateService;

  @InjectMocks
  private TemplateFieldValueService templateFieldValueService = new TemplateFieldValueServiceImpl();

  private Template template;

  private PatientTemplate patientTemplate;

  private TemplateField templateField;

  private TemplateFieldValue templateFieldValue;

  @Before
  public void setUp() {
    mockStatic(Context.class);

    when(Context.getRegisteredComponent("messages.templateFieldValueDao", TemplateFieldValueDao.class))
            .thenReturn(templateFieldValueDao);

    template = createTestTemplate();
    patientTemplate = createTestPatientTemplate();
    templateField = createTestTemplateField();
    templateFieldValue = createTestTemplateFieldValue();
    patientTemplate.setTemplateFieldValues(Collections.singletonList(templateFieldValue));
  }

  @Test
  public void shouldUpdateTemplateFieldValue() {
    when(patientTemplateService.getById(100)).thenReturn(patientTemplate);

    assertEquals("Daily", templateFieldValue.getValue());
    templateFieldValueService.updateTemplateFieldValue(100, "Frequency of the message", "Weekly");
    assertEquals("Weekly", templateFieldValue.getValue());
  }

  @Test
  public void shouldGetTemplateFieldValueByPatientTemplateAndFieldType() {
    templateFieldValueService.getTemplateFieldByPatientTemplateAndFieldType(patientTemplate, "Frequency of the message");

   verify(templateFieldValueDao).findOneByCriteria(any());
  }

  private Template createTestTemplate() {
    Template template = new Template();
    template.setName("Adherence report daily");
    template.setServiceQuery("select 1");
    template.setServiceQuery("SQL");
    return template;
  }

  private PatientTemplate createTestPatientTemplate() {
    PatientTemplate patientTemplate = new PatientTemplate();
    patientTemplate.setId(100);
    patientTemplate.setTemplate(template);
    return patientTemplate;
  }

  private TemplateField createTestTemplateField() {
    TemplateField templateField = new TemplateField();
    templateField.setTemplate(template);
    templateField.setName("Frequency of the message");
    return templateField;
  }

  private TemplateFieldValue createTestTemplateFieldValue() {
    TemplateFieldValue templateFieldValue = new TemplateFieldValue();
    templateFieldValue.setTemplateField(templateField);
    templateFieldValue.setPatientTemplate(patientTemplate);
    templateFieldValue.setValue("Daily");
    return templateFieldValue;
  }
}
