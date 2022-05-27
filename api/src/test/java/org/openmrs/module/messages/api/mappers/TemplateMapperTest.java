package org.openmrs.module.messages.api.mappers;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.openmrs.module.messages.builder.TemplateDTOBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldDTOBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TemplateMapperTest {

    private Template dao;

    private TemplateDTO dto;

    private TemplateMapper templateMapper;

    private TemplateFieldMapper templateFieldMapper;

    private TemplateFieldDefaultValueMapper fieldDefaultValueMapper;

    private List<TemplateFieldDTO> templateFields;

    @Before
    public void setUp() {
        templateMapper = new TemplateMapper();
        fieldDefaultValueMapper = new TemplateFieldDefaultValueMapper();
        templateFieldMapper = new TemplateFieldMapper();
        templateFieldMapper.setTemplateFieldDefaultValueMapper(fieldDefaultValueMapper);
        templateMapper.setTemplateFieldMapper(templateFieldMapper);
        templateFields = new ArrayList<>();
        templateFields.add(new TemplateFieldDTOBuilder().withName("FieldA").build());
        dao = new TemplateBuilder().build();
        dto = new TemplateDTOBuilder().build();
        dto.setTemplateFields(templateFields);
    }

    @Test
    public void shouldMapToDtoSuccessfully() {
        TemplateDTO templateDTO = templateMapper.toDto(dao);

        assertThat(templateDTO, is(notNullValue()));
        assertEquals(dao.getId(), templateDTO.getId());
        assertEquals(templateFieldMapper.toDtos(dao.getTemplateFields()), templateDTO.getTemplateFields());
        assertEquals(dao.getName(), templateDTO.getName());
        assertEquals(dao.getServiceQuery(), templateDTO.getServiceQuery());
        assertEquals(dao.getServiceQueryType(), templateDTO.getServiceQueryType());
        assertEquals(dao.getDateCreated(), templateDTO.getCreatedAt());
        assertEquals(dao.getUuid(), templateDTO.getUuid());
    }

    @Test
    public void shouldMapToDaoSuccessfully() {
        final Template template = templateMapper.fromDto(dto);

        assertThat(template, is(notNullValue()));
        assertEquals(dto.getId(), template.getId());
        assertEquals(dto.getName(), template.getName());
        assertEquals(dto.getServiceQuery(), template.getServiceQuery());
        assertEquals(dto.getServiceQueryType(), template.getServiceQueryType());
        assertEquals(dto.getUuid(), template.getUuid());

        final TemplateFieldDTO dtoField = dto.getTemplateFields().get(0);
        final TemplateField expectedField = new TemplateFieldBuilder()
                .withName(dto.getTemplateFields().get(0).getName())
                .withTemplate(template).build();
        assertEquals(dtoField.getName(), expectedField.getName());
        assertEquals(template, expectedField.getTemplate());
    }

    @Test
    public void shouldSafelyDeleteTemplate() {
        final Template template = templateMapper.fromDto(dto);

        assertFalse(template.getRetired());
        templateMapper.doSafeDelete(template);

        assertTrue(template.getRetired());
    }
}
