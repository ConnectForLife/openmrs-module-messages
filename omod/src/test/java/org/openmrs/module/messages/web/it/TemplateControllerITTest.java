package org.openmrs.module.messages.web.it;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.api.dto.PageDTO;
import org.openmrs.module.messages.api.dto.TemplateDTO;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class TemplateControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String TEMPLATES_DATA_SET_PATH = "datasets/TemplateDataset.xml";

    private static final String BASE_URL = "/messages/templates";

    private static final String ROWS_PARAM = "rows";

    private static final String PAGE_PARAM = "page";

    private static final int THREE_ROWS = 3;

    private static final int ONE_ROWS = 1;

    private static final int FIRST_PAGE = 1;

    private static final int SECOND_PAGE = 2;

    private static final int EXPECTED_TWO_CONTENT_SIZE = 2;

    private static final int EXPECTED_ONE_CONTENT_SIZE = 1;

    private static final int EXPECTED_FIELDS_SET_SIZE_TEMP_1 = 2;

    private static final int EXPECTED_FIELDS_SET_SIZE_TEMP_2 = 1;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

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

    private PageDTO<TemplateDTO> getDtoFromResult(MvcResult result) throws IOException {
        return new ObjectMapper().readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<PageDTO<TemplateDTO>>() {

                });
    }
}
