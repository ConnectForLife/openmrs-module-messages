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

public class TemplateFieldMapperTest {

    private TemplateField dao;

    private TemplateFieldDTO dto;

    private TemplateFieldMapper templateFieldMapper;

    @Before
    public void setUp() {
        templateFieldMapper = new TemplateFieldMapper();
        dao = new TemplateFieldBuilder().build();
        dto = new TemplateFieldDTOBuilder().build();
    }

    @Test
    public void toDto() {
        TemplateFieldDTO actual = templateFieldMapper.toDto(dao);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is(dao.getId()));
        assertThat(actual.getName(), is(dao.getName()));
        assertThat(actual.getMandatory(), is(dao.getMandatory()));
        assertThat(actual.getDefaultValue(), is(dao.getDefaultValue()));
        assertThat(actual.getType(), is(dao.getTemplateFieldType().name()));
        assertThat(actual.getUuid(), is(dao.getUuid()));
    }

    @Test
    public void fromDto() {
        TemplateField actual = templateFieldMapper.fromDto(dto);
        assertThat(actual, is(notNullValue()));
        assertThat(actual.getId(), is(dto.getId()));
        assertThat(actual.getName(), is(dto.getName()));
        assertThat(actual.getMandatory(), is(dto.getMandatory()));
        assertThat(actual.getDefaultValue(), is(dto.getDefaultValue()));
        assertThat(actual.getTemplateFieldType().name(), is(dto.getType()));
        assertThat(actual.getUuid(), is(dto.getUuid()));
    }
}
