/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * <p>
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */

package org.openmrs.module.messages.web.it;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.messages.ApiConstant;
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

import java.io.IOException;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
public class PersonStatusControllerITTest extends BaseModuleWebContextSensitiveWithActivatorTest {

    private static final String XML_DATASET_PATH = "datasets/";

    private static final String BASE_URL_PATTERN = "/messages/person-statuses/%s";

    private static final String URL_REASONS_SUFFIX = "/reasons";

    private static final String XML_PERSON_ATTRIBUTE_DATASET = XML_DATASET_PATH + "PersonAttributeDataset.xml";

    private static final String PERSON_UUID = "d64b7f82-5857-48f5-8694-b1bdb46688c3";

    private static final String VOIDED_CONTACT_TIME = "48212932834";

    private static final String ACTUAL_CONTACT_TIME = "48421029382";

    private static final String VOIDED_STATUS = PersonStatus.NO_CONSENT.name();

    private static final String ACTUAL_STATUS = PersonStatus.ACTIVATED.name();

    private static final String EXPECTED_ACTIVATED_STYLE =
            "background-color: #51a351; border-color: #51a351; color: #f5f5f5;";

    private static final String EXPECTED_MISSING_STYLE =
            "background-color: #EEA616; border-color: #EEA616; color: #f5f5f5;";

    private static final String EXPECTED_DEACTIVATE_STYLE =
            "background-color: #f23722; border-color: #f23722; color: #f5f5f5;";

    private static final String NOT_EXIST_PERSON = "8124263d-d1ec-4be5-bf62-502ab125d076";

    private static final String PERSON_WITHOUT_ATTRIBUTE = "8c8169be-94f0-42ba-81de-cf3f2c12a7ec";

    private static final Integer PERSON_ID_WITHOUT_ATTRIBUTE = 99923;

    private static final String TEST_INVALID_REASON = "The status was changed for test reason";

    private static final String EXPECTED_ERROR = "Could not fetch person status for personIdOrUuid: %s";

    private static final String NO_VALID_VALUE = "No value";

    private static final String NO_VALID_VALUE_EXPECTED_ERROR = "Not valid value of status: %s";

    private static final String NO_VALID_REASON_EXPECTED_ERROR = "Not valid value of reason: %s";

