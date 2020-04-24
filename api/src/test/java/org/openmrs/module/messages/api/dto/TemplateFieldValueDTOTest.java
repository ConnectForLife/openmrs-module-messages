package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TemplateFieldValueDTOTest {

    private static final Integer ID = 1;

    private static final Integer TEMPLATE_FIELD_ID = 5;

    private static final String VALUE = "Deactivate service";

    private static final String UUID = "1b6ebd50-0444-4358-9f24-ee2cce78fe63";

    private TemplateFieldValueDTO templateFieldValueDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        templateFieldValueDTO = buildTemplateFieldValueDTOObject();

        assertThat(templateFieldValueDTO, is(notNullValue()));
        assertEquals(ID, templateFieldValueDTO.getId());
        assertEquals(TEMPLATE_FIELD_ID, templateFieldValueDTO.getTemplateFieldId());
        assertEquals(VALUE, templateFieldValueDTO.getValue());
        assertEquals(UUID, templateFieldValueDTO.getUuid());
    }

    private TemplateFieldValueDTO buildTemplateFieldValueDTOObject() {
        TemplateFieldValueDTO dto = new TemplateFieldValueDTO()
                .withId(ID)
                .withTemplateFieldId(TEMPLATE_FIELD_ID)
                .withValue(VALUE)
                .withUuid(UUID);
        return dto;
    }
}
