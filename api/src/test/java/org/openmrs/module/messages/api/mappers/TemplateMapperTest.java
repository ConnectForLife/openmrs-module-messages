package org.openmrs.module.messages.api.mappers;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.openmrs.module.messages.builder.TemplateDTOBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

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
        assertEquals(dao.getUuid(), templateDTO.getUuid());
    }

    @Test
    public void shouldMapToDaoSuccessfully() {
        Template template = templateMapper.fromDto(dto);

        assertThat(template, is(notNullValue()));
        assertEquals(dto.getId(), template.getId());
        assertEquals(dto.getTemplateFields(), template.getTemplateFields());
        assertEquals(dto.getName(), template.getName());
        assertEquals(dto.getServiceQuery(), template.getServiceQuery());
        assertEquals(dto.getServiceQueryType(), template.getServiceQueryType());
        assertEquals(dto.getUuid(), template.getUuid());
    }
}
