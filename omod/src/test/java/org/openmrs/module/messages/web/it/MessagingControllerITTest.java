package org.openmrs.module.messages.web.it;

import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class MessagingControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String QUERY_1 = "query1";
    private static final String QUERY_TYPE_1 = "query_type1";
    private static final String QUERY_2 = "query2";
    private static final String QUERY_TYPE_2 = "query_type2";
    private static final int THREE_ROWS = 3;
    private static final int FIRST_PAGE = 1;
    private static final int SECOND_PAGE = 2;
    private static final String ROWS_PARAM = "rows";
    private static final String PAGE_PARAM = "page";
    private static final String PATIENT_ID_PARAM = "patientId";
    public static final String START_DATE_PARAM = "startDate";
    public static final String END_DATE_PARAM = "endDate";

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private PatientTemplateDao patientTemplateDao;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PersonService personService;

    @Autowired
    private TemplateDao templateDao;

    private Patient patient1;
    private Patient patient2;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        patient1 = patientService.getAllPatients().get(0);
        patient2 = patientService.getAllPatients().get(1);
    }

    @Test
    public void testFetchingMessagesForPatient() throws Exception {
        Relationship relationship = personService.getAllRelationships().get(0);
        Relationship otherRelationship = personService.getAllRelationships().get(1);
        PatientTemplate template1 = createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_1, QUERY_TYPE_1);

        createPatientTemplate(patient2, otherRelationship.getPersonA(),
                otherRelationship, QUERY_1, QUERY_TYPE_2);

        PatientTemplate template2 = createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_2, QUERY_TYPE_2);

        Patient patientWithId = new Patient();
        patientWithId.setId(patient1.getId());

        MvcResult result = mockMvc.perform(get("/messages/details")
                .param(ROWS_PARAM, String.valueOf(THREE_ROWS))
                .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
                .param(PATIENT_ID_PARAM, patient1.getId().toString()))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));
        MessageDetailsDTO dto = getDtoFromMvcResult(result);

        assertThat(dto.getPatientId(), is(equalTo(patientWithId.getId())));
        assertThat(dto.getMessages().size(), is(equalTo(2)));

        for (MessageDTO messageDTO : dto.getMessages()) {
            assertThat(messageDTO.getType(), anyOf(is(QUERY_TYPE_1), is(QUERY_TYPE_2)));
            assertThat(messageDTO.getAuthor().getUsername(), is(equalTo("admin")));
            assertThat(messageDTO.getActorSchedule().getActorType(),
                    is(equalTo(relationship.getRelationshipType().getaIsToB())));
            assertThat(messageDTO.getCreatedAt(), anyOf(
                    is(equalTo(template1.getDateCreated())),
                    is(equalTo(template2.getDateCreated()))));
        }
    }

    @Test
    public void testPagination() throws Exception {
        Relationship relationship = personService.getAllRelationships().get(0);

        // Page 1, with QUERY_TYPE_1
        createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_1, QUERY_TYPE_1);

        createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_1, QUERY_TYPE_1);

        createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_2, QUERY_TYPE_1);

        // Page 1, with QUERY_TYPE_2
        createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_1, QUERY_TYPE_2);

        createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_1, QUERY_TYPE_2);

        createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, QUERY_2, QUERY_TYPE_2);

        Patient patientWithId = new Patient();
        patientWithId.setId(patient1.getId());

        // Fetch page 1
        MvcResult result = mockMvc.perform(get("/messages/details")
                .param(ROWS_PARAM, String.valueOf(THREE_ROWS))
                .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
                .param(PATIENT_ID_PARAM, patient1.getId().toString()))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));
        assertMessageDetailsDTO(getDtoFromMvcResult(result), QUERY_TYPE_1,
                patientWithId.getPatientId());

        // Fetch page 2
        result = mockMvc.perform(get("/messages/details")
                .param(ROWS_PARAM, String.valueOf(THREE_ROWS))
                .param(PAGE_PARAM, String.valueOf(SECOND_PAGE))
                .param(PATIENT_ID_PARAM, patient1.getId().toString()))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));
        assertMessageDetailsDTO(getDtoFromMvcResult(result), QUERY_TYPE_2,
                patientWithId.getPatientId());
    }

    @Test
    public void shouldReturnMessages() throws Exception {
        createPatientTemplate(
                patient1,
                patient1,
                null,
                "SELECT now() AS EXECUTION_DATE, 1 AS MESSAGE_ID, '1' AS CHANNEL_ID;",
                "SQL"
        );

        final Date startDate = DateUtils.addDays(new Date(), -1);
        final Date endDate = DateUtils.addDays(new Date(), 3);

        MvcResult result = mockMvc.perform(get("/messages")
                .param(PATIENT_ID_PARAM, patient1.getId().toString())
                .param(START_DATE_PARAM, String.valueOf(startDate.getTime()))
                .param(END_DATE_PARAM, String.valueOf(endDate.getTime())))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));

        List<ServiceResultList> results = getResultListFromMvcResult(result);
        assertThat(results, hasSize(1));

        ServiceResultList serviceResultList = results.get(0);
        assertThat(serviceResultList.getStartDate(), is(startDate));
        assertThat(serviceResultList.getEndDate(), is(endDate));
        assertThat(serviceResultList.getPatientId(), is(patient1.getId()));
        assertThat(serviceResultList.getActorId(), is(patient1.getId()));
        assertThat(serviceResultList.getResults(), hasSize(1));

        ServiceResult serviceResult = serviceResultList.getResults().get(0);
        assertThat(serviceResult.getMessageId(), is(1));
        assertThat(serviceResult.getChannelName(), is("1"));
    }

    private void assertMessageDetailsDTO(MessageDetailsDTO dto, String queryType, Integer patientId) {
        assertThat(dto.getPatientId(), is(equalTo(patientId)));
        assertThat(dto.getMessages().size(), is(equalTo(THREE_ROWS)));

        for (MessageDTO messageDTO : dto.getMessages()) {
            assertThat(messageDTO.getType(), is(queryType));
        }
    }

    private MessageDetailsDTO getDtoFromMvcResult(MvcResult result) throws IOException {
        return objectMapper.readValue(
                result.getResponse().getContentAsString(),
                MessageDetailsDTO.class);
    }

    private List<ServiceResultList> getResultListFromMvcResult(MvcResult result) throws IOException {
        return objectMapper.readValue(result.getResponse().getContentAsString(),
                new TypeReference<List<ServiceResultList>>() { });
    }

    private PatientTemplate createPatientTemplate(Patient patient, Person person,
                                                  Relationship relationship,
                                                  String query, String queryType) {
        return patientTemplateDao.saveOrUpdate(new PatientTemplateBuilder()
                .withActor(person)
                .withActorType(relationship)
                .withPatient(patient)
                .withServiceQuery(query)
                .withServiceQueryType(queryType)
                .withTemplate(createTemplate())
                .buildAsNew());
    }

    private Template createTemplate() {
        Template template = new TemplateBuilder()
                .withServiceQuery("SELECT now() AS EXECUTION_DATE, 1 AS MESSAGE_ID, '1' AS CHANNEL_ID;")
                .withServiceQueryType("SQL")
                .setName("Service Name")
                .buildAsNew();
        return templateDao.saveOrUpdate(template);
    }
}
