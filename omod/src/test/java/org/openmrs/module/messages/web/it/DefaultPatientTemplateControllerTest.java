package org.openmrs.module.messages.web.it;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.openmrs.module.messages.ApiConstant.PAGE_PARAM;
import static org.openmrs.module.messages.ApiConstant.ROWS_PARAM;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
public class DefaultPatientTemplateControllerTest extends BaseModuleWebContextSensitiveTest {

    private static final String ACTOR_DEFAULT_VALUE_SERVICE_TYPE = "Call";
    private static final String TEMPLATES_DATA_SET_PATH = "datasets/TemplateWithDefaultValuesDataset.xml";
    private static final int PATIENT_1_ID = 254451;
    private static final int PATIENT_2_ID = 102;
    private static final int PATIENT_3_ID = 103;
    private static final int PERSON_2_ID = 254452;
    private static final int PERSON_3_ID = 254453;
    private static final int FIRST_PAGE = 1;
    private static final int PAGE_SIZE = 10;
    private static final String CAREGIVER_UUID = "1286b4bc-2d35-46d6-b645-a1b563aaf62a";
    private static final String FATHER_UUID = "5b82938d-5cab-43b7-a8f1-e4d6fbb484cc";
    private static final String DEFAULT_CONFIGURATION = CAREGIVER_UUID + "," + FATHER_UUID + ":B";
    private static final String DEFAULT_VALUE_SERVICE_TYPE = "Deactivated";
    private static final String DEFAULT_VALUE_FREQUENCY = "Daily";
    private static final String ACTOR_DEFAULT_VALUE_FREQUENCY = "Weekly";
    private static final int FIRST_TEMPLATE = 2231;
    private static final int SECOND_TEMPLATE = 2232;
    private static final String SMS_TYPE = "SMS";
    private static final int SIX = 6;
    private static final int TWO = 2;
    public static final int ONE = 1;
    public static final int THREE = 3;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeDataSet(TEMPLATES_DATA_SET_PATH);
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY,
            DEFAULT_CONFIGURATION);
    }

    @Test
    public void shouldReturnDefaultValuesUsedForPatientWithoutPT() throws Exception {
        mockMvc.perform(get("/messages/defaults/{patientId}/check", PATIENT_1_ID))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.defaultValuesUsed").value(true))
            .andExpect(jsonPath("$.allValuesDefault").value(true))
            .andExpect(jsonPath("$.details.messages.length()").value(TWO))
            .andExpect(jsonPath("$.details.messages.[0].actorSchedules.length()").value(THREE))
            .andExpect(jsonPath("$.details.messages.[1].actorSchedules.length()").value(THREE))
            .andReturn();
    }

    @Test
    public void shouldReturnDefaultValuesUsedForPatientWithOneOfTwoPT() throws Exception {
        mockMvc.perform(get("/messages/defaults/{patientId}/check", PATIENT_2_ID))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.defaultValuesUsed").value(true))
            .andExpect(jsonPath("$.allValuesDefault").value(false))
            .andExpect(jsonPath("$.details.messages.length()").value(TWO))
            .andExpect(jsonPath("$.details.messages.[0].actorSchedules.length()").value(ONE))
            .andExpect(jsonPath("$.details.messages.[1].actorSchedules.length()").value(ONE))
            .andReturn();
    }

    @Test
    public void shouldNotReturnDefaultValuesUsedForPatientWithAllPT() throws Exception {
        mockMvc.perform(get("/messages/defaults/{patientId}/check", PATIENT_3_ID))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.defaultValuesUsed").value(false))
            .andExpect(jsonPath("$.allValuesDefault").value(false))
            .andReturn();
    }

    @Test
    public void shouldNotSetDefaultValuesToPatientTemplates() throws Exception {
        mockMvc.perform(post("/messages/defaults/{patientId}/generate-and-save",
            PATIENT_3_ID))
            .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
            .andReturn();
    }

    @Test
    public void shouldSaveDefaultValuesToPatientTemplatesForEachActor() throws Exception {
        mockMvc.perform(post("/messages/defaults/{patientId}/generate-and-save",
            PATIENT_1_ID))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(
                jsonPath(String.format("$.[?(@.actorId === %s)].templateFieldValues.[*].value",
                    PATIENT_1_ID))
                    .value(containsInAnyOrder(DEFAULT_VALUE_SERVICE_TYPE, DEFAULT_VALUE_FREQUENCY,
                        SMS_TYPE))
            )
            .andExpect(
                jsonPath(String.format("$.[?(@.actorId === %s)].templateFieldValues.[*].value",
                    PERSON_2_ID))
                    .value(containsInAnyOrder(ACTOR_DEFAULT_VALUE_SERVICE_TYPE,
                        ACTOR_DEFAULT_VALUE_FREQUENCY, SMS_TYPE))
            )
            .andExpect(
                jsonPath(String.format("$.[?(@.actorId === %s)].templateFieldValues.[*].value",
                    PERSON_3_ID))
                    .value(containsInAnyOrder(DEFAULT_VALUE_SERVICE_TYPE, DEFAULT_VALUE_FREQUENCY,
                        SMS_TYPE))
            )
            .andExpect(jsonPath("$.length()").value(SIX))
            .andReturn();

        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_1_ID)
            .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(
                jsonPath(String.format("$.content.[?(@.actorId === %s)].templateFieldValues.[*]" +
                        ".value",
                    PATIENT_1_ID))
                    .value(containsInAnyOrder(DEFAULT_VALUE_SERVICE_TYPE, DEFAULT_VALUE_FREQUENCY,
                        SMS_TYPE))
            )
            .andExpect(
                jsonPath(String.format("$.content.[?(@.actorId === %s)].templateFieldValues.[*]" +
                        ".value",
                    PERSON_2_ID))
                    .value(containsInAnyOrder(ACTOR_DEFAULT_VALUE_SERVICE_TYPE,
                        ACTOR_DEFAULT_VALUE_FREQUENCY, SMS_TYPE))
            )
            .andExpect(
                jsonPath(String.format("$.content.[?(@.actorId === %s)].templateFieldValues.[*]" +
                        ".value",
                    PERSON_3_ID))
                    .value(containsInAnyOrder(DEFAULT_VALUE_SERVICE_TYPE, DEFAULT_VALUE_FREQUENCY,
                        SMS_TYPE))
            )
            .andExpect(jsonPath("$.content.length()").value(SIX))
            .andReturn();
    }

    @Test
    public void shouldSaveDefaultValuesToPatientTemplatesForSinglePatient() throws Exception {
        mockMvc.perform(post("/messages/defaults/{patientId}/generate-and-save",
            PATIENT_2_ID))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[0].patientId").value(PATIENT_2_ID))
            .andExpect(jsonPath("$.[0].actorId").value(PATIENT_2_ID))
            .andExpect(jsonPath("$.[0].templateId").value(SECOND_TEMPLATE))
            .andExpect(jsonPath("$.length()").value(ONE))
            .andReturn();

        mockMvc.perform(get("/messages/patient-templates/patient/{id}", PATIENT_2_ID)
            .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE)))
            .andExpect(status().is(HttpStatus.OK.value()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.content.length()").value(TWO))
            .andExpect(jsonPath("$.content.[0].patientId").value(PATIENT_2_ID))
            .andExpect(jsonPath("$.content.[0].actorId").value(PATIENT_2_ID))
            .andExpect(jsonPath("$.content.[1].patientId").value(PATIENT_2_ID))
            .andExpect(jsonPath("$.content.[1].actorId").value(PATIENT_2_ID))
            .andExpect(jsonPath("$.content.[*].templateId")
                .value(containsInAnyOrder(FIRST_TEMPLATE, SECOND_TEMPLATE)))
            .andReturn();
    }
}
