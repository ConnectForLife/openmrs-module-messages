package org.openmrs.module.messages.web.it;

import org.apache.commons.lang3.time.DateUtils;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.module.messages.Constant;
import org.openmrs.module.messages.api.dao.PatientTemplateDao;
import org.openmrs.module.messages.api.dao.TemplateDao;
import org.openmrs.module.messages.api.dto.ActorScheduleDTO;
import org.openmrs.module.messages.api.dto.MessageDTO;
import org.openmrs.module.messages.api.dto.MessageDetailsDTO;
import org.openmrs.module.messages.api.execution.ServiceResult;
import org.openmrs.module.messages.api.execution.ServiceResultList;
import org.openmrs.module.messages.api.model.PatientTemplate;
import org.openmrs.module.messages.api.model.Template;
import org.openmrs.module.messages.api.model.TemplateField;
import org.openmrs.module.messages.api.model.TemplateFieldType;
import org.openmrs.module.messages.api.model.TemplateFieldValue;
import org.openmrs.module.messages.builder.PatientTemplateBuilder;
import org.openmrs.module.messages.builder.TemplateBuilder;
import org.openmrs.module.messages.builder.TemplateFieldBuilder;
import org.openmrs.module.messages.builder.TemplateFieldValueBuilder;
import org.openmrs.module.messages.util.TestUtil;
import org.openmrs.web.test.BaseModuleWebContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.openmrs.module.messages.api.constants.MessagesConstants.PATIENT_DEFAULT_ACTOR_TYPE;
import static org.openmrs.module.messages.util.TestUtil.loadSystemRelationshipsToActorTypes;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class MessagingControllerITTest extends BaseModuleWebContextSensitiveTest {

    private static final String QUERY_TYPE_1 = "query_type1";
    private static final String QUERY_TYPE_2 = "query_type2";
    private static final int THREE = 3;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final int TEN = 10;
    private static final int FIRST_PAGE = 1;
    private static final int SECOND_PAGE = 2;
    private static final String ROWS_PARAM = "rows";
    private static final String PAGE_PARAM = "page";
    private static final String PERSON_ID_PARAM = "personId";
    public static final String START_DATE_PARAM = "startDate";
    public static final String END_DATE_PARAM = "endDate";
    private static final String MESSAGE_TEMPLATE_NAME = "Template Name";
    private static final String EXPECTED_DEACTIVATED_STATE = "DEACTIVATED";
    private static final int PAGE_SIZE_ONE_HUNDRED = 1000;

    private MockMvc mockMvc;

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
        loadSystemRelationshipsToActorTypes();
    }

    @Test
    @Ignore
    public void testFetchingMessagesForPatientWithDeactivatedMessageWhenServiceIsDeactivated() throws Exception {
        Relationship relationship = personService.getAllRelationships().get(0);
        Relationship otherRelationship = personService.getAllRelationships().get(1);
        PatientTemplate template1 = createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, MESSAGE_TEMPLATE_NAME + "1", false);

        createPatientTemplate(patient2, otherRelationship.getPersonA(),
                otherRelationship, MESSAGE_TEMPLATE_NAME + "2", false);

        PatientTemplate template2 = createPatientTemplate(patient1, relationship.getPersonA(),
                relationship, MESSAGE_TEMPLATE_NAME + "3", false);

        Patient patientWithId = new Patient();
        patientWithId.setId(patient1.getId());

        MvcResult result = mockMvc.perform(get("/messages/details")
                .param(ROWS_PARAM, String.valueOf(THREE))
                .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
                .param(PERSON_ID_PARAM, patient1.getId().toString()))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));
        MessageDetailsDTO dto = getDtoFromMvcResult(result);

        assertThat(dto.getPersonId(), is(equalTo(patientWithId.getId())));
        assertThat(dto.getMessages().size(), is(equalTo(THREE)));

        for (int i = 0; i < 2; i++) {
            MessageDTO messageDTO = dto.getMessages().get(i);
            assertThat(messageDTO.getType(), containsString(MESSAGE_TEMPLATE_NAME));
            assertThat(messageDTO.getAuthor().getUsername(), is(equalTo("admin")));
            assertThat(messageDTO.getCreatedAt(), anyOf(
                    is(equalTo(template1.getDateCreated())),
                    is(equalTo(template2.getDateCreated()))));
            assertThat(dto.getMessages().get(0).getActorSchedules(),
                contains(
                    new ActorScheduleDTO(null, PATIENT_DEFAULT_ACTOR_TYPE,
                        EXPECTED_DEACTIVATED_STATE),
                    new ActorScheduleDTO(relationship.getPersonA().getId(),
                        relationship.getRelationshipType().getaIsToB(),
                        EXPECTED_DEACTIVATED_STATE)
                ));
        }

        MessageDTO messageDTO = dto.getMessages().get(2);
        assertThat(messageDTO.getType(), containsString(MESSAGE_TEMPLATE_NAME));
        assertThat(messageDTO.getAuthor(), is(nullValue()));
        assertThat(messageDTO.getCreatedAt(), is(nullValue()));
        assertThat(dto.getMessages().get(2).getActorSchedules(),
            hasItem(
                new ActorScheduleDTO(null, PATIENT_DEFAULT_ACTOR_TYPE,
                    EXPECTED_DEACTIVATED_STATE)
            ));

    }

    @Test
    public void testPaginationIsDisabledAndAllObjectsAreReturned() throws Exception {
        Relationship relationship = personService.getAllRelationships().get(0);

        // Page 1, with QUERY_TYPE_1
        createPatientTemplate(patient1, relationship.getPersonA(), relationship);

        createPatientTemplate(patient1, relationship.getPersonA(), relationship);

        createPatientTemplate(patient1, relationship.getPersonA(), relationship);

        // Page 1, with QUERY_TYPE_2
        createPatientTemplate(patient1, relationship.getPersonA(), relationship);

        createPatientTemplate(patient1, relationship.getPersonA(), relationship);

        createPatientTemplate(patient1, relationship.getPersonA(), relationship);

        Patient patientWithId = new Patient();
        patientWithId.setId(patient1.getId());

        // Fetch page 1
        MvcResult result = mockMvc.perform(get("/messages/details")
                .param(ROWS_PARAM, String.valueOf(SIX))
                .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
                .param(PERSON_ID_PARAM, patient1.getId().toString()))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));
        assertMessageDetailsDTO(getDtoFromMvcResult(result), QUERY_TYPE_1,
                patientWithId.getPatientId());

        // Fetch page 2
        result = mockMvc.perform(get("/messages/details")
                .param(ROWS_PARAM, String.valueOf(SIX))
                .param(PAGE_PARAM, String.valueOf(SECOND_PAGE))
                .param(PERSON_ID_PARAM, patient1.getId().toString()))
                .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        assertThat(result, is(notNullValue()));
        assertMessageDetailsDTO(getDtoFromMvcResult(result), QUERY_TYPE_2,
                patientWithId.getPatientId());
    }

    @Test
    @Ignore
    public void shouldReturnDetailsForExistingPatientTemplatesAndDefault() throws Exception {
        Relationship relationship = personService.getAllRelationships().get(0);

        int existingPatientTemplatesCount = TEN;
        for (int i = 0; i < existingPatientTemplatesCount; i++) {
            createPatientTemplate(patient1, relationship.getPersonA(),
                relationship);
        }

        int templatesForDefaultAttachment = FIVE;
        for (int i = 0; i < templatesForDefaultAttachment; i++) {
            createTemplate(String.format("Template %s without PT", i));

        }

        // Fetch page 1
        MvcResult result = mockMvc.perform(get("/messages/details")
            .param(ROWS_PARAM, String.valueOf(PAGE_SIZE_ONE_HUNDRED))
            .param(PAGE_PARAM, String.valueOf(FIRST_PAGE))
            .param(PERSON_ID_PARAM, patient1.getId().toString()))
            .andExpect(status().is(HttpStatus.OK.value())).andReturn();

        MessageDetailsDTO messageDetailsDTO = getDtoFromMvcResult(result);

        int summarySize = existingPatientTemplatesCount + templatesForDefaultAttachment;
        Assert.assertThat(messageDetailsDTO.getMessages(), hasSize(summarySize));

        for (int i = 0; i < existingPatientTemplatesCount; i++) {
            assertIsRelatedToExistingPatientTemplate(messageDetailsDTO.getMessages()
                .get(i));
        }
        for (int i = 0; i < templatesForDefaultAttachment; i++) {
            assertIsBasedOnDefaultTemplate(messageDetailsDTO.getMessages()
                .get(existingPatientTemplatesCount + i));
        }
    }

    @Test
    public void shouldReturnMessages() throws Exception {
        createPatientTemplate(
                patient1,
                patient1,
                null
        );

        final Date startDate = DateUtils.addDays(new Date(), -1);
        final Date endDate = DateUtils.addDays(new Date(), 3);

        MvcResult result = mockMvc.perform(get("/messages")
                .param(PERSON_ID_PARAM, patient1.getId().toString())
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
        assertThat(serviceResult.getChannelType(), is(Constant.CHANNEL_TYPE_CALL));
    }

    private void assertMessageDetailsDTO(MessageDetailsDTO dto, String queryType, Integer patientId) {
        assertThat(dto.getPersonId(), is(equalTo(patientId)));
        assertThat(dto.getMessages().size(), is(equalTo(SIX)));

        for (MessageDTO messageDTO : dto.getMessages()) {
            assertThat(messageDTO.getType(), is(MESSAGE_TEMPLATE_NAME));
        }
    }

    private MessageDetailsDTO getDtoFromMvcResult(MvcResult result) throws IOException {
        return TestUtil.readValue(
            result.getResponse().getContentAsString(),
            MessageDetailsDTO.class);
    }

    private List<ServiceResultList> getResultListFromMvcResult(MvcResult result) throws IOException {
        return TestUtil.readValueAsList(result.getResponse().getContentAsString(),
                new TypeReference<List<ServiceResultList>>() {
                });
    }

    private PatientTemplate createPatientTemplate(Patient patient, Person person,
                                                  Relationship relationship,
                                                  String templateName,
                                                  boolean isServiceActive) {
        Template template = createTemplate(templateName);
        PatientTemplate patientTemplate = new PatientTemplateBuilder()
                .withActor(person)
                .withActorType(relationship)
                .withPatient(patient)
                .withTemplate(template)
                .buildAsNew();
        if (isServiceActive) {
            TemplateFieldValue templateFieldValue = crateTemplateFieldValue(template, patientTemplate);
            patientTemplate.setTemplateFieldValues(Collections.singletonList(templateFieldValue));
        }
        return patientTemplateDao.saveOrUpdate(patientTemplate);
    }

    private TemplateFieldValue crateTemplateFieldValue(Template template, PatientTemplate patientTemplate) {
        return new TemplateFieldValueBuilder()
                    .withTemplateField(template.getTemplateFields().get(0))
                    .withValue(Constant.CHANNEL_TYPE_CALL)
                    .withPatientTemplate(patientTemplate)
                    .buildAsNew();
    }

    private PatientTemplate createPatientTemplate(Patient patient, Person person,
                                                  Relationship relationship) {
        return createPatientTemplate(patient, person, relationship,
            MESSAGE_TEMPLATE_NAME, true);
    }

    private Template createTemplate(String templateName) {
        Template template = new TemplateBuilder()
                .withServiceQuery("SELECT now() AS EXECUTION_DATE, 1 AS MESSAGE_ID, 'Call' AS CHANNEL_ID;")
                .withServiceQueryType("SQL")
                .setName(templateName)
                .withCalendarServiceQuery("")
                .buildAsNew();
        template.setTemplateFields(createTemplateFields(template));
        return templateDao.saveOrUpdate(template);
    }

    private List<TemplateField> createTemplateFields(Template template) {
        TemplateField templateField = new TemplateFieldBuilder()
                .withTemplateFieldType(TemplateFieldType.SERVICE_TYPE)
                .withDefaultValue(Constant.CHANNEL_TYPE_CALL)
                .withTemplate(template)
                .buildAsNew();
        return Collections.singletonList(templateField);
    }

    private void assertIsRelatedToExistingPatientTemplate(MessageDTO messageDTO) {
        Assert.assertEquals(messageDTO.getAuthor().getUsername(), "admin");
        Assert.assertNotNull(messageDTO.getCreatedAt());
    }

    private void assertIsBasedOnDefaultTemplate(MessageDTO messageDTO) {
        Assert.assertNull(messageDTO.getAuthor());
        Assert.assertNull(messageDTO.getCreatedAt());
    }
}
