package org.openmrs.module.messages.web.it;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.TemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldDTO;
import org.openmrs.module.messages.api.mappers.TemplateMapper;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.service.TemplateService;
import org.openmrs.module.messages.builder.TemplateDTOBuilder;
import org.openmrs.module.messages.builder.TemplateFieldDTOBuilder;
import org.openmrs.module.messages.web.model.TemplateWrapper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.fail;
import static org.openmrs.module.messages.Constant.PAGE_PARAM;
import static org.openmrs.module.messages.Constant.ROWS_PARAM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class TemplateControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String TEMPLATES_DATA_SET_PATH = "datasets/TemplateDataset.xml";

    private static final String BASE_URL = "/messages/templates";

    private static final int THREE_ROWS = 3;

    private static final int ONE_ROWS = 1;

    private static final int FIRST_PAGE = 1;

    private static final int SECOND_PAGE = 2;

    private static final int EXPECTED_TWO_CONTENT_SIZE = 2;

    private static final int EXPECTED_ONE_CONTENT_SIZE = 1;

    private static final int EXPECTED_FIELDS_SET_SIZE_TEMP_1 = 2;

    private static final int EXPECTED_FIELDS_SET_SIZE_TEMP_2 = 1;

    private static final String EMPTY_STRING = "";

    private static final int EXPECTED_NUMBER_OF_ERROR = 6;

    private static final String EXPECTED_ERROR_MESSAGE = "Template isn't a new object (use PUT tu update).";

    private static final int EXISTING_TEMPLATE_ID = 2231;

    private static final String UPDATED_NAME = "Updated Name";

    private static final String UPDATED_DEFAULT_VALUE = "test_value_updated";

    private static final int EXPECTED_NUMBER_OF_FIELDS = 3;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateMapper templateMapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeDataSet(TEMPLATES_DATA_SET_PATH);
    }

    @Test
    public void shouldSuccessfullyReturnAllTemplates() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL)
                .param(ROWS_PARAM, String.valueOf(THREE_ROWS))
                .param(PAGE_PARAM, String.valueOf(FIRST_PAGE)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result, is(notNullValue()));
        PageDTO<TemplateDTO> page = getDtoFromResult(result);
        assertThat(page.getContent(), is(notNullValue()));
        assertThat(page.getContentSize(), is(EXPECTED_TWO_CONTENT_SIZE));
        assertThat(page.getPageSize(), is(THREE_ROWS));
        assertThat(page.getPageIndex(), is(FIRST_PAGE));
        assertThat(page.getContent().size(), is(EXPECTED_TWO_CONTENT_SIZE));
        assertThat(page.getContent().get(0).getTemplateFields().size(), is(EXPECTED_FIELDS_SET_SIZE_TEMP_1));
        assertThat(page.getContent().get(1).getTemplateFields().size(), is(EXPECTED_FIELDS_SET_SIZE_TEMP_2));
    }

    @Test
    public void shouldFetchOnlyOneTemplate() throws Exception {
        MvcResult result = mockMvc.perform(get(BASE_URL)
                .param(ROWS_PARAM, String.valueOf(ONE_ROWS))
                .param(PAGE_PARAM, String.valueOf(SECOND_PAGE)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        assertThat(result, is(notNullValue()));
        PageDTO<TemplateDTO> page = getDtoFromResult(result);
        assertThat(page.getContent(), is(notNullValue()));
        assertThat(page.getContentSize(), is(EXPECTED_ONE_CONTENT_SIZE));
        assertThat(page.getPageSize(), is(ONE_ROWS));
        assertThat(page.getPageIndex(), is(SECOND_PAGE));
        assertThat(page.getContent().size(), is(EXPECTED_ONE_CONTENT_SIZE));
        assertThat(page.getContent().get(0).getTemplateFields().size(), is(EXPECTED_FIELDS_SET_SIZE_TEMP_2));
    }

    @Test
    public void shouldReturnValidationExceptionWhenWrongRequest() throws Exception {
        TemplateFieldDTO field = new TemplateFieldDTOBuilder()
                .withName(EMPTY_STRING)
                .withType(EMPTY_STRING)
                .buildAsNew();
        TemplateDTO request = new TemplateDTOBuilder()
                .withName(EMPTY_STRING)
                .withServiceQuery(EMPTY_STRING)
                .withServiceQueryType(EMPTY_STRING)
                .withTemplateFields(Collections.singletonList(field))
                .buildAsNew();

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_BAD_REQUEST))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.length()").value(EXPECTED_NUMBER_OF_ERROR));
    }

    @Test
    public void shouldCreateSuccessfully() throws Exception {
        TemplateDTO request = new TemplateDTOBuilder().buildAsNew();
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_CREATED))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.uuid").value(notNullValue()));
    }

    @Test
    public void shouldReturnValidationExceptionWhenPostWithNonNullId() throws Exception {
        TemplateDTO request = new TemplateDTOBuilder().build();
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_BAD_REQUEST))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.[0].message").value(EXPECTED_ERROR_MESSAGE));
    }

    @Test
    public void shouldUpdateSuccessfully() throws Exception {
        Template existingTemplate = templateService.getById(EXISTING_TEMPLATE_ID);
        TemplateDTO templateDTO = templateMapper.toDto(existingTemplate);
        templateDTO.setName(UPDATED_NAME);
        templateDTO.getTemplateFields().add(new TemplateFieldDTOBuilder().buildAsNew());
        mockMvc.perform(put(BASE_URL + "/" + existingTemplate.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(templateDTO)))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_OK))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(UPDATED_NAME))
                .andExpect(jsonPath("$.templateFields.length()").value(EXPECTED_NUMBER_OF_FIELDS));
    }

    @Test
    public void shouldBatchUpdateSuccessfully() throws Exception {
        List<Template> templatesToUpdate = templateService.getAll(false);

        assertThat(templatesToUpdate.size(), is(2));
        Template templateToUpdate = templatesToUpdate.get(0);
        TemplateField fieldToUpdate = templateToUpdate.getTemplateFields().get(0);
        fieldToUpdate.setDefaultValue(UPDATED_DEFAULT_VALUE);

        TemplateWrapper request = new TemplateWrapper(templateMapper.toDtos(templatesToUpdate));

        TemplateWrapper updated = getWrapperFromResult(mockMvc.perform(put(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(request)))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_OK))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andReturn());

        assertThat(updated.getTemplates().size(), is(2));

        TemplateDTO updatedTemplate = findTemplate(updated.getTemplates(), templateToUpdate.getId());
        TemplateFieldDTO fieldUpdated = findTemplateField(updatedTemplate.getTemplateFields(),
                fieldToUpdate.getId());

        assertThat(fieldUpdated.getDefaultValue(), is(UPDATED_DEFAULT_VALUE));
    }

    private PageDTO<TemplateDTO> getDtoFromResult(MvcResult result) throws IOException {
        return new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<PageDTO<TemplateDTO>>() {
                });
    }

    private TemplateWrapper getWrapperFromResult(MvcResult result) throws IOException {
        return new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<TemplateWrapper>() {
                });
    }

    private String json(Object obj) throws IOException {
        return new ObjectMapper().writeValueAsString(obj);
    }

    private TemplateDTO findTemplate(List<TemplateDTO> templates, Integer templateId) {
        for (TemplateDTO dto : templates) {
            if (dto.getId().equals(templateId)) {
                return dto;
            }
        }
        fail(String.format("Template with id %d not found", templateId));
        return null;
    }

    private TemplateFieldDTO findTemplateField(List<TemplateFieldDTO> fields, Integer fieldId) {
        for (TemplateFieldDTO dto : fields) {
            if (dto.getId().equals(fieldId)) {
                return dto;
            }
        }
        fail(String.format("Template field with id %d not found", fieldId));
        return null;
    }
}
