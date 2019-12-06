package org.openmrs.module.messages.web.it;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.util.TestUtil;
import org.openmrs.module.messages.web.model.PatientTemplatesWrapper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.openmrs.module.messages.Constant.PAGE_PARAM;
import static org.openmrs.module.messages.Constant.ROWS_PARAM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class PatientTemplateControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String XML_DATA_SET_PATH = "datasets/";
    private static final int PATIENT_1_ID = 101;
    private static final int PATIENT_2_ID = 102;
    private static final int PATIENT_1_TEMPLATE_1_ID = 1;
    private static final int PATIENT_1_TEMPLATE_2_ID = 2;
    private static final int PATIENT_1_TEMPLATE_3_ID = 3;
    private static final int PAGE_1 = 1;
    private static final int PAGE_2 = 2;
    private static final int PAGE_SIZE_2 = 2;
    private static final int PAGE_SIZE_3 = 3;
    private static final int PAGE_SIZE_10 = 10;
    private static final int EMPTY_RESULT_SIZE = 0;
    private static final int RESULT_SIZE_1 = 1;
    private static final int PATIENT_3_ID = 103;
    private static final int PATIENT_3_ACTOR_ID = 103;
    private static final int PATIENT_3_ACTOR_TYPE_ID = 103;
    private static final int TEMPLATE_1_ID = 1;
    private static final String PATIENT_3_QUERY_TYPE = "SQL";
    private static final String PATIENT_3_QUERY = "SELECT (*) FROM messages_scheduled_service;";

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "PatientTemplateDataSet.xml");
    }

    @Test
    public void shouldReturnAllForPatientOne() throws Exception {
        mockMvc.perform(get("/patient-templates/patient/{id}", PATIENT_1_ID))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_1_ID)))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_2_ID)))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_3_ID)))
            .andExpect(jsonPath("$.content.[*].templateFieldValues.[*].value")
                .value(hasItem("test value")))
            .andExpect(jsonPath("$.content.[*].templateFieldValues.[*].value")
                .value(hasItem("test value 2")))
            .andExpect(jsonPath("$.content.[*].templateFieldValues.[*].value")
                .value(hasItem("test value 3")))
            .andExpect(jsonPath("$.content.length()").value(PAGE_SIZE_3))
            .andReturn();
    }

    @Test
    public void shouldReturnThreePatientTemplatesForPatientOne() throws Exception {
        mockMvc.perform(get("/patient-templates/patient/{id}", PATIENT_1_ID)
            .param(PAGE_PARAM, String.valueOf(PAGE_1))
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE_3)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_1_ID)))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_2_ID)))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_3_ID)))
            .andExpect(jsonPath("$.content.[*].templateFieldValues.[*].value")
                .value(hasItem("test value")))
            .andExpect(jsonPath("$.content.[*].templateFieldValues.[*].value")
                .value(hasItem("test value 2")))
            .andExpect(jsonPath("$.content.[*].templateFieldValues.[*].value")
                .value(hasItem("test value 3")))
            .andExpect(jsonPath("$.content.length()").value(PAGE_SIZE_3))
            .andReturn();
    }

    @Test
    public void shouldReturnTwoPatientTemplates() throws Exception {
        mockMvc.perform(get("/patient-templates/patient/{id}", PATIENT_1_ID)
            .param(PAGE_PARAM, String.valueOf(PAGE_1))
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE_2)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_1_ID)))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_2_ID)))
            .andExpect(jsonPath("$.content.length()").value(PAGE_SIZE_2))
            .andReturn();
    }

    @Test
    public void shouldReturnSecondPageOfPatientTemplates() throws Exception {
        mockMvc.perform(get("/patient-templates/patient/{id}", PATIENT_1_ID)
            .param(PAGE_PARAM, String.valueOf(PAGE_2))
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE_2)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.[*].id").value(hasItem(PATIENT_1_TEMPLATE_3_ID)))
            .andExpect(jsonPath("$.content.length()").value(RESULT_SIZE_1))
            .andReturn();
    }

    @Test
    public void shouldReturnEmptyPageForSecondPatient() throws Exception {
        mockMvc.perform(get("/patient-templates/patient/{id}", PATIENT_2_ID)
            .param(PAGE_PARAM, String.valueOf(PAGE_1))
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE_10)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.length()").value(EMPTY_RESULT_SIZE))
            .andReturn();
    }

    @Test
    public void shouldReturnSaveCollection() throws Exception {
        final int resultSize = 1;
        PatientTemplateDTO dto = new PatientTemplateDTO()
            .setActorId(PATIENT_3_ACTOR_ID)
            .setActorTypeId(PATIENT_3_ACTOR_TYPE_ID)
            .setTemplateId(1)
            .setPatientId(PATIENT_3_ID)
            .setServiceQuery(PATIENT_3_QUERY)
            .setServiceQueryType(PATIENT_3_QUERY_TYPE);

        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/patient-templates/patient/{id}", PATIENT_3_ID)
            .contentType(Constant.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(body)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.patientTemplates.[0].patientId").value(PATIENT_3_ID))
            .andExpect(jsonPath("$.patientTemplates.[0].serviceQuery").value(PATIENT_3_QUERY))
            .andExpect(jsonPath("$.patientTemplates.[0].serviceQueryType").value(PATIENT_3_QUERY_TYPE))
            .andExpect(jsonPath("$.patientTemplates.[0].actorTypeId").value(PATIENT_3_ACTOR_TYPE_ID))
            .andExpect(jsonPath("$.patientTemplates.[0].actorId").value(PATIENT_3_ACTOR_ID))
            .andExpect(jsonPath("$.patientTemplates.[0].templateId").value(TEMPLATE_1_ID))
            .andExpect(jsonPath("$.patientTemplates.length()").value(resultSize))
            .andReturn();
    }

}
