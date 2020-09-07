package org.openmrs.module.messages.web.it;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.module.messages.ApiConstant;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.constants.MessagesConstants;
import org.openmrs.module.messages.api.dto.PatientTemplateDTO;
import org.openmrs.module.messages.api.dto.TemplateFieldValueDTO;
import org.openmrs.module.messages.api.mappers.PatientTemplateMapper;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.service.PatientTemplateService;
import org.openmrs.module.messages.domain.criteria.PatientTemplateCriteria;
import org.openmrs.module.messages.util.TestUtil;
import org.openmrs.module.messages.web.model.PatientTemplatesWrapper;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.openmrs.module.messages.ApiConstant.PAGE_PARAM;
import static org.openmrs.module.messages.ApiConstant.ROWS_PARAM;
import static org.openmrs.module.messages.util.TestUtil.loadSystemRelationshipsToActorTypes;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class PatientTemplateControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String XML_DATA_SET_PATH = "datasets/";
    private static final int TEMPLATE_1_ID = 1;
    private static final int TEMPLATE_FIELD_1_ID = 1;

    private static final int PATIENT_1_ID = 101;
    private static final int PATIENT_2_ID = 102;
    private static final int PATIENT_3_ID = 103;

    private static final int PATIENT_1_TEMPLATE_1_ID = 1;
    private static final int PATIENT_1_TEMPLATE_2_ID = 2;
    private static final int PATIENT_1_TEMPLATE_3_ID = 3;
    private static final int PATIENT_2_TEMPLATE_1_ID = 4;
    private static final String PATIENT_2_TEMPLATE_1_UUID = "81c5d663-47e8-4116-a972-b6c121a770e5";
    private static final int PATIENT_2_TEMPLATE_2_ID = 5;
    private static final String PATIENT_2_TEMPLATE_2_UUID = "332acf89-0c76-4303-97f6-c590d5ad5589";

    private static final int PATIENT_3_ACTOR_ID = 103;
    private static final int PATIENT_2_ACTOR_ID = 102;

    private static final int PATIENT_3_ACTOR_TYPE_ID = 103;
    private static final int PATIENT_2_ACTOR_TYPE_ID = 102;

    private static final String PATIENT_3_TEMPLATE_FIELD_VALUE_1_VALUE = MessagesConstants.DEACTIVATED_SERVICE;
    private static final int PATIENT_2_TEMPLATE_FIELD_VALUE_1_ID = 4;
    private static final String PATIENT_2_TEMPLATE_FIELD_VALUE_1_VALUE = Constant.CHANNEL_TYPE_CALL;
    private static final String PATIENT_2_TEMPLATE_FIELD_VALUE_1_UUID = "e8f09dc3-0655-486f-af1e-b3c39f39f101";
    private static final int PATIENT_2_TEMPLATE_FIELD_VALUE_2_ID = 5;
    private static final String PATIENT_2_TEMPLATE_FIELD_VALUE_2_VALUE = Constant.CHANNEL_TYPE_SMS;
    private static final String PATIENT_2_TEMPLATE_FIELD_VALUE_2_UUID = "1c0055b3-b323-4f23-bad6-e75e0362af46";

    private static final int NON_EXISTING_PATIENT_ID = 999999;

    private static final int PAGE_1 = 1;
    private static final int PAGE_2 = 2;
    private static final int PAGE_SIZE_2 = 2;
    private static final int PAGE_SIZE_3 = 3;
    private static final int PAGE_SIZE_10 = 10;
    private static final int EMPTY_RESULT_SIZE = 0;
    private static final int RESULT_SIZE_1 = 1;
    private static final int RESULT_SIZE_2 = 2;

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    @Qualifier("messages.patientTemplateService")
    private PatientTemplateService patientTemplateService;

    @Autowired
    @Qualifier("messages.patientTemplateMapper")
    private PatientTemplateMapper mapper;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeDataSet(XML_DATA_SET_PATH + "ConceptDataSet.xml");
        executeDataSet(XML_DATA_SET_PATH + "ConfigDataset.xml");
        executeDataSet(XML_DATA_SET_PATH + "PatientTemplateDataSet.xml");
        loadSystemRelationshipsToActorTypes();
    }

    @Test
    public void shouldReturnAllForPatientOne() throws Exception {
        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_1_ID))
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
        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_1_ID)
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
        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_1_ID)
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
        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_1_ID)
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
        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .param(PAGE_PARAM, String.valueOf(PAGE_1))
                .param(ROWS_PARAM, String.valueOf(PAGE_SIZE_10)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()").value(EMPTY_RESULT_SIZE))
                .andReturn();
    }

    @Test
    public void shouldCreateCollection() throws Exception {
        final int resultSize = 1;
        PatientTemplateDTO dto = createDTOForPatientTemplate3();

        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientTemplates.[0].patientId").value(PATIENT_3_ID))
                .andExpect(jsonPath("$.patientTemplates.[0].actorTypeId").value(PATIENT_3_ACTOR_TYPE_ID))
                .andExpect(jsonPath("$.patientTemplates.[0].actorId").value(PATIENT_3_ACTOR_ID))
                .andExpect(jsonPath("$.patientTemplates.[0].templateId").value(TEMPLATE_1_ID))
                .andExpect(jsonPath("$.patientTemplates.length()").value(resultSize))
                .andReturn();
    }

    @Test
    public void shouldRemovePatientTemplate() throws Exception {
        PatientTemplateDTO dto = createDTOForPatientTemplate3WithValue();
        PatientTemplatesWrapper body = wrap(dto);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientTemplates.[0].patientId")
                        .value(PATIENT_3_ID))
                .andExpect(jsonPath("$.patientTemplates.length()")
                        .value(RESULT_SIZE_1))
                .andReturn();

        body = new PatientTemplatesWrapper(new ArrayList<>());

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<PatientTemplate> patientTemplates = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forPatientId(PATIENT_3_ID));
        assertTrue(patientTemplates.isEmpty());
    }

    @Test
    public void shouldRemovePatientTemplateAndAddDifferentAndLeaveSome() throws Exception {
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(createDTOForPatientTemplates2());
        List<PatientTemplateDTO> dtos = new ArrayList<>();
        // 2 templates on db, so we will POST only one of existing
        dtos.add(body.getPatientTemplates().get(0));
        // and one completely new
        dtos.add(new PatientTemplateDTO()
                .withId(null)
                .withActorId(PATIENT_2_ACTOR_ID)
                .withPatientId(PATIENT_2_ID)
                .withTemplateId(TEMPLATE_1_ID)
                .withUuid(null)); // new values from DE will have both ids set to null
        body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_2_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));

        List<PatientTemplate> patientTemplates = patientTemplateService.findAllByCriteria(
                PatientTemplateCriteria.forPatientId(PATIENT_2_ID));
        assertThat(patientTemplates.size(), is(RESULT_SIZE_2));
        assertThat(patientTemplates.get(0).getId(), is(PATIENT_2_TEMPLATE_1_ID));
        assertThat(patientTemplates.get(1).getId(), not(PATIENT_2_TEMPLATE_2_ID));
    }

    @Test
    public void shouldSaveWithoutActorType() throws Exception {
        PatientTemplateDTO dto = createDTOForPatientTemplate3()
                .withActorTypeId(null);

        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientTemplates.length()").value(1))
                .andExpect(jsonPath("$.patientTemplates.[0].actorTypeId").doesNotExist())
                .andReturn();
    }

    @Test
    public void shouldBeSavedTogetherWithNestedTemplateFieldValues() throws Exception {
        final String expectedValue = Constant.CHANNEL_TYPE_CALL;

        TemplateFieldValueDTO valueDTO = new TemplateFieldValueDTO()
                .withValue(expectedValue)
                .withTemplateFieldId(TEMPLATE_FIELD_1_ID);

        List<TemplateFieldValueDTO> valueDTOs = new ArrayList<TemplateFieldValueDTO>();
        valueDTOs.add(valueDTO);

        PatientTemplateDTO dto = createDTOForPatientTemplate3()
                .withTemplateFieldValues(valueDTOs);

        List<PatientTemplateDTO> dtos = new ArrayList<PatientTemplateDTO>();
        dtos.add(dto);
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.patientTemplates.length()").value(1))
                .andExpect(jsonPath("$.patientTemplates.[0].templateFieldValues.length()").value(1))
                .andExpect(jsonPath("$.patientTemplates.[0].templateFieldValues[0].value").value(expectedValue))
                .andReturn();
    }

    @Test
    public void shouldReturnBadRequestWhenPatientIdIsMissing() throws Exception {
        PatientTemplateDTO dto = createDTOForPatientTemplate3();
        dto.withPatientId(null);

        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    public void shouldReturnBadRequestWhenNonExistingPatientIdIsPassed() throws Exception {
        PatientTemplateDTO dto = createDTOForPatientTemplate3();
        dto.withPatientId(NON_EXISTING_PATIENT_ID);

        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        PatientTemplatesWrapper body = new PatientTemplatesWrapper(dtos);

        mockMvc.perform(post("/messages/patient-templates/patient/{id}", PATIENT_3_ID)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(body)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    private PatientTemplateDTO createDTOForPatientTemplate3() {
        return new PatientTemplateDTO()
                .withActorId(PATIENT_3_ACTOR_ID)
                .withActorTypeId(PATIENT_3_ACTOR_TYPE_ID)
                .withPatientId(PATIENT_3_ID)
                .withTemplateId(TEMPLATE_1_ID);
    }

    private PatientTemplateDTO createDTOForPatientTemplate3WithValue() {
        PatientTemplateDTO dto = createDTOForPatientTemplate3();
        TemplateFieldValueDTO valueDTO = new TemplateFieldValueDTO()
                .withValue(PATIENT_3_TEMPLATE_FIELD_VALUE_1_VALUE)
                .withTemplateFieldId(TEMPLATE_FIELD_1_ID);
        dto.getTemplateFieldValues().add(valueDTO);
        return dto;
    }

    private List<PatientTemplateDTO> createDTOForPatientTemplates2() {
        PatientTemplateDTO dto1 = new PatientTemplateDTO()
                .withId(PATIENT_2_TEMPLATE_1_ID)
                .withActorId(PATIENT_2_ACTOR_ID)
                .withPatientId(PATIENT_2_ID)
                .withTemplateId(TEMPLATE_1_ID)
                .withUuid(PATIENT_2_TEMPLATE_1_UUID);

        TemplateFieldValueDTO value1 = new TemplateFieldValueDTO()
                .withId(PATIENT_2_TEMPLATE_FIELD_VALUE_1_ID)
                .withValue(PATIENT_2_TEMPLATE_FIELD_VALUE_1_VALUE)
                .withTemplateFieldId(TEMPLATE_FIELD_1_ID)
                .withUuid(PATIENT_2_TEMPLATE_FIELD_VALUE_1_UUID);
        dto1.getTemplateFieldValues().add(value1);

        PatientTemplateDTO dto2 = new PatientTemplateDTO()
                .withId(PATIENT_2_TEMPLATE_2_ID)
                .withActorId(PATIENT_2_ACTOR_ID)
                .withPatientId(PATIENT_2_ID)
                .withTemplateId(TEMPLATE_1_ID)
                .withUuid(PATIENT_2_TEMPLATE_2_UUID);

        TemplateFieldValueDTO value2 = new TemplateFieldValueDTO()
                .withId(PATIENT_2_TEMPLATE_FIELD_VALUE_2_ID)
                .withValue(PATIENT_2_TEMPLATE_FIELD_VALUE_2_VALUE)
                .withTemplateFieldId(TEMPLATE_FIELD_1_ID)
                .withUuid(PATIENT_2_TEMPLATE_FIELD_VALUE_2_UUID);
        dto2.getTemplateFieldValues().add(value2);

        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto1);
        dtos.add(dto2);

        return dtos;
    }

    private PatientTemplatesWrapper wrap(PatientTemplateDTO dto) {
        List<PatientTemplateDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        return new PatientTemplatesWrapper(dtos);
    }
}
