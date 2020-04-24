package org.openmrs.module.messages.api.mappers;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldDTOBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class TemplateFieldMapperTest {

    private TemplateField dao;

    private TemplateFieldDTO dto;

    private TemplateFieldMapper templateFieldMapper;

    private TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper;

    @Before
    public void setUp() {
        templateFieldMapper = new TemplateFieldMapper();
        templateFieldDefaultValueMapper = new TemplateFieldDefaultValueMapper();
        templateFieldMapper.setTemplateFieldDefaultValueMapper(templateFieldDefaultValueMapper);
        dao = new TemplateFieldBuilder().build();
        dto = new TemplateFieldDTOBuilder().build();
    }

    @Test
    public void shouldMapToDtoSuccessfully() {
        TemplateFieldDTO actual = templateFieldMapper.toDto(dao);
        assertThat(actual, is(notNullValue()));
        assertEquals(dao.getId(), actual.getId());
        assertEquals(dao.getName(), actual.getName());
        assertEquals(dao.getMandatory(), actual.getMandatory());
        assertEquals(dao.getDefaultValue(), actual.getDefaultValue());
        assertEquals(dao.getTemplateFieldType().name(), actual.getType());
        assertEquals(dao.getUuid(), actual.getUuid());
    }

    @Test
    public void shouldMapToDaoSuccessfully() {
        TemplateField actual = templateFieldMapper.fromDto(dto);
        assertThat(actual, is(notNullValue()));
        assertEquals(dto.getId(), actual.getId());
        assertEquals(dto.getName(), actual.getName());
        assertEquals(dto.getMandatory(), actual.getMandatory());
        assertEquals(dto.getDefaultValue(), actual.getDefaultValue());
        assertEquals(dto.getType(), actual.getTemplateFieldType().name());
        assertEquals(dto.getUuid(), actual.getUuid());
    }
}
