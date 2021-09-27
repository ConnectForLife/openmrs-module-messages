package org.openmrs.module.messages.api.mappers;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.RelationshipType;
import org.openmrs.module.messages.api.dto.TemplateFieldDefaultValueDTO;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldDefaultValue;
import org.openmrs.module.messages.builder.RelationshipTypeBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class TemplateFieldDefaultValueMapperTest {

    private static final Integer ID = 11;

    private static final String DEFAULT_VALUE = "Deactivated";

    private TemplateFieldDefaultValue dao;

    private TemplateFieldDefaultValueDTO dto;

    private TemplateFieldDefaultValueMapper templateFieldDefaultValueMapper;

    private RelationshipType relationshipType;

    private TemplateField templateField;

    @Before
    public void setUp() {
        templateFieldDefaultValueMapper = new TemplateFieldDefaultValueMapper();
        relationshipType = new RelationshipTypeBuilder().build();
        templateField = new TemplateFieldBuilder().build();
        dao = buildTemplateFieldDefaultValueDaoObject();
        dto = buildTemplateFieldDefaultValueDtoObject();
    }

    @Test
    public void shouldMapToDtoSuccessfully() {
        TemplateFieldDefaultValueDTO templateFieldDefaultValueDTO = templateFieldDefaultValueMapper.toDto(dao);

        assertThat(templateFieldDefaultValueDTO, is(notNullValue()));
        assertEquals(dao.getId(), templateFieldDefaultValueDTO.getId());
        assertEquals(dao.getDefaultValue(), templateFieldDefaultValueDTO.getDefaultValue());
        assertEquals(dao.getDirection(), templateFieldDefaultValueDTO.getDirection());
        assertEquals(dao.getRelationshipType().getRelationshipTypeId(),
                templateFieldDefaultValueDTO.getRelationshipTypeId());
        assertEquals(dao.getTemplateField().getId(), templateFieldDefaultValueDTO.getTemplateFieldId());
    }

    @Test
    public void shouldMapToDaoSuccessfully() {
        TemplateFieldDefaultValue templateFieldDefaultValue = templateFieldDefaultValueMapper.fromDto(dto);

        assertThat(templateFieldDefaultValue, is(notNullValue()));
        assertEquals(dto.getId(), templateFieldDefaultValue.getId());
        assertEquals(dto.getDefaultValue(), templateFieldDefaultValue.getDefaultValue());
        assertEquals(dto.getDirection(), templateFieldDefaultValue.getDirection());
        assertEquals(dto.getRelationshipTypeId(), templateFieldDefaultValue.getRelationshipType().getId());
        assertEquals(dto.getTemplateFieldId(), templateFieldDefaultValue.getTemplateField().getId());
    }

    private TemplateFieldDefaultValue buildTemplateFieldDefaultValueDaoObject() {
        dao = new TemplateFieldDefaultValue();
        dao.setId(ID);
        dao.setDefaultValue(DEFAULT_VALUE);
        dao.setDirection(RelationshipTypeDirection.B);
        dao.setRelationshipType(relationshipType);
        dao.setTemplateField(templateField);
        return dao;
    }

    private TemplateFieldDefaultValueDTO buildTemplateFieldDefaultValueDtoObject() {
        TemplateFieldDefaultValueDTO templateFieldDefaultValueDTO = new TemplateFieldDefaultValueDTO()
                .setId(ID)
                .setDefaultValue(DEFAULT_VALUE)
                .setDirection(RelationshipTypeDirection.B)
                .setRelationshipTypeId(relationshipType.getId())
                .setTemplateFieldId(templateField.getId());
        return templateFieldDefaultValueDTO;
    }
}
