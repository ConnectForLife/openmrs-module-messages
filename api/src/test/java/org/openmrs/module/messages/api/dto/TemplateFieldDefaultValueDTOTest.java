package org.openmrs.module.messages.api.dto;

import org.junit.Test;
import org.openmrs.module.messages.api.model.RelationshipTypeDirection;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TemplateFieldDefaultValueDTOTest {

    private static final Integer ID = 1;

    private static final Integer RELATIONSHIP_TYPE_ID = 3;

    private static final Integer TEMPLATE_FIELD_ID = 22;

    private static final RelationshipTypeDirection DIRECTION = RelationshipTypeDirection.A;

    private static final String DEFAULT_VALUE = "Weekly";

    private TemplateFieldDefaultValueDTO templateFieldDefaultValueDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        templateFieldDefaultValueDTO = buildTemplateFieldDefaultValueDTOObject();

        assertThat(templateFieldDefaultValueDTO, is(notNullValue()));
        assertEquals(ID, templateFieldDefaultValueDTO.getId());
        assertEquals(RELATIONSHIP_TYPE_ID, templateFieldDefaultValueDTO.getRelationshipTypeId());
        assertEquals(TEMPLATE_FIELD_ID, templateFieldDefaultValueDTO.getTemplateFieldId());
        assertEquals(DIRECTION, templateFieldDefaultValueDTO.getDirection());
        assertEquals(DEFAULT_VALUE, templateFieldDefaultValueDTO.getDefaultValue());
    }

    private TemplateFieldDefaultValueDTO buildTemplateFieldDefaultValueDTOObject() {
        TemplateFieldDefaultValueDTO dto = new TemplateFieldDefaultValueDTO()
                .setId(ID)
                .setRelationshipTypeId(RELATIONSHIP_TYPE_ID)
                .setTemplateFieldId(TEMPLATE_FIELD_ID)
                .setDirection(DIRECTION)
                .setDefaultValue(DEFAULT_VALUE);
        return dto;
    }
}
