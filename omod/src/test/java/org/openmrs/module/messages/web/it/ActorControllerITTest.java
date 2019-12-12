package org.openmrs.module.messages.web.it;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.util.ConfigConstants;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class ActorControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String BASE_URL = "/messages/actor";

    private static final String XML_ACTOR_TYPES_DATASET = XML_DATASET_PATH + "ActorTypesDataset.xml";

    private static final String CAREGIVER_UUID = "1286b4bc-2d35-46d6-b645-a1b563aaf62a";

    private static final String FATHER_UUID = "5b82938d-5cab-43b7-a8f1-e4d6fbb484cc";

    private static final String DEFAULT_CONFIGURATION = CAREGIVER_UUID + "," + FATHER_UUID + ":B";

    private static final Integer PATIENT_ID = 254451;

    private static final Integer NOT_EXISTING_PATIENT_ID = 2134141512;

    private static final Integer EXPECTED_NUMBER_OF_VALUES = 2;

    private static final String CAREGIVER_NAME = "Caregiver";

    private static final String FATHER_NAME = "Father";

    private static final String EXPECTED_ERROR = String.format("Patient with %d id doesn't exist.",
            NOT_EXISTING_PATIENT_ID);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        executeDataSet(XML_ACTOR_TYPES_DATASET);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        Context.getAdministrationService().setGlobalProperty(ConfigConstants.ACTOR_TYPES_KEY, DEFAULT_CONFIGURATION);
    }

    @Test
    public void shouldSuccessfullyReturnAllActors() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + PATIENT_ID.toString()))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(EXPECTED_NUMBER_OF_VALUES))
                .andExpect(jsonPath("$.[*].actorTypeName").value(hasItem(CAREGIVER_NAME)))
                .andExpect(jsonPath("$.[*].actorTypeName").value(hasItem(FATHER_NAME)));
    }

    @Test
    public void shouldReturnValidationExceptionWhenPatientWithPassedIdNotExist() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + NOT_EXISTING_PATIENT_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_BAD_REQUEST))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error").value(EXPECTED_ERROR));
    }
}