    private static final int EXPECTED_REASON_SIZE = 6;

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
        Context.flushSession();
    }

    @Test
    public void shouldFetchStatusByPersonId() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, person.getId().toString())))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(is(person.getId())))
                .andExpect(jsonPath("$.title").value(is(PersonStatus.ACTIVATED.getTitleKey())))
                .andExpect(jsonPath("$.value").value(is(PersonStatus.ACTIVATED.name())))
                .andExpect(jsonPath("$.style").value(is(EXPECTED_ACTIVATED_STYLE)))
                .andExpect(jsonPath("$.reason").value(is(nullValue())));
    }

    @Test
    public void shouldFetchStatusByPersonUuid() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, person.getUuid())))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(person.getPersonId()))
                .andExpect(jsonPath("$.title").value(PersonStatus.ACTIVATED.getTitleKey()))
                .andExpect(jsonPath("$.value").value(PersonStatus.ACTIVATED.name()))
                .andExpect(jsonPath("$.style").value(EXPECTED_ACTIVATED_STYLE))
                .andExpect(jsonPath("$.reason").value(nullValue()));
    }

    @Test
    public void shouldReturnEntityNotFoundExceptionIfPersonNotExists() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, NOT_EXIST_PERSON))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.[0].code").value(ErrorMessageEnum.ERR_ENTITY_NOT_FOUND.getCode()))
                .andExpect(jsonPath("$.errorMessages.[0].message").value(String.format(EXPECTED_ERROR, NOT_EXIST_PERSON)));
    }

    @Test
    public void shouldFetchMissingStatusIfPersonHasNotStatusAttribute() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, PERSON_WITHOUT_ATTRIBUTE)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(PERSON_ID_WITHOUT_ATTRIBUTE))
                .andExpect(jsonPath("$.title").value(PersonStatus.MISSING_VALUE.getTitleKey()))
                .andExpect(jsonPath("$.value").value(PersonStatus.MISSING_VALUE.name()))
                .andExpect(jsonPath("$.style").value(EXPECTED_MISSING_STYLE))
                .andExpect(jsonPath("$.reason").value(nullValue()));
    }

    @Test
    public void shouldUpdateStatusAttribute() throws Exception {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getPersonId())
                .setValue(PersonStatus.DEACTIVATED.name())
                .setReason(Constant.STATUS_REASON_PAUSE);

        mockMvc.perform(put(String.format(BASE_URL_PATTERN, person.getUuid()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(status)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.personId").value(person.getPersonId()))
                .andExpect(jsonPath("$.title").value(PersonStatus.DEACTIVATED.getTitleKey()))
                .andExpect(jsonPath("$.value").value(PersonStatus.DEACTIVATED.name()))
                .andExpect(jsonPath("$.style").value(EXPECTED_DEACTIVATE_STYLE))
                .andExpect(jsonPath("$.reason").value(Constant.STATUS_REASON_PAUSE));
    }

    @Test
    public void shouldThrowExceptionWhenNoValidStatusValue() throws Exception {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getPersonId())
                .setValue(NO_VALID_VALUE)
                .setReason(Constant.STATUS_REASON_PAUSE);

        mockMvc.perform(put(String.format(BASE_URL_PATTERN, person.getUuid()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(status)))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.[0].code")
                        .value(ErrorMessageEnum.ERR_SYSTEM.getCode()))
                .andExpect(jsonPath("$.errorMessages.[0].message")
                        .value(String.format(NO_VALID_VALUE_EXPECTED_ERROR, NO_VALID_VALUE)));
    }

    @Test
    public void shouldThrowExceptionWhenMissingStatusValue() throws Exception {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getPersonId())
                .setValue(PersonStatus.MISSING_VALUE.name())
                .setReason(Constant.STATUS_REASON_PAUSE);

        mockMvc.perform(put(String.format(BASE_URL_PATTERN, person.getUuid()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(status)))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.[0].code")
                        .value(ErrorMessageEnum.ERR_SYSTEM.getCode()))
                .andExpect(jsonPath("$.errorMessages.[0].message")
                        .value(String.format(NO_VALID_VALUE_EXPECTED_ERROR, PersonStatus.MISSING_VALUE.name())));
    }

    @Test
    public void shouldThrowExceptionWhenInvalidReasonValue() throws Exception {
        PersonStatusDTO status = new PersonStatusDTO()
                .setPersonId(person.getPersonId())
                .setValue(PersonStatus.DEACTIVATED.name())
                .setReason(TEST_INVALID_REASON);

        mockMvc.perform(put(String.format(BASE_URL_PATTERN, person.getUuid()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(status)))
                .andExpect(content().contentType(ApiConstant.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.errorMessages.[0].code")
                        .value(ErrorMessageEnum.ERR_SYSTEM.getCode()))
                .andExpect(jsonPath("$.errorMessages.[0].message")
                        .value(String.format(NO_VALID_REASON_EXPECTED_ERROR, TEST_INVALID_REASON)));
    }

    @Test
    public void shouldFetchPossibleStatusReasons() throws Exception {
        mockMvc.perform(get(String.format(BASE_URL_PATTERN, URL_REASONS_SUFFIX)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(EXPECTED_REASON_SIZE))
                .andExpect(jsonPath("$.[*]").value(hasItem(Constant.STATUS_REASON_DECEASED)))
                .andExpect(jsonPath("$.[*]").value(hasItem(Constant.STATUS_REASON_LOST_FOLLOW_UP)))
                .andExpect(jsonPath("$.[*]").value(hasItem(Constant.STATUS_REASON_PAUSE)))
                .andExpect(jsonPath("$.[*]").value(hasItem(Constant.STATUS_REASON_VACATION)))
                .andExpect(jsonPath("$.[*]").value(hasItem(Constant.STATUS_REASON_TRANSFERRED)))
                .andExpect(jsonPath("$.[*]").value(hasItem(Constant.STATUS_REASON_OTHER)));
    }

    private String json(Object obj) throws IOException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
