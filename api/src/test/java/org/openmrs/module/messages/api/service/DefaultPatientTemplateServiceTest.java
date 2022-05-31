package org.openmrs.module.messages.api.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openmrs.Concept;
import org.openmrs.ConceptClass;
import org.openmrs.Patient;
import org.openmrs.api.ConceptService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.exception.ValidationException;
import org.openmrs.module.messages.api.mappers.MessageDetailsMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.impl.DefaultPatientTemplateServiceImpl;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class})
public class DefaultPatientTemplateServiceTest {

  private final Patient patient = new Patient(100);

  @Mock private ConceptService conceptService;

  @Mock private PatientTemplateService patientTemplateService;

  @Mock private MessageDetailsMapper messageDetailsMapper;

  @Mock private TemplateService templateService;

  @Mock private ActorService actorService;

  @InjectMocks
  private DefaultPatientTemplateService defaultPatientTemplateService =
      new DefaultPatientTemplateServiceImpl();

  @Before
  public void setUp() {
    mockStatic(Context.class);

    when(Context.getConceptService()).thenReturn(conceptService);
  }

  @Test(expected = ValidationException.class)
  public void shouldThrowValidationExceptionPatientTemplateListIsEmpty() {
    when(patientTemplateService.findAllByCriteria(any(PatientTemplateCriteria.class)))
        .thenReturn(Collections.emptyList());

    defaultPatientTemplateService.generateDefaultPatientTemplates(patient);

    verify(patientTemplateService).findAllByCriteria(any(PatientTemplateCriteria.class));
  }

  @Test
  public void shouldGetDetailsForRealAndDefault() {
    when(patientTemplateService.findAllByCriteria(any(PatientTemplateCriteria.class)))
        .thenReturn(Collections.emptyList());
    when(messageDetailsMapper.toDto(anyListOf(PatientTemplate.class)))
        .thenReturn(new MessageDetailsDTO());

    MessageDetailsDTO actual = defaultPatientTemplateService.getDetailsForRealAndDefault(patient);

    assertNotNull(actual);
    verify(patientTemplateService, times(2)).findAllByCriteria(any(PatientTemplateCriteria.class));
    verify(messageDetailsMapper).toDto(anyListOf(PatientTemplate.class));
  }

  @Test
  public void shouldGetHealthTipCategoryConcepts() {
    ConceptClass testConceptClass = new ConceptClass();
    when(conceptService.getConceptClassByName("Health Tips Category")).thenReturn(testConceptClass);
    when(conceptService.getConceptsByClass(testConceptClass)).thenReturn(Collections.emptyList());

    List<Concept> actual = defaultPatientTemplateService.getHealthTipCategoryConcepts();

    assertNotNull(actual);
    assertEquals(0, actual.size());
    verify(conceptService).getConceptClassByName("Health Tips Category");
    verify(conceptService).getConceptsByClass(testConceptClass);
  }
}
