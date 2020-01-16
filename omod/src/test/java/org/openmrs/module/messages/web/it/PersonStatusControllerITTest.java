package org.openmrs.module.messages.web.it;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.BaseModuleWebContextSensitiveWithActivatorTest;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.constants.ConfigConstants;
import org.openmrs.module.messages.api.dto.PersonStatusDTO;
import org.openmrs.module.messages.api.model.ErrorMessageEnum;
import org.openmrs.module.messages.api.model.PersonStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebAppConfiguration
public class PersonStatusControllerITTest extends BaseModuleWebContextSensitiveWithActivatorTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String BASE_URL_PATTERN = "/messages/person-statuses/%s";

    private static final String XML_PERSON_ATTRIBUTE_DATASET = XML_DATASET_PATH + "PersonAttributeDataset.xml";

    private static final String PERSON_UUID = "d64b7f82-5857-48f5-8694-b1bdb46688c3";

    private static final String VOIDED_CONTACT_TIME = "48212932834";

    private static final String ACTUAL_CONTACT_TIME = "48421029382";

    private static final String VOIDED_STATUS = PersonStatus.NO_CONSENT.name();

    private static final String ACTUAL_STATUS = PersonStatus.ACTIVE.name();

    private static final String EXPECTED_ACTIVE_STYLE =
            "background-color: #51a351; border-color: #51a351; color: #f5f5f5;";

    private static final String EXPECTED_MISSING_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    private static final String EXPECTED_DEACTIVATE_STYLE =
            "background-color: #f23722; border-color: #f23722; color: #f5f5f5;";

    private static final String NOT_EXIST_PERSON = "8124263d-d1ec-4be5-bf62-502ab125d076";

    private static final String PERSON_WITHOUT_ATTRIBUTE = "8c8169be-94f0-42ba-81de-cf3f2c12a7ec";

    private static final String TEST_REASON = "The status was changed for test reason";

    private static final String EXPECTED_ERROR = "Could not fetch person status for personId: %s";

    private Person person;

    private MockMvc mockMvc;

    @Autowired
    @Qualifier("personService")
    private PersonService personService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        executeDataSet(XML_PERSON_ATTRIBUTE_DATASET);
        person = personService.getPersonByUuid(PERSON_UUID);
        PersonAttributeType contactTimeAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_CONTACT_TIME_TYPE_UUID);
        PersonAttributeType personStatusAttributeType = personService.getPersonAttributeTypeByUuid(
                ConfigConstants.PERSON_STATUS_ATTRIBUTE_TYPE_UUID);

        PersonAttribute voidedContactTime = new PersonAttribute(contactTimeAttributeType, VOIDED_CONTACT_TIME);
        person.addAttribute(voidedContactTime);
        PersonAttribute voidedStatus = new PersonAttribute(personStatusAttributeType, VOIDED_STATUS);
        person.addAttribute(voidedStatus);
        person = personService.savePerson(person);
        Context.flushSession();
        getConnection().commit();
        PersonAttribute actualContactTime = new PersonAttribute(contactTimeAttributeType, ACTUAL_CONTACT_TIME);
        person.addAttribute(actualContactTime);
        PersonAttribute actualStatus = new PersonAttribute(personStatusAttributeType, ACTUAL_STATUS);
        person.addAttribute(actualStatus);
        person = personService.savePerson(person);
    }

    @Test
    public void shouldFetchStatusByPersonId() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, person.getId().toString())))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(is(person.getId().toString())))
                .andExpect(jsonPath("$.title").value(is(PersonStatus.ACTIVE.getTitleKey())))
                .andExpect(jsonPath("$.value").value(is(PersonStatus.ACTIVE.name())))
                .andExpect(jsonPath("$.style").value(is(EXPECTED_ACTIVE_STYLE)))
                .andExpect(jsonPath("$.reason").value(is(nullValue())));
    }

    @Test
    public void shouldFetchStatusByPersonUuid() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, person.getUuid())))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(person.getUuid()))
                .andExpect(jsonPath("$.title").value(PersonStatus.ACTIVE.getTitleKey()))
                .andExpect(jsonPath("$.value").value(PersonStatus.ACTIVE.name()))
                .andExpect(jsonPath("$.style").value(EXPECTED_ACTIVE_STYLE))
                .andExpect(jsonPath("$.reason").value(nullValue()));
    }

    @Test
    public void shouldReturnEntityNotFoundExceptionIfPersonNotExists() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, NOT_EXIST_PERSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_NOT_FOUND))
                .andExpect(content().contentType(Constant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.[0].code").value(ErrorMessageEnum.ERR_ENTITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errorMessages.[0].message").value(String.format(EXPECTED_ERROR, NOT_EXIST_PERSON)));
    }

    @Test
    public void shouldFetchMissingStatusIfPersonHasNotStatusAttribute() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, PERSON_WITHOUT_ATTRIBUTE)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(PERSON_WITHOUT_ATTRIBUTE))
                .andExpect(jsonPath("$.title").value(PersonStatus.MISSING_VALUE.getTitleKey()))
                .andExpect(jsonPath("$.value").value(PersonStatus.MISSING_VALUE.name()))
                .andExpect(jsonPath("$.style").value(EXPECTED_MISSING_STYLE))
                .andExpect(jsonPath("$.reason").value(nullValue()));
    }

    @Test
    public void shouldUpdateStatusAttribute() throws Exception {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getUuid())
                .setValue(PersonStatus.DEACTIVATE.name())
                .setReason(TEST_REASON);

        mockMvc.perform(put(String.format(BASE_URL_PATTERN, person.getUuid()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(status)))
                .andExpect(status().is(org.apache.http.HttpStatus.SC_OK))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(person.getUuid()))
                .andExpect(jsonPath("$.title").value(PersonStatus.DEACTIVATE.getTitleKey()))
                .andExpect(jsonPath("$.value").value(PersonStatus.DEACTIVATE.name()))
                .andExpect(jsonPath("$.style").value(EXPECTED_DEACTIVATE_STYLE))
                .andExpect(jsonPath("$.reason").value(TEST_REASON));
    }

    private String json(Object obj) throws IOException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
