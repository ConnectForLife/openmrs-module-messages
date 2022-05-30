package org.openmrs.module.messages.api.mappers;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PatientTemplateMapperTest {

  private PatientTemplate dao;

  private PatientTemplateDTO dto;

  private PatientTemplateMapper patientTemplateMapper;

  private TemplateFieldValueMapper templateFieldValueMapper;

  private static final Integer ID = 1;

  private List<TemplateFieldValueDTO> templateFieldValues = new ArrayList<>();

  private static final Integer PATIENT_ID = 5;

  private static final Integer TEMPLATE_ID = 10;

  private static final Integer ACTOR_ID = 7;

  private static final Integer ACTOR_TYPE_ID = 12;

  private static final String UUID = "f08a77d6-f122-438c-aa63-e3a9230780a9";

  @Before
  public void setUp() {
    patientTemplateMapper = new PatientTemplateMapper();
    templateFieldValueMapper = new TemplateFieldValueMapper();
    patientTemplateMapper.setValueMapper(templateFieldValueMapper);
    dao = new PatientTemplateBuilder().build();
    dto = buildPatientTemplateDtoObject();
  }

  @Test
  public void shouldMapToDtoSuccessfully() {
    PatientTemplateDTO patientTemplateDTO = patientTemplateMapper.toDto(dao);

    assertThat(patientTemplateDTO, is(notNullValue()));
    assertEquals(dao.getId(), patientTemplateDTO.getId());
    assertEquals(dao.getTemplateFieldValues(), patientTemplateDTO.getTemplateFieldValues());
    assertEquals(dao.getPatient().getId(), patientTemplateDTO.getPatientId());
    assertEquals(dao.getTemplate().getId(), patientTemplateDTO.getTemplateId());
    assertEquals(dao.getActor().getId(), patientTemplateDTO.getActorId());
    assertEquals(dao.getUuid(), patientTemplateDTO.getUuid());
  }

  @Test
  public void shouldMapToDaoSuccessfully() {
    PatientTemplate patientTemplate = patientTemplateMapper.fromDto(dto);

    assertThat(patientTemplate, is(notNullValue()));
    assertEquals(dto.getId(), patientTemplate.getId());
    assertEquals(dto.getTemplateFieldValues(), patientTemplate.getTemplateFieldValues());
    assertEquals(dto.getPatientId(), patientTemplate.getPatient().getId());
    assertEquals(dto.getTemplateId(), patientTemplate.getTemplate().getId());
    assertEquals(dto.getActorId(), patientTemplate.getActor().getId());
    assertEquals(dto.getUuid(), patientTemplate.getUuid());
  }

  @Test
  public void shouldMapToTemplateFieldValueList() {
    List<TemplateFieldValue> actual =
        patientTemplateMapper.fromDtos(new PatientTemplate(), createTestTemplateFieldValueDTOs());

    assertNotNull(actual);
    assertEquals(2, actual.size());
  }

  private PatientTemplateDTO buildPatientTemplateDtoObject() {
    return new PatientTemplateDTO()
        .withId(ID)
        .withPatientId(PATIENT_ID)
        .withTemplateId(TEMPLATE_ID)
        .withTemplateFieldValues(templateFieldValues)
        .withActorId(ACTOR_ID)
        .withActorTypeId(ACTOR_TYPE_ID)
        .withUuid(UUID);
  }

  private List<TemplateFieldValueDTO> createTestTemplateFieldValueDTOs() {
    return Arrays.asList(
        new TemplateFieldValueDTO().withTemplateFieldId(1).withValue("Daily"),
        new TemplateFieldValueDTO().withTemplateFieldId(2).withValue("Weekly"));
  }
}
