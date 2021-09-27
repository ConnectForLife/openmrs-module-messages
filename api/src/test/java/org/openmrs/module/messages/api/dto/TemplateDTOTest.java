package org.openmrs.module.messages.api.dto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TemplateDTOTest {

    private static final Integer ID = 1;

    private static final String NAME = "Adherence report daily";

    private static final String SERVICE_QUERY = "SELECT * FROM table;";

    private static final String SERVICE_QUERY_TYPE = "SQL";

    private static final String UUID = "9556482a-20b2-11ea-ac12-0242c0a82002";

    private List<TemplateFieldDTO> templateFields = new ArrayList<>();

    private TemplateDTO templateDTO;

    @Test
    public void shouldBuildDtoObjectSuccessfully() {
        templateDTO = buildTemplateDTOObjectSuccessfully();

        assertThat(templateDTO, is(notNullValue()));
        assertEquals(ID, templateDTO.getId());
        assertEquals(NAME, templateDTO.getName());
        assertEquals(SERVICE_QUERY, templateDTO.getServiceQuery());
        assertEquals(SERVICE_QUERY_TYPE, templateDTO.getServiceQueryType());
        assertEquals(templateFields, templateDTO.getTemplateFields());
        assertEquals(UUID, templateDTO.getUuid());
    }

    private TemplateDTO buildTemplateDTOObjectSuccessfully() {
        TemplateDTO dto = new TemplateDTO()
                .setId(ID)
                .setName(NAME)
                .setServiceQuery(SERVICE_QUERY)
                .setServiceQueryType(SERVICE_QUERY_TYPE)
                .setTemplateFields(templateFields)
                .setUuid(UUID);
        return dto;
    }

}
