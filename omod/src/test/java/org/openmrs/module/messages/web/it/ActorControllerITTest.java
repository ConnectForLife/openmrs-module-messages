package org.openmrs.module.messages.web.it;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.ApiConstant;
import org.openmrs.module.messages.api.dto.ContactTimeDTO;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.util.TestUtil;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.openmrs.module.messages.ApiConstant.CAREGIVER_RELATIONSHIP;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class ActorControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String BASE_URL = "/messages/actor";

    private static final String SINGLE_CONTACT_TIME_URL_GET = BASE_URL + "/%s/contact-time";

    private static final String SINGLE_CONTACT_TIME_URL_POST = BASE_URL + "/contact-time";

    private static final String CONTACT_TIMES_URL = BASE_URL + "/contact-times";

    private static final String XML_ACTOR_TYPES_DATASET = XML_DATASET_PATH + "ActorTypesDataset.xml";

    private static final String CAREGIVER_UUID = "1286b4bc-2d35-46d6-b645-a1b563aaf62a";

    private static final Integer CAREGIVER_ID = 254453;

    private static final String FATHER_UUID = "5b82938d-5cab-43b7-a8f1-e4d6fbb484cc";

    private static final Integer FATHER_ID = 254452;

    private static final String FATHER_TIME = "11:38";

    private static final String DEFAULT_CONFIGURATION = CAREGIVER_UUID + "," + FATHER_UUID + ":B";

    private static final Integer PATIENT_ID = 254451;

    private static final Integer NOT_EXISTING_PATIENT_ID = 2134141512;

    private static final String PATIENT_TIME = "23:38";

    private static final Integer EXPECTED_NUMBER_OF_VALUES = 2;

    private static final String FATHER_NAME = "Father";

    private static final String EXPECTED_ERROR = String.format("Person with %d id doesn't exist.",
            NOT_EXISTING_PATIENT_ID);

    private static final String MISSING_ID_ERROR = "Missing id parameter.";

    private static final String PERSON_ID_NOT_EXISTS_ERROR = "Person with %d id doesn't exist.";

    private static final String INCORRECT_TIME_VALUE = "24:71";

    private static final String CORRECT_TIME_VALUE = "16:01";

    private static final Integer ANOTHER_PERSON_ID = 254454;

    private static final String ANOTHER_PERSON_TIME = "07:15";

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
                .andExpect(jsonPath("$.[*].actorTypeName").value(hasItem(CAREGIVER_RELATIONSHIP)))
                .andExpect(jsonPath("$.[*].actorTypeName").value(hasItem(FATHER_NAME)));
    }

    @Test
    public void shouldReturnValidationExceptionWhenPatientWithPassedIdNotExist() throws Exception {
        mockMvc.perform(get(BASE_URL + "/" + NOT_EXISTING_PATIENT_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_BAD_REQUEST))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error").value(EXPECTED_ERROR));
    }

    @Test
    public void shouldReturnValidationExceptionWhenPersonIdIsEmpty() throws Exception {
        mockMvc.perform(get(String.format(SINGLE_CONTACT_TIME_URL_GET, ""))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error").value(MISSING_ID_ERROR));
    }

    @Test
    public void shouldReturnNonEmptyContactTime() throws Exception {
        MvcResult result = mockMvc.perform(get(String.format(SINGLE_CONTACT_TIME_URL_GET, PATIENT_ID)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), is(PATIENT_TIME));
    }

    @Test
    public void shouldReturnEmptyContactTime() throws Exception {
        MvcResult result = mockMvc.perform(get(String.format(SINGLE_CONTACT_TIME_URL_GET, CAREGIVER_ID)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andReturn();
        assertThat(result.getResponse().getContentAsString(), is(StringUtils.EMPTY));
    }

    @Test
    public void shouldThrowValidationExceptionWhenPersonDoNotExists() throws Exception {
        mockMvc.perform(get(String.format(SINGLE_CONTACT_TIME_URL_GET, NOT_EXISTING_PATIENT_ID))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.error").value(String.format(PERSON_ID_NOT_EXISTS_ERROR,
                        NOT_EXISTING_PATIENT_ID)));
    }

    @Test
    public void shouldReturnOneNonEmptyAndOneEmpty() throws Exception {
        mockMvc.perform(get(CONTACT_TIMES_URL)
                .param("personIds[]", PATIENT_ID.toString())
                .param("personIds[]", CAREGIVER_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[*].time").value(hasItem(PATIENT_TIME)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(nullValue())));
    }

    @Test
    public void shouldReturnTwoNonEmpty() throws Exception {
        mockMvc.perform(get(CONTACT_TIMES_URL)
                .param("personIds[]", PATIENT_ID.toString())
                .param("personIds[]", FATHER_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[*].time").value(hasItem(PATIENT_TIME)))
                .andExpect(jsonPath("$.[*].time").value(hasItem(FATHER_TIME)));
    }

    @Test
    public void shouldThrowValidationExceptionWhenEmptyValue() throws Exception {
        ContactTimeDTO contactTimeDTO = new ContactTimeDTO()
                .setPersonId(CAREGIVER_ID)
                .setTime(StringUtils.EMPTY);
        mockMvc.perform(post(SINGLE_CONTACT_TIME_URL_POST)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactTimeDTO)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldThrowValidationExceptionWhenIncorrectValue() throws Exception {
        ContactTimeDTO contactTimeDTO = new ContactTimeDTO()
                .setPersonId(CAREGIVER_ID)
                .setTime(INCORRECT_TIME_VALUE);
        mockMvc.perform(post(SINGLE_CONTACT_TIME_URL_POST)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactTimeDTO)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldThrowValidationExceptionWhenOneOfValuesIsIncorrect() throws Exception {
        List<ContactTimeDTO> contactTimeDTOs = Arrays.asList(
                new ContactTimeDTO()
                        .setPersonId(PATIENT_ID)
                        .setTime(PATIENT_TIME),
                new ContactTimeDTO()
                        .setPersonId(CAREGIVER_ID)
                        .setTime(INCORRECT_TIME_VALUE)
        );
        mockMvc.perform(post(CONTACT_TIMES_URL)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactTimeDTOs)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8));
    }

    @Test
    public void shouldSaveTwoOfContactTime() throws Exception {
        List<ContactTimeDTO> contactTimeDTOs = Arrays.asList(
                new ContactTimeDTO()
                        .setPersonId(PATIENT_ID)
                        .setTime(CORRECT_TIME_VALUE),
                new ContactTimeDTO()
                        .setPersonId(CAREGIVER_ID)
                        .setTime(CORRECT_TIME_VALUE)
        );
        mockMvc.perform(post(CONTACT_TIMES_URL)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactTimeDTOs)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(get(CONTACT_TIMES_URL)
                .param("personIds[]", PATIENT_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[*].time").value(hasItem(CORRECT_TIME_VALUE)));
        mockMvc.perform(get(CONTACT_TIMES_URL)
                .param("personIds[]", CAREGIVER_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[*].time").value(hasItem(CORRECT_TIME_VALUE)));
    }

    @Test
    public void shouldSuccessfullyUpdateValueOfContactTime() throws Exception {
        List<ContactTimeDTO> contactTimeDTOs = Arrays.asList(
                new ContactTimeDTO()
                        .setPersonId(PATIENT_ID)
                        .setTime(CORRECT_TIME_VALUE)
        );
        mockMvc.perform(post(CONTACT_TIMES_URL)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactTimeDTOs)))
                .andExpect(status().is(HttpStatus.CREATED.value()));
        mockMvc.perform(get(CONTACT_TIMES_URL)
                .param("personIds[]", PATIENT_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[*].time").value(hasItem(CORRECT_TIME_VALUE)));
    }

    @Test
    public void shouldNotChangeValueOfContactTimeAfterException() throws Exception {
        List<ContactTimeDTO> contactTimeDTOs = Arrays.asList(
                new ContactTimeDTO()
                        .setPersonId(ANOTHER_PERSON_ID)
                        .setTime(INCORRECT_TIME_VALUE)
        );
        mockMvc.perform(post(CONTACT_TIMES_URL)
                .contentType(ApiConstant.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contactTimeDTOs)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8));
        mockMvc.perform(get(CONTACT_TIMES_URL)
                .param("personIds[]", ANOTHER_PERSON_ID.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$.[*].time").value(hasItem(ANOTHER_PERSON_TIME)));
    }
}
